package com.tokopedia.flight.passenger.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.common.travel.database.CommonTravelRoomDb
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * @author by furqan on 12/03/18.
 */

@Module
class FlightPassengerModule {

    @FlightPassengerScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @FlightPassengerScope
    @Provides
    fun provideMultiRequestGraphqlUseCase(graphqlRepository: GraphqlRepository):
            MultiRequestGraphqlUseCase = MultiRequestGraphqlUseCase(graphqlRepository)

    @FlightPassengerScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @FlightPassengerScope
    @Provides
    fun provideTravelPassengerRoomDb(@ApplicationContext context: Context)
            : CommonTravelRoomDb = CommonTravelRoomDb.getDatabase(context)

}
