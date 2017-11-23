package com.tokopedia.core.geolocation.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.fingerprint.LocationCache;
import com.tokopedia.core.geolocation.activity.GeolocationActivity;
import com.tokopedia.core.geolocation.fragment.GoogleMapFragment;
import com.tokopedia.core.geolocation.interactor.RetrofitInteractor;
import com.tokopedia.core.geolocation.interactor.RetrofitInteractorImpl;
import com.tokopedia.core.geolocation.listener.GoogleMapView;
import com.tokopedia.core.geolocation.model.autocomplete.LocationPass;
import com.tokopedia.core.geolocation.model.autocomplete.Prediction;
import com.tokopedia.core.geolocation.model.autocomplete.viewmodel.PredictionResult;
import com.tokopedia.core.geolocation.model.coordinate.viewmodel.CoordinateViewModel;
import com.tokopedia.core.geolocation.utils.GeoLocationUtils;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

/**
 * Created by hangnadi on 1/31/16.
 */
public class GoogleMapPresenterImpl implements GoogleMapPresenter, LocationListener {

    private static final String TAG = GoogleMapPresenterImpl.class.getSimpleName();
    private static final String STATE_IS_ALLOW_GENERATE_ADDRESS = "STATE_IS_ALLOW_GENERATE_ADDRESS";
    private static final String STATE_IS_USE_EXISTING_LOCATION = "STATE_IS_USE_EXISTING_LOCATION";
    public static final String CACHE_LATITUDE_LONGITUDE = "cache_latitude_longitude";
    public static final String CACHE_LATITUDE = "cache_latitude";
    public static final String CACHE_LONGITUDE = "cache_longitude";

    private final GoogleMapView view;
    private final RetrofitInteractor retrofitInteractor;
    private final GoogleApiClient googleApiClient;
    private final LocationRequest locationRequest;

    private Context context;

    private boolean isUseExistingLocation;
    private boolean isAllowGenerateAddress;

    private LocationPass locationPass;

    public GoogleMapPresenterImpl(Context context, GoogleMapFragment googleMapFragment, LocationPass locationPass) {
        this.context = context;
        this.view = googleMapFragment;
        this.retrofitInteractor = new RetrofitInteractorImpl();
        this.locationRequest = LocationRequest.create()
                .setInterval(DEFAULT_UPDATE_INTERVAL_IN_MILLISECONDS)
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        this.googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(googleMapFragment)
                .addOnConnectionFailedListener(googleMapFragment)
                .build();
        this.isAllowGenerateAddress = true;
        this.locationPass = locationPass;
        if(locationPass!=null)
        {
            Location location = new Location(LocationManager.NETWORK_PROVIDER);
            location.setLatitude(Double.parseDouble(locationPass.getLatitude()));
            location.setLongitude(Double.parseDouble(locationPass.getLongitude()));
            LocationCache.saveLocation(context, location);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged");
        view.moveMap(GeoLocationUtils.generateLatLng(location.getLatitude(), location.getLongitude()));
        LocationCache.saveLocation(context, location);
        removeLocationUpdate();
    }

    @Override
    public void onGoogleApiConnected(Bundle bundle) {
        Log.d(TAG, "onGoogleApiConnected");
    }

    private void getNewLocation() {
        checkLocationSettings();
    }

    private void getExistingLocation() {
        checkLocationSettings();
        setExistingLocationState(true);
        view.moveMap(GeoLocationUtils.generateLatLng(locationPass.getLatitude(), locationPass.getLongitude()));
    }

    private void setExistingLocationState(boolean b) {
        isUseExistingLocation = b;
    }

    private void checkLocationSettings() {
        LocationSettingsRequest.Builder locationSettingsRequest = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        locationSettingsRequest.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi
                        .checkLocationSettings(googleApiClient, locationSettingsRequest.build());

        view.checkLocationSettings(result);
    }

    @Override
    public void onGoogleApiSuspended(int cause) {

    }

    @Override
    public void onGoogleApiFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onResult(LocationSettingsResult locationSettingsResult) {
        Log.d(TAG, "onResult");
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                view.moveMap(getLastLocation());
                requestLocationUpdate();
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                view.showDialogError(status);
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                break;
            default:
                break;
        }
    }

    @Override
    public LatLng getLastLocation() {
        try {
            if (isServiceConnected()) {
                Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                LocationCache.saveLocation(context, location);
                return new LatLng(location.getLatitude(), location.getLongitude());
            } else {
                return DEFAULT_LATLNG_JAKARTA;
            }
        } catch (Exception e) {
            return DEFAULT_LATLNG_JAKARTA;
        }
    }

    private boolean isServiceConnected() {
        GoogleApiAvailability availability = GoogleApiAvailability.getInstance();

        int resultCode = availability.isGooglePlayServicesAvailable(context);

        if (ConnectionResult.SUCCESS == resultCode) {
            CommonUtils.dumper("Google play services available");
            return true;
        } else {
            CommonUtils.dumper("Google play services unavailable");
            return false;
        }
    }

    @Override
    public void requestLocationUpdate() {
        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
    }

