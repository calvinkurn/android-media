package com.tokopedia.core.database.manager;

import android.util.Log;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.tokopedia.core.database.model.LatLngModelDB;
import com.tokopedia.core.database.model.LatLngModelDB_Table;

/**
 * Created by hangnadi on 2/2/16.
 */
public class LatLngCacheManager {

    private static final String TAG = LatLngCacheManager.class.getSimpleName();
    private double latitude;
    private double longitude;

    public LatLngCacheManager() {
    }

    public LatLngCacheManager setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public LatLngCacheManager setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public static int CountTable() {
        return (int) new Select().from(LatLngModelDB.class).count();
    }

    public static void dropCache() {
        Log.d(TAG, "Drop Cache LatLng");
        new Delete().from(LatLngModelDB.class).execute();
    }

    public LatLngCacheManager saveCache() {
        Log.d(TAG, "Store Cache LatLng");
        LatLngModelDB cache = new LatLngModelDB();
        cache.locationID = String.valueOf(CountTable() + 1);
        cache.latitude = this.latitude;
        cache.longitude = this.longitude;
        cache.save();
        return this;
    }

    public LatLngCacheManager getCache(String latLngID) {
        Log.d(TAG, "Get Cache LatLngID: " + latLngID);

        LatLngModelDB modelDB = new Select().from(LatLngModelDB.class)
                .where(LatLngModelDB_Table.locationID.is(latLngID))
                .querySingle();

        if (modelDB == null) {
            throw new RuntimeException("Not Found");
        } else {
            latitude = modelDB.latitude;
            longitude = modelDB.longitude;
            return this;
        }
    }
}