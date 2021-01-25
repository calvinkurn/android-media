package com.tokopedia.flight.searchV4.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.flight.searchV4.data.FlightSearchGQLQuery
import com.tokopedia.flight.searchV4.data.cloud.FlightSearchDataCloudSource
import com.tokopedia.flight.searchV4.presentation.util.FlightSearchCache
import dagger.Module
import dagger.Provides
import javax.inject.Named

/**
 * @author by furqan on 06/04/2020
 */
@Module
class FlightSearchModule {

    @Provides
    @FlightSearchScope
    @Named(FlightSearchDataCloudSource.NAMED_FLIGHT_SEARCH_COMBINE_QUERY)
    fun provideFlightSearchCombineQuery() = FlightSearchGQLQuery.SEARCH_COMBINE

    @Provides
    @FlightSearchScope
    fun provideFlightSearchCache(@ApplicationContext context: Context) = FlightSearchCache(context)

    @FlightSearchScope
    @Provides
    @Named(FlightSearchDataCloudSource.NAMED_FLIGHT_SEARCH_SINGLE_QUERY)
    fun provideFlightSearchSingleQuery(): String = FlightSearchGQLQuery.SEARCH_SINGLE

}