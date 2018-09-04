package com.tokopedia.flight.orderlist.di;

import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.flight.orderlist.domain.FlightGetOrderUseCase;
import com.tokopedia.flight.orderlist.domain.FlightGetOrdersUseCase;
import com.tokopedia.flight.orderlist.domain.FlightSendEmailUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * @author by alvarisi on 12/6/17.
 */
@Module
public class FlightOrderModule {

    public FlightOrderModule() {
    }

    @Provides
    FlightGetOrdersUseCase provideFlightGetOrdersUseCase(FlightRepository flightRepository) {
        return new FlightGetOrdersUseCase(flightRepository);
    }

    @Provides
    FlightGetOrderUseCase provideFlightGetOrderUseCase(FlightRepository flightRepository) {
        return new FlightGetOrderUseCase(flightRepository);
    }

    @Provides
    FlightSendEmailUseCase provideFlightSendEmailUseCase(FlightRepository flightRepository) {
        return new FlightSendEmailUseCase(flightRepository);
    }
}
