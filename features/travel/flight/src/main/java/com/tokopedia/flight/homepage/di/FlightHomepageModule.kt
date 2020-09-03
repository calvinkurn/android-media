package com.tokopedia.flight.homepage.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.flight.homepage.data.cache.FlightDashboardCache
import dagger.Module
import dagger.Provides
import javax.inject.Named

/**
 * @author by furqan on 27/03/2020
 */
@Module
@FlightHomepageScope
class FlightHomepageModule {

    @Provides
    fun provideFlightDashboardCache(@ApplicationContext context: Context): FlightDashboardCache =
            FlightDashboardCache(context)

    @Provides
    @Named("travel_calendar_holiday_query")
    fun provideTravelCalendarHolidayQuery(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, com.tokopedia.travelcalendar.R.raw.query_get_travel_calendar_holiday)
    }


}