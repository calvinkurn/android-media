package com.tokopedia.flight.airport.data.source.cache

import android.content.Context

import com.tokopedia.abstraction.base.data.source.cache.DataCacheSource
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext

import java.util.concurrent.TimeUnit

import javax.inject.Inject

import rx.Observable

/**
 * Created by hendry on 7/14/2017.
 */

class FlightAirportDataCacheSource @Inject
constructor(@ApplicationContext context: Context) : DataCacheSource(context) {

    override fun getPrefKeyName(): String {
        return PREF_KEY_NAME
    }

    override fun getExpiredTimeInSec(): Long {
        return ONE_WEEK
    }

    override fun isExpired(): Observable<Boolean> {
        return Observable.just(false)
    }

    companion object {

        private val PREF_KEY_NAME = "PREF_KEY_AIRPORT_LIST"
        private val ONE_WEEK = TimeUnit.DAYS.toSeconds(7)
    }
}
