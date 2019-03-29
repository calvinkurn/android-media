package com.tokopedia.core.analytics.fingerprint;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.tokopedia.core.deprecated.LocalCacheHandler;

/**
 * Created by Herdi_WORK on 07.07.17.
 */

public class LocationCache {

    double DEFAULT_LATITUDE = -6.175794;
    double DEFAULT_LONGITUDE = 106.826457;
    public static final String KEY_LOCATION = "KEY_FP_LOCATION";
    public static final String KEY_LOCATION_LAT = "KEY_FP_LOCATION_LAT";
    public static final String KEY_LOCATION_LONG = "KEY_FP_LOCATION_LONG";


    public static final long DEFAULT_UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            DEFAULT_UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    private Context context;

    public LocationCache (Context context){
        this.context = context;
    }

    public void saveLocation(Context context, Location location){
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, KEY_LOCATION);
        localCacheHandler.putString(KEY_LOCATION_LAT, String.valueOf(location.getLatitude()));
        localCacheHandler.putString(KEY_LOCATION_LONG, String.valueOf(location.getLongitude()));
        localCacheHandler.applyEditor();
    }

    public String getLatitudeCache() {
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, KEY_LOCATION);
        return localCacheHandler.getString(KEY_LOCATION_LAT, String.valueOf(DEFAULT_LATITUDE));
    }

    public String getLongitudeCache() {
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, KEY_LOCATION);
        return localCacheHandler.getString(KEY_LOCATION_LONG, String.valueOf(DEFAULT_LONGITUDE));
    }


}
