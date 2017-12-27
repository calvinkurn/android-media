package com.tokopedia.core.analytics.fingerprint;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import com.google.gson.reflect.TypeToken;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.geolocation.presenter.GoogleMapPresenter;
import com.tokopedia.core.var.TkpdCache;

/**
 * Created by Herdi_WORK on 07.07.17.
 */

public class LocationCache {

    public LocationCache (){

    }

    public static void saveLocation(Context context, Location location){
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, TkpdCache.Key.KEY_LOCATION);
        localCacheHandler.putString(TkpdCache.Key.KEY_LOCATION_LAT, String.valueOf(location.getLatitude()));
        localCacheHandler.putString(TkpdCache.Key.KEY_LOCATION_LONG, String.valueOf(location.getLongitude()));
        localCacheHandler.applyEditor();
    }

    public static String getLatitudeCache() {
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(MainApplication.getAppContext(), TkpdCache.Key.KEY_LOCATION);
        return localCacheHandler.getString(TkpdCache.Key.KEY_LOCATION_LAT, String.valueOf(GoogleMapPresenter.DEFAULT_LATITUDE));
    }

    public static String getLongitudeCache() {
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(MainApplication.getAppContext(), TkpdCache.Key.KEY_LOCATION);
        return localCacheHandler.getString(TkpdCache.Key.KEY_LOCATION_LONG, String.valueOf(GoogleMapPresenter.DEFAULT_LONGITUDE));
    }


}
