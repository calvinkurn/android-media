package com.tokopedia.flight.orderlist.di

import com.tokopedia.flight.common.domain.FlightRepository
import com.tokopedia.flight.orderlist.domain.FlightGetOrderUseCase
import com.tokopedia.flight.orderlist.domain.FlightGetOrdersUseCase
import com.tokopedia.flight.orderlist.domain.FlightSendEmailUseCase

import dagger.Module
import dagger.Provides

/**
 * @author by alvarisi on 12/6/17.
 */
@Module
class FlightOrderModule {

    @Provides
    internal fun provideFlightGetOrdersUseCase(flightRepository: FlightRepository): FlightGetOrdersUseCase {
        return FlightGetOrdersUseCase(flightRepository)
    }

    @Provides
    internal fun provideFlightGetOrderUseCase(flightRepository: FlightRepository): FlightGetOrderUseCase {
        return FlightGetOrderUseCase(flightRepository)
    }

    @Provides
    internal fun provideFlightSendEmailUseCase(flightRepository: FlightRepository): FlightSendEmailUseCase {
        return FlightSendEmailUseCase(flightRepository)
    }
}
