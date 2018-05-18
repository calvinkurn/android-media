package com.tokopedia.travelcalendar.data;

import android.content.Context;

import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.travelcalendar.network.TravelCalendarApi;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 16/05/18.
 */

public class TravelCalendarSourceFactory {

    private TravelCalendarApi travelCalendarApi;
    private CacheManager cacheManager;
    private Context context;

    @Inject
    public TravelCalendarSourceFactory(TravelCalendarApi travelCalendarApi, CacheManager cacheManager,
                                       @ApplicationContext  Context context) {
        this.travelCalendarApi = travelCalendarApi;
        this.cacheManager = cacheManager;
        this.context = context;
    }

    public TravelCalendarDataStore createTravelCalendarDataStore() {
        return new TravelCalendarCloudDataStore(cacheManager, context, travelCalendarApi);
    }

    public TravelCalendarDataStore createLocalTravelCalendarDataStore() {
        return new TravelCalendarLocalDataStore(cacheManager);
    }
}