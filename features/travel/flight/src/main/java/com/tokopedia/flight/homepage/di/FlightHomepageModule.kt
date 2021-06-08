package com.tokopedia.flight.homepage.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.flight.homepage.data.cache.FlightDashboardCache
import com.tokopedia.flight.search.data.FlightSearchGQLQuery
import com.tokopedia.flight.search.data.cloud.FlightSearchDataCloudSource
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

    @FlightHomepageScope
    @Provides
    @Named(FlightSearchDataCloudSource.NAMED_FLIGHT_SEARCH_SINGLE_QUERY)
    fun provideFlightSearchSingleQuery(): String = FlightSearchGQLQuery.SEARCH_SINGLE

    @Provides
    @FlightHomepageScope
    @Named(FlightSearchDataCloudSource.NAMED_FLIGHT_SEARCH_COMBINE_QUERY)
    fun provideFlightSearchCombineQuery() = FlightSearchGQLQuery.SEARCH_COMBINE

}