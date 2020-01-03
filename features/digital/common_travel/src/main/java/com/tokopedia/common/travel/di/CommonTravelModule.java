package com.tokopedia.common.travel.di;

import com.tokopedia.common.travel.domain.provider.TravelProvider;
import com.tokopedia.common.travel.domain.provider.TravelScheduler;
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor;
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase;
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository;

import dagger.Module;
import dagger.Provides;
import kotlinx.coroutines.CoroutineDispatcher;
import kotlinx.coroutines.Dispatchers;

/**
 * Created by nabillasabbaha on 13/08/18.
 */
@Module
public class CommonTravelModule {

    @Provides
    TravelProvider provideTravelProvider() {
        return new TravelScheduler();
    }

    @Provides
    GraphqlRepository provideGraphqlRepository() { return GraphqlInteractor.getInstance().getGraphqlRepository(); }

    @Provides
    MultiRequestGraphqlUseCase provideMultiRequestGraphqlUseCase(GraphqlRepository graphqlRepository)
    { return new MultiRequestGraphqlUseCase(graphqlRepository); }

    @Provides
    CoroutineDispatcher provideMainDispatcher() { return Dispatchers.getMain(); }

}
