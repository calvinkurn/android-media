package com.tokopedia.flight.dashboardV2.di

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import dagger.Module
import dagger.Provides

/**
 * @author by furqan on 27/03/2020
 */
@Module
@FlightDashboardV2Scope
class FlightDashboardV2Module {

    @FlightDashboardV2Scope
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository =
            GraphqlInteractor.getInstance().graphqlRepository

}