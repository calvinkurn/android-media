package com.tokopedia.logisticaddaddress.features.pinpoint;

import android.content.res.Configuration;
import android.view.View;
import android.widget.AdapterView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.tokopedia.logisticaddaddress.data.IMapsRepository;
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.LocationPass;

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

        void setLoading(boolean active);
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

        LocationPass getUpdateLocation();

        void onResult(LocationSettingsResult locationSettingsResult);

        void requestLocationUpdate();

        void connectGoogleApi();

        void disconnectGoogleApi();

        void removeLocationUpdate();

        void getReverseGeoCoding(String latitude, String longitude);

        void prepareAutoCompleteView();

        void onSuggestionItemClick(AdapterView<?> adapter, int position);

        void onDestroy();

        void prepareDetailDestination(View view);

        void onMapReady();
    }
}