    @Override
    public void removeLocationUpdate() {
        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }
    }

    @Override
    public void initDefaultLocation() {
        view.moveMap(DEFAULT_LATLNG_JAKARTA);
    }

    @Override
    public void connectGoogleApi() {
        googleApiClient.connect();
    }

    @Override
    public void disconnectGoogleApi() {
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onCameraChange(Context context, CameraPosition cameraPosition) {
        setAutoCompleteBoundary();
        if (!isUseExistingLocation) {
            saveLatLng(cameraPosition.target);
            generateAddress(context);
        } else {
            setExistingLocationState(false);
            view.setValuePointer(locationPass.getGeneratedAddress());
        }
    }

    private void setAutoCompleteBoundary() {
        view.generateBoundsFromCamera();
    }

    private void generateAddress(final Context context) {
        if (isAllowGenerateAddress) {
            retrofitInteractor.generateAddress(context, new RetrofitInteractor.GenerateAddressListener() {

                @Override
                public void onSuccess(LocationPass locationPass) {
                    allowReverseGeocode(true);
                    setNewLocationPass(locationPass);
                    view.setValuePointer(locationPass.getGeneratedAddress());
                }

                @Override
                public void onError(Throwable e) {
                    allowReverseGeocode(true);
                    view.setValuePointer("Error");
                }

                @Override
                public void onPreConnection() {
                    allowReverseGeocode(false);
                    view.setValuePointer(context.getString(R.string.wait_generate_address));
                }

                @Override
                public String getAddress(double latitude, double longitude) {
                    String resultGeocode = GeoLocationUtils.reverseGeoCode(context, latitude, longitude);
                    if (resultGeocode.contains(String.valueOf(latitude)) || resultGeocode.contains(String.valueOf(longitude))) {
                        return context.getString(R.string.choose_this_location);
                    } else {
                        return resultGeocode;
                    }
                }

                @Override
                public LocationPass convertData(double latitude, double longitude) {
                    LocationPass temp = new LocationPass();
                    temp.setLatitude(String.valueOf(latitude));
                    temp.setLongitude(String.valueOf(longitude));
                    temp.setGeneratedAddress(getAddress(latitude, longitude));
                    return temp;
                }
            });
        }
    }

    private void setNewLocationPass(LocationPass locationPass) {
        this.locationPass = locationPass;
    }

    private void allowReverseGeocode(boolean b) {
        isAllowGenerateAddress = b;
    }

    private void saveLatLng(LatLng target) {
        LocalCacheHandler cache = new LocalCacheHandler(context, CACHE_LATITUDE_LONGITUDE);
        cache.putString(CACHE_LATITUDE, String.valueOf(target.latitude));
        cache.putString(CACHE_LONGITUDE, String.valueOf(target.longitude));
        cache.applyEditor();
    }

    @Override
    public void prepareAutoCompleteView() {
        view.initAutoCompleteAdapter(retrofitInteractor.getCompositeSubscription(),
                retrofitInteractor.getMapService(),
                retrofitInteractor.getMapRepository(),
                googleApiClient, setDefaultBoundsJakarta());
        view.setAutoCompleteAdaoter();
    }

    @Override
    public void prepareDetailDestination(View rootview) {
        if (locationPass != null) {
            if (locationPass.getManualAddress() == null) {
                view.hideDetailDestination();
            } else {
                view.showDetailDestination(rootview);
                view.setManualDestination(locationPass.getManualAddress());
            }
        } else {
            view.hideDetailDestination();
        }
    }

    @Override
    public void onMapReady() {
        if (locationPass == null) {
            getNewLocation();
        } else {
            getExistingLocation();
        }
    }

    private LatLngBounds setDefaultBoundsJakarta() {
        return GeoLocationUtils.generateBoundary(DEFAULT_LATITUDE, DEFAULT_LONGITUDE);
    }

    @Override
    public void onSuggestionItemClick(AdapterView<?> adapter, int position) {
        final PredictionResult item = (PredictionResult) adapter.getItemAtPosition(position);
        final String placeID = item.getPlaceId();
        final CharSequence primaryText = item.getMainText();

        Log.d(TAG, "AutoComplete item selected: " + primaryText);

        //TODO summon service di sini
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put("placeid", placeID);
        retrofitInteractor.generateLatLng(context,
                AuthUtil.generateParamsNetwork(context, param),
                latLongListener());
    }

    @Override
    public void onSubmitPointer(Activity activity) {
        if (isAllowGenerateAddress) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(GeolocationActivity.EXTRA_EXISTING_LOCATION, locationPass);
            Intent intent = new Intent();
            intent.putExtras(bundle);
            intent.putExtra(GeolocationActivity.EXTRA_EXISTING_LOCATION, locationPass);
            activity.setResult(Activity.RESULT_OK, intent);
            activity.finish();
        }
    }

    @Override
    public void restoreStateData(Bundle savedState) {
        if (savedState != null) {
            isAllowGenerateAddress = savedState.getBoolean(STATE_IS_ALLOW_GENERATE_ADDRESS);
            isUseExistingLocation = savedState.getBoolean(STATE_IS_USE_EXISTING_LOCATION);
        }
    }

    @Override
    public void saveStateCurrentLocation(Bundle state) {
        state.putBoolean(STATE_IS_ALLOW_GENERATE_ADDRESS, isAllowGenerateAddress);
        state.putBoolean(STATE_IS_USE_EXISTING_LOCATION, isUseExistingLocation);
    }

    @Override
    public void onDestroy() {
        retrofitInteractor.unSubscribe();
    }

    private RetrofitInteractor.GenerateLatLongListener latLongListener() {
        return new RetrofitInteractor.GenerateLatLongListener() {
            @Override
            public void onSuccess(CoordinateViewModel model) {
                view.moveMap(model.getCoordinate());
            }

            @Override
            public void onError(String errorMessage) {

            }
        };
    }
}