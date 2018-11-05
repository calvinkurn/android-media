package com.tokopedia.logisticgeolocation.util;

import android.content.Context;
import android.location.Location;

import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.abstraction.constant.TkpdCache;

/**
 * Created by Fajar Ulin Nuha on 30/10/18.
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

}
