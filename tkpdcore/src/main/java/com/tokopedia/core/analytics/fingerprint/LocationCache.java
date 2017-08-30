package com.tokopedia.core.analytics.fingerprint;

import android.location.Location;
import android.location.LocationManager;

import com.google.gson.reflect.TypeToken;
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

    public static void saveLocation(Location location){
        new GlobalCacheManager()
                .setKey(TkpdCache.Key.KEY_LOCATION)
                .setValue(CacheUtil.convertModelToString(location,new TypeToken<Location>(){}.getType()))
                .store();
    }

    public static boolean isLocationCached(){
        return GlobalCacheManager.isAvailable(TkpdCache.Key.KEY_LOCATION);
    }

    public static Location getLocation(){
        Location location = new Location(LocationManager.NETWORK_PROVIDER);
        location.setLatitude(GoogleMapPresenter.DEFAULT_LATITUDE);
        location.setLongitude(GoogleMapPresenter.DEFAULT_LONGITUDE);
        if(isLocationCached())
            return new GlobalCacheManager().getConvertObjData(TkpdCache.Key.KEY_LOCATION, Location.class);
        else
            return location;
    }
}
