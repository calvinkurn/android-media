package com.tokopedia.flight.homepage.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.flight.dashboard.view.fragment.cache.FlightDashboardCache
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import dagger.Module
import dagger.Provides

/**
 * @author by furqan on 27/03/2020
 */
@Module
@FlightHomepageScope
class FlightHomepageModule {

    @FlightHomepageScope
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository =
            GraphqlInteractor.getInstance().graphqlRepository

    @Provides
    fun provideFlightDashboardCache(@ApplicationContext context: Context): FlightDashboardCache =
            FlightDashboardCache(context)

}