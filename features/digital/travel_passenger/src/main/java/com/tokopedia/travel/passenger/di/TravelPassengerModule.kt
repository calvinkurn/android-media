package com.tokopedia.travel.passenger.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.travel.passenger.data.database.TravelPassengerDb
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * @author by furqan on 02/01/2020
 */
@Module
class TravelPassengerModule {

    @Provides
    @TravelPassengerScope
    fun provideTravelPassengerDb(@ApplicationContext context: Context): TravelPassengerDb =
            TravelPassengerDb.getDatabase(context)

    @Provides
    @TravelPassengerScope
    fun provideTravelPassengerDao(travelPassengerDb: TravelPassengerDb) =
            travelPassengerDb.travelPassengerDao()

    @Provides
    @TravelPassengerScope
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @Provides
    @TravelPassengerScope
    fun provideMultiRequestGraphqlUseCase(graphqlRepository: GraphqlRepository): MultiRequestGraphqlUseCase {
        return MultiRequestGraphqlUseCase(graphqlRepository)
    }

    @Provides
    @TravelPassengerScope
    fun provideMainDispatcher(): CoroutineDispatcher {
        return Dispatchers.Main
    }

}