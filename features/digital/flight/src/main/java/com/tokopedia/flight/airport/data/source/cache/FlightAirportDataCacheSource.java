package com.tokopedia.flight.airport.data.source.cache;

import android.content.Context;

import com.tokopedia.abstraction.base.data.source.cache.DataCacheSource;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by hendry on 7/14/2017.
 */

public class FlightAirportDataCacheSource extends DataCacheSource {

    private static final String PREF_KEY_NAME = "PREF_KEY_AIRPORT_LIST";
    private static final long ONE_WEEK = TimeUnit.DAYS.toSeconds(7);

    @Inject
    public FlightAirportDataCacheSource(@ApplicationContext Context context) {
        super(context);
    }

    @Override
    protected String getPrefKeyName() {
        return PREF_KEY_NAME;
    }

    @Override
    protected long getExpiredTimeInSec() {
        return ONE_WEEK;
    }

    @Override
    public Observable<Boolean> isExpired() {
        return Observable.just(false);
    }
}
