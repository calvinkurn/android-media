package com.tokopedia.mitra.fingerprint;

import android.content.Context;
import android.location.Location;

import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.mitra.MitraApplication;

/**
 * Created by Rizky on 31/08/18.
 */
public class LocationCache {

    public LocationCache (){

    }

    public static void saveLocation(Context context, Location location){
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, ConstactsFingerprint.Key.KEY_LOCATION);
        localCacheHandler.putString(ConstactsFingerprint.Key.KEY_LOCATION_LAT, String.valueOf(location.getLatitude()));
        localCacheHandler.putString(ConstactsFingerprint.Key.KEY_LOCATION_LONG, String.valueOf(location.getLongitude()));
        localCacheHandler.applyEditor();
    }

    public static String getLatitudeCache() {
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(MitraApplication.getInstance().getAppContext(), ConstactsFingerprint.Key.KEY_LOCATION);
        double DEFAULT_LATITUDE = -6.175794;
        return localCacheHandler.getString(ConstactsFingerprint.Key.KEY_LOCATION_LAT, String.valueOf(DEFAULT_LATITUDE));
    }

    public static String getLongitudeCache() {
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(MitraApplication.getInstance().getAppContext(), ConstactsFingerprint.Key.KEY_LOCATION);
        double DEFAULT_LONGITUDE = 106.826457;
        return localCacheHandler.getString(ConstactsFingerprint.Key.KEY_LOCATION_LONG, String.valueOf(DEFAULT_LONGITUDE));
    }

}
