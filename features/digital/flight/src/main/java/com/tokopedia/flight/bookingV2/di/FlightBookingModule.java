package com.tokopedia.flight.bookingV2.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.flight.bookingV2.domain.FlightAddToCartUseCase;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.flight.search.domain.FlightSearchJourneyByIdUseCase;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.promocheckout.common.domain.flight.FlightCancelVoucherUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by alvarisi on 11/8/17.
 */
@Module
public class FlightBookingModule {

    @FlightBookingScope
    @Provides
    FlightAddToCartUseCase flightAddToCartUseCase(FlightRepository flightRepository,
                                                  FlightSearchJourneyByIdUseCase flightBookingGetSingleResultUseCase) {
        return new FlightAddToCartUseCase(flightRepository, flightBookingGetSingleResultUseCase);
    }

    @FlightBookingScope
    @Provides
    FlightCancelVoucherUseCase flightCancelVoucherUseCase(@ApplicationContext Context context,
                                                          GraphqlUseCase graphqlUseCase) {
        return new FlightCancelVoucherUseCase(context, graphqlUseCase);
    }

}
