package com.tokopedia.flight.homepage.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.flight.R
import com.tokopedia.flight.homepage.data.cache.FlightDashboardCache
import com.tokopedia.flight.searchV4.data.cloud.FlightSearchDataCloudSource
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

    @FlightHomepageScope
    @Provides
    @Named(FlightSearchDataCloudSource.NAMED_FLIGHT_SEARCH_SINGLE_QUERY)
    fun provideFlightSearchSingleQuery(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.flight_search_single)
    }

    @Provides
    @FlightHomepageScope
    @Named(FlightSearchDataCloudSource.NAMED_FLIGHT_SEARCH_COMBINE_QUERY)
    fun provideFlightSearchCombineQuery(@ApplicationContext context: Context) =
            GraphqlHelper.loadRawString(context.resources, R.raw.flight_search_combine)


}