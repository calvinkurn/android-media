package com.tokopedia.flight.filter.di

import com.tokopedia.flight.search.data.FlightSearchGQLQuery
import com.tokopedia.flight.search.data.cloud.FlightSearchDataCloudSource
import dagger.Module
import dagger.Provides
import javax.inject.Named

/**
 * @author by furqan on 17/02/2020
 */
@Module
class FlightFilterModule {
    @FlightFilterScope
    @Provides
    @Named(FlightSearchDataCloudSource.NAMED_FLIGHT_SEARCH_SINGLE_QUERY)
    fun provideFlightSearchSingleQuery(): String = FlightSearchGQLQuery.SEARCH_SINGLE

    @Provides
    @FlightFilterScope
    @Named(FlightSearchDataCloudSource.NAMED_FLIGHT_SEARCH_COMBINE_QUERY)
    fun provideFlightSearchCombineQuery() = FlightSearchGQLQuery.SEARCH_COMBINE
}