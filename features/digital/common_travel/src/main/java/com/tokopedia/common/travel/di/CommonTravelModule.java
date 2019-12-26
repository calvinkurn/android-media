package com.tokopedia.common.travel.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.common.travel.database.CommonTravelRoomDb;
import com.tokopedia.common.travel.database.TravelPassengerDao;
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
    CommonTravelRoomDb provideTravelPassengerRoomDb(@ApplicationContext Context context) {
        return CommonTravelRoomDb.getDatabase(context);
    }

    @Provides
    TravelPassengerDao provideTravelPassengerDao(CommonTravelRoomDb commonTravelRoomDb) {
        return commonTravelRoomDb.travelPassengerDao();
    }

    @Provides
    TravelProvider provideTravelProvider() {
        return new TravelScheduler();
    }

    @Provides
    GraphqlRepository provideGraphqlRepository() { return GraphqlInteractor.getInstance().getGraphqlRepository(); }

    @Provides
    MultiRequestGraphqlUseCase provideMultiRequestGraphqlUseCase(GraphqlRepository graphqlRepository)
    { return new MultiRequestGraphqlUseCase(graphqlRepository); }

    @CommonTravelScope
    @Provides
    CoroutineDispatcher provideMainDispatcher() { return Dispatchers.getMain(); }

}
