package com.tokopedia.core.geolocation.listener;

import android.content.res.Configuration;
import android.view.View;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.tokopedia.core.geolocation.domain.IMapsRepository;
import com.tokopedia.core.network.apiservices.maps.MapService;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by hangnadi on 1/31/16.
 */
public interface GoogleMapView {

    int LANDSCAPE = Configuration.ORIENTATION_LANDSCAPE;
    int PORTRAIT = Configuration.ORIENTATION_PORTRAIT;

    /**
     *
     */
    void prepareActionBarView();

    /**
     *
     */
    void setToolbarMap();

    /**
     *
     */
    void setMyLocButton();

    /**
     *
     */
    void initMapListener();

    /**
     *
     */
    void initMapView();

    /**
     * @param latLng
     */
    void moveMap(LatLng latLng);

    /**
     * @param result
     */
    void checkLocationSettings(PendingResult<LocationSettingsResult> result);

    /**
     *
     */
    void showDialogError(Status status);

    void prepareDetailDestination(View view);

    /**
     * @param googleApiClient
     * @param latLngBounds
     */
    void initAutoCompleteAdapter(CompositeSubscription compositeSubscription,
                                 MapService service, IMapsRepository repository,
                                 GoogleApiClient googleApiClient, LatLngBounds latLngBounds);

    /**
     *
     */
    void setAutoCompleteAdaoter();

    /**
     * @param s
     */
    void toastMessage(String s);

    /**
     * @param s
     */
    void setValuePointer(String s);

    /**
     * @param bounds
     * @return
     */
    void setAutoCompleteBounds(LatLngBounds bounds);

    /**
     *
     */
    void hideActionBar();

    /**
     *
     */
    void showActionBar();

    /**
     *
     */
    void generateBoundsFromCamera();

    /**
     *
     */
    void hideDetailDestination();

    /**
     * @param view
     */
    void showDetailDestination(View view);

    /**
     * show manual address inputted by user
     *
     * @param s Manual Address
     */
    void setManualDestination(String s);
}
