package com.tokopedia.core.geolocation.presenter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by hangnadi on 1/29/16.
 */
public interface GoogleMapPresenter {

    double DEFAULT_LATITUDE = -6.175794;
    double DEFAULT_LONGITUDE = 106.826457;

    LatLng DEFAULT_LATLNG_JAKARTA =
            new LatLng(DEFAULT_LATITUDE, DEFAULT_LONGITUDE);

    long DEFAULT_UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            DEFAULT_UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    void onGoogleApiConnected(Bundle bundle);

    void onGoogleApiSuspended(int cause);

    void onGoogleApiFailed(ConnectionResult connectionResult);

    void onResult(LocationSettingsResult locationSettingsResult);

    LatLng getLastLocation();

    void requestLocationUpdate();

    void connectGoogleApi();

    void disconnectGoogleApi();

    void removeLocationUpdate();

    void initDefaultLocation();

    void onCameraChange(Context context, CameraPosition cameraPosition);

    void prepareAutoCompleteView();

    void onSuggestionItemClick(AdapterView<?> adapter, int position);

    void onSubmitPointer(Activity activity);

    void restoreStateData(Bundle savedState);

    void saveStateCurrentLocation(Bundle state);

    void onDestroy();

    void prepareDetailDestination(View view);
}
