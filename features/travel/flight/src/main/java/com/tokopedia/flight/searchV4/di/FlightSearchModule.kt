package com.tokopedia.flight.searchV4.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.flight.R
import com.tokopedia.flight.searchV4.data.cloud.FlightSearchDataCloudSource
import com.tokopedia.flight.searchV4.presentation.util.FlightSearchCache
import dagger.Module
import dagger.Provides
import javax.inject.Named

/**
 * @author by furqan on 06/04/2020
 */
@FlightSearchScope
@Module
class FlightSearchModule {

    @Provides
    @FlightSearchScope
    @Named(FlightSearchDataCloudSource.NAMED_FLIGHT_SEARCH_COMBINE_QUERY)
    fun provideFlightSearchCombineQuery(@ApplicationContext context: Context) =
            GraphqlHelper.loadRawString(context.resources, R.raw.flight_search_combine)

    @Provides
    @FlightSearchScope
    fun provideFlightSearchCache(@ApplicationContext context: Context) = FlightSearchCache(context)

    @FlightSearchScope
    @Provides
    @Named(FlightSearchDataCloudSource.NAMED_FLIGHT_SEARCH_SINGLE_QUERY)
    fun provideFlightSearchSingleQuery(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.flight_search_single)
    }

}