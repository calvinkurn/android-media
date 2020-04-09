package com.tokopedia.flight.searchV4.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.flight.R
import com.tokopedia.flight.searchV4.data.cloud.FlightSearchDataCloudSource
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
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
    @Named(FlightSearchDataCloudSource.NAMED_FLIGHT_SEARCH_SINGLE_QUERY)
    fun provideFlightSearchSingleQuery(@ApplicationContext context: Context)  =
            GraphqlHelper.loadRawString(context.resources, R.raw.flight_search_single)

    @Provides
    @FlightSearchScope
    fun provideGraphqlCoroutineRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

}