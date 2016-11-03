package com.tokopedia.tkpd.geolocation.interactor;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by hangnadi on 1/31/16.
 */
public interface CacheInteractor {

    void storeLatLng(double latitude, double longitude);

    void dropCache();

    void getLastCache(OnGetLastCacheListener listener);

    interface OnGetLastCacheListener {

        void onSuccess(LatLng latLng);

        void onError();
    }
}
