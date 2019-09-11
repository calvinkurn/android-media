package com.tokopedia.flight.booking.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.flight.booking.domain.FlightAddToCartUseCase;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.flight.search.domain.usecase.FlightSearchJourneyByIdUseCase;
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor;
import com.tokopedia.promocheckout.common.domain.flight.FlightCancelVoucherUseCase;
import com.tokopedia.graphql.domain.GraphqlUseCase;
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
    FlightCancelVoucherUseCase flightCancelVoucherUseCase(@ApplicationContext Context context,
                                                          GraphqlUseCase graphqlUseCase) {
        return new FlightCancelVoucherUseCase(context, graphqlUseCase);
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
