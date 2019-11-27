package com.tokopedia.flight.orderlist.di

import com.tokopedia.common.travel.utils.TrackingCrossSellUtil
import com.tokopedia.flight.common.domain.FlightRepository
import com.tokopedia.flight.orderlist.domain.FlightGetOrderUseCase
import com.tokopedia.flight.orderlist.domain.FlightGetOrdersUseCase
import com.tokopedia.flight.orderlist.domain.FlightSendEmailUseCase
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository

import dagger.Module
import dagger.Provides

/**
 * @author by alvarisi on 12/6/17.
 */
@Module
class FlightOrderModule {

    @Provides
    fun provideFlightGetOrdersUseCase(flightRepository: FlightRepository): FlightGetOrdersUseCase {
        return FlightGetOrdersUseCase(flightRepository)
    }

    @Provides
    fun provideFlightGetOrderUseCase(flightRepository: FlightRepository): FlightGetOrderUseCase {
        return FlightGetOrderUseCase(flightRepository)
    }

    @Provides
    fun provideFlightSendEmailUseCase(flightRepository: FlightRepository): FlightSendEmailUseCase {
        return FlightSendEmailUseCase(flightRepository)
    }

    @FlightOrderScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @FlightOrderScope
    @Provides
    fun provideMultiRequestGraphqlUseCase(graphqlRepository: GraphqlRepository): MultiRequestGraphqlUseCase {
        return MultiRequestGraphqlUseCase(graphqlRepository)
    }

    @FlightOrderScope
    @Provides
    fun provideTrackingCrossSellUtil(): TrackingCrossSellUtil = TrackingCrossSellUtil()

}
