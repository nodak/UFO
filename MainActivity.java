import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity implements LocationListener {
	
	private GoogleMap googleMap;
	private long back_pressed;
	private int mapType;
	private double latitude;
	private double longitude;
	private LatLng latLng;
	
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );
		setUpMapIfNeeded();
	}

	private void setUpMapIfNeeded() {
		if ( null == googleMap ) {
			googleMap = ( ( SupportMapFragment ) getSupportFragmentManager().findFragmentById( R.id.map )).getMap();
			if  ( null != googleMap ) {
				setUpMap();
			}
		}
	}

	private void setUpMap() {
		googleMap.setMyLocationEnabled( true );
		LocationManager locationManager = ( LocationManager ) getSystemService( LOCATION_SERVICE );
		Criteria criteria = new Criteria();
		String provider = locationManager.getBestProvider( criteria, true );
		Location myLocation = locationManager.getLastKnownLocation( provider );
		if( null != myLocation ) {
			onLocationChanged( myLocation );
		} else {
			locationManager.requestLocationUpdates( provider, 20000, 0, this );
		}
		setMapType();
		googleMap.addMarker( new MarkerOptions().position( new LatLng( latitude, longitude ) ).title( "I'm" ) );
	}
	
	public void onLocationChanged( Location location ) {
		latitude = location.getLatitude();
		longitude = location.getLongitude();
		latLng = new LatLng( latitude, longitude );
		googleMap.moveCamera( CameraUpdateFactory.newLatLng( latLng ) );
		googleMap.animateCamera( CameraUpdateFactory.zoomTo( 20 ) );
	}

	private void setMapType() {
		if ( 0 == mapType ) {
			mapType = GoogleMap.MAP_TYPE_HYBRID;
			googleMap.setMapType( mapType );
		} else {
			googleMap.setMapType( mapType );
		}
	}

	@Override
	public boolean onCreateOptionsMenu( Menu menu ) {
		getMenuInflater().inflate( R.menu.main, menu );
		return true;
	}
		
	@Override
	public void onBackPressed() {
		if ( back_pressed + 2000 > System.currentTimeMillis() ) {
			//super.onBackPressed();
			finish();
		} else {
			Toast.makeText( getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT ).show();
			back_pressed = System.currentTimeMillis();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch ( item.getItemId() ) {
		case R.id.action_item1:
			mapType = GoogleMap.MAP_TYPE_NORMAL;
			googleMap.setMapType( mapType );
			return true;
		case R.id.action_item2:
			mapType = GoogleMap.MAP_TYPE_SATELLITE;
			googleMap.setMapType( mapType );
			return true;
		case R.id.action_item3:
			mapType = GoogleMap.MAP_TYPE_TERRAIN;
			googleMap.setMapType( mapType );
			return true;
		default:
			return true;
		}
	}

	@Override
	public void onProviderDisabled( String provider ) {
	}

	@Override
	public void onProviderEnabled( String provider ) {
	}

	@Override
	public void onStatusChanged( String provider, int status, Bundle extras ) {
	}
}
