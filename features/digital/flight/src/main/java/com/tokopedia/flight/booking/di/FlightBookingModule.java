package com.tokopedia.flight.booking.di;

import com.tokopedia.flight.booking.domain.FlightAddToCartUseCase;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.flight.search.domain.usecase.FlightSearchJourneyByIdUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by alvarisi on 11/8/17.
 */
@Module
public class FlightBookingModule {

    @Provides
    FlightAddToCartUseCase flightAddToCartUseCase(FlightRepository flightRepository,
                                                  FlightSearchJourneyByIdUseCase flightBookingGetSingleResultUseCase) {
        return new FlightAddToCartUseCase(flightRepository, flightBookingGetSingleResultUseCase);
    }
}
