package com.tokopedia.flight.filter.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.flight.R
import com.tokopedia.flight.searchV4.data.cloud.FlightSearchDataCloudSource
import dagger.Module
import dagger.Provides
import javax.inject.Named

/**
 * @author by furqan on 17/02/2020
 */
@FlightFilterScope
@Module
class FlightFilterModule {
    @FlightFilterScope
    @Provides
    @Named(FlightSearchDataCloudSource.NAMED_FLIGHT_SEARCH_SINGLE_QUERY)
    fun provideFlightSearchSingleQuery(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.flight_search_single)
    }

    @Provides
    @FlightFilterScope
    @Named(FlightSearchDataCloudSource.NAMED_FLIGHT_SEARCH_COMBINE_QUERY)
    fun provideFlightSearchCombineQuery(@ApplicationContext context: Context) =
            GraphqlHelper.loadRawString(context.resources, R.raw.flight_search_combine)
}