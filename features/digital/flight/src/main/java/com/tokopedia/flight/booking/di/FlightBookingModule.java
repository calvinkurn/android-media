package com.tokopedia.flight.booking.di;

import com.tokopedia.flight.booking.domain.FlightAddToCartUseCase;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.flight.review.domain.FlightCancelVoucherUseCase;
import com.tokopedia.flight.search.domain.usecase.FlightSearchJourneyByIdUseCase;
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor;
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase;
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository;

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
    FlightCancelVoucherUseCase flightCancelVoucherUseCase(FlightRepository flightRepository) {
        return new FlightCancelVoucherUseCase(flightRepository);
    }

    @FlightBookingScope
    @Provides
    GraphqlRepository provideGraphqlRepository() { return GraphqlInteractor.getInstance().getGraphqlRepository(); }

    @FlightBookingScope
    @Provides
    MultiRequestGraphqlUseCase provideMultiRequestGraphqlUseCase(GraphqlRepository graphqlRepository) {
        return new MultiRequestGraphqlUseCase(graphqlRepository);
    }

}
