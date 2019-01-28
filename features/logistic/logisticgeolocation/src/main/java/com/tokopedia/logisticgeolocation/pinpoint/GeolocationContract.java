package com.tokopedia.logisticgeolocation.pinpoint;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.LocationPass;
import com.tokopedia.logisticgeolocation.data.IMapsRepository;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Fajar Ulin Nuha on 29/10/18.
 */
public interface GeolocationContract {

    interface GeolocationView {
        int LANDSCAPE = Configuration.ORIENTATION_LANDSCAPE;
        int PORTRAIT = Configuration.ORIENTATION_PORTRAIT;

        void prepareActionBarView();

        void setToolbarMap();

        void setMyLocButton();

        void initMapListener();

        void initMapView();

        void moveMap(LatLng latLng);

        void checkLocationSettings(PendingResult<LocationSettingsResult> result);

        void showDialogError(Status status);

        void prepareDetailDestination(View view);

        void initAutoCompleteAdapter(CompositeSubscription compositeSubscription, IMapsRepository repository,
                                     GoogleApiClient googleApiClient, LatLngBounds latLngBounds);

        void setAutoCompleteAdaoter();

        void toastMessage(String s);

        void setValuePointer(String s);

        void setAutoCompleteBounds(LatLngBounds bounds);

        void hideActionBar();

        void showActionBar();

        void generateBoundsFromCamera();

        void hideDetailDestination();

        void showDetailDestination(View view);

        void setManualDestination(String s);
    }

    interface GeolocationPresenter {

        double DEFAULT_LATITUDE = -6.175794;
        double DEFAULT_LONGITUDE = 106.826457;

        LatLng DEFAULT_LATLNG_JAKARTA =
                new LatLng(DEFAULT_LATITUDE, DEFAULT_LONGITUDE);

        long DEFAULT_UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
        long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
                DEFAULT_UPDATE_INTERVAL_IN_MILLISECONDS / 2;

        void setUpVariables(LocationPass locationPass, boolean hasLocation);

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

        Bundle saveStateCurrentLocation(Bundle state);

        void onDestroy();

        void prepareDetailDestination(View view);

        void onMapReady();
    }
}
