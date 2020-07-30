package com.tokopedia.logisticaddaddress.features.pinpoint;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.authentication.AuthHelper;
import com.tokopedia.logisticaddaddress.data.RetrofitInteractor;
import com.tokopedia.logisticaddaddress.data.RetrofitInteractorImpl;
import com.tokopedia.logisticaddaddress.di.ActivityContext;
import com.tokopedia.logisticaddaddress.di.GeolocationScope;
import com.tokopedia.logisticaddaddress.domain.mapper.GeolocationMapper;
import com.tokopedia.logisticaddaddress.utils.LocationCache;
import com.tokopedia.logisticdata.data.constant.LogisticConstant;
import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.LocationPass;
import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.viewmodel.PredictionResult;
import com.tokopedia.logisticdata.data.entity.geolocation.coordinate.viewmodel.CoordinateViewModel;
import com.tokopedia.logisticdata.data.entity.response.KeroMapsAutofill;
import com.tokopedia.logisticdata.data.utils.GeoLocationUtils;
import com.tokopedia.logisticdata.domain.usecase.RevGeocodeUseCase;
import com.tokopedia.network.utils.TKPDMapParam;
import com.tokopedia.user.session.UserSession;

import javax.inject.Inject;

import rx.Subscriber;
import timber.log.Timber;

@GeolocationScope
public class GeolocationPresenter implements GeolocationContract.GeolocationPresenter, LocationListener {

    public static final String CACHE_LATITUDE_LONGITUDE = "cache_latitude_longitude";
    public static final String CACHE_LATITUDE = "cache_latitude";
    public static final String CACHE_LONGITUDE = "cache_longitude";

    private final GeolocationContract.GeolocationView view;
    private final RetrofitInteractor retrofitInteractor;
    private final GoogleApiClient googleApiClient;
    private final LocationRequest locationRequest;
    private UserSession userSession;
    private RevGeocodeUseCase revGeocodeUseCase;
    private GeolocationMapper mapper;

    private Context context;

    private boolean hasLocation;
    private LocationPass locationPass;

    @Inject
    public GeolocationPresenter(@ActivityContext Context context, RetrofitInteractorImpl retrofitInteractor,
                                UserSession userSession, RevGeocodeUseCase revGeocodeUseCase,
                                GoogleMapFragment googleMapFragment, GeolocationMapper geolocationMapper) {
        this.context = context;
        this.userSession = userSession;
        this.view = googleMapFragment;
        this.retrofitInteractor = retrofitInteractor;
        this.revGeocodeUseCase = revGeocodeUseCase;
        this.mapper = geolocationMapper;
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
    }

    @Override
    public void onLocationChanged(Location location) {
        view.moveMap(GeoLocationUtils.generateLatLng(location.getLatitude(), location.getLongitude()));
        LocationCache.saveLocation(context, location);
        removeLocationUpdate();
    }

    @Override
    public void setUpVariables(LocationPass locationPass, boolean hasLocation) {
        this.locationPass = locationPass;
        this.hasLocation = hasLocation;
        if (hasLocation) {
            Location location = new Location(LocationManager.NETWORK_PROVIDER);
            location.setLatitude(Double.parseDouble(locationPass.getLatitude()));
            location.setLongitude(Double.parseDouble(locationPass.getLongitude()));
            LocationCache.saveLocation(context, location);
        }
    }

    @Override
    public LocationPass getUpdateLocation() {
        return locationPass;
    }

    private void getExistingLocation() {
        view.moveMap(GeoLocationUtils.generateLatLng(locationPass.getLatitude(), locationPass.getLongitude()));
    }

    @Override
    public void onResult(LocationSettingsResult locationSettingsResult) {
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

    @SuppressLint("MissingPermission")
    private LatLng getLastLocation() {
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
            Timber.d("Google play services available");
            return true;
        } else {
            Timber.d("Google play services unavailable");
            return false;
        }
    }

    @SuppressLint("MissingPermission")
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
    public void getReverseGeoCoding(String latitude, String longitude) {
        String keyword = String.format("%s,%s", latitude, longitude);
        view.setLoading(true);
        revGeocodeUseCase.execute(keyword)
                .subscribe(new Subscriber<KeroMapsAutofill>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.setLoading(false);
                        view.setValuePointer("Error");
                    }

                    @Override
                    public void onNext(KeroMapsAutofill keroMapsAutofill) {
                        view.setLoading(false);
                        view.setValuePointer(keroMapsAutofill.getData().getFormattedAddress());
                        setNewLocationPass(mapper.map(keroMapsAutofill));
                    }
                });
    }

    private void setNewLocationPass(LocationPass locationPass) {
        this.locationPass = locationPass;
    }

    @Override
    public void prepareAutoCompleteView() {
        view.initAutoCompleteAdapter(retrofitInteractor.getCompositeSubscription(),
                retrofitInteractor.getMapRepository(),
                googleApiClient, setDefaultBoundsJakarta());
        view.setAutoCompleteAdaoter();
    }

    @Override
    public void prepareDetailDestination(View rootview) {
        if (hasLocation) {
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
        if (!hasLocation) {
            LocationSettingsRequest.Builder locationSettingsRequest = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            locationSettingsRequest.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi
                            .checkLocationSettings(googleApiClient, locationSettingsRequest.build());

            view.checkLocationSettings(result);
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

        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put("placeid", placeID);
        retrofitInteractor.generateLatLng(
                AuthHelper.generateParamsNetwork(userSession.getUserId(), userSession.getDeviceId(), param),
                latLongListener());
    }

    @Override
    public void onDestroy() {
        retrofitInteractor.unSubscribe();
        revGeocodeUseCase.unsubscribe();
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