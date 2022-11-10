package com.tokopedia.flight.homepage.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.flight.homepage.data.cache.FlightDashboardCache
import com.tokopedia.travelcalendar.data.TravelCalendarGQLQuery
import dagger.Module
import dagger.Provides
import javax.inject.Named

/**
 * @author by furqan on 27/03/2020
 */
@Module
class FlightHomepageModule {

    @Provides
    fun provideFlightDashboardCache(@ApplicationContext context: Context): FlightDashboardCache =
            FlightDashboardCache(context)

    @Provides
    @Named("travel_calendar_holiday_query")
    fun provideTravelCalendarHolidayQuery(): String =
            TravelCalendarGQLQuery.GET_TRAVEL_CALENDAR_HOLIDAY

}