package com.tokopedia.flight.airline.data.cache;

import android.content.Context;
import android.content.SharedPreferences;

import com.tokopedia.abstraction.base.data.source.cache.DataCacheSource;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * Created by hendry on 7/14/2017.
 */

public class FlightAirlineDataCacheSource extends DataCacheSource {

    private static final String PREF_KEY_NAME = "PREF_KEY_AIRLINE_LIST";
    private static final String FLIGHT_VERSION_AIRLINE = "FLIGHT_VERSION_AIRLINE";
    private SharedPreferences sharedPreferences;
    private static final long ONE_MONTH = TimeUnit.DAYS.toSeconds(30);

    @Inject
    public FlightAirlineDataCacheSource(@ApplicationContext Context context) {
        super(context);
        sharedPreferences = context.getSharedPreferences(FLIGHT_VERSION_AIRLINE, Context.MODE_PRIVATE);
    }

    @Override
    protected String getPrefKeyName() {
        return PREF_KEY_NAME;
    }

    @Override
    protected long getExpiredTimeInSec() {
        return ONE_MONTH;
    }

    public long getVersion() {
        return sharedPreferences.getLong(FLIGHT_VERSION_AIRLINE, 0);
    }

    public void updateVersion(long versionAirline) {
        sharedPreferences.edit().putLong(FLIGHT_VERSION_AIRLINE, versionAirline).apply();
    }
}
