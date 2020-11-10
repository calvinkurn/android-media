package com.tokopedia.common.travel.di

import com.tokopedia.common.travel.utils.TravelDispatcherProvider
import com.tokopedia.common.travel.utils.TravelProductionDispatcherProvider
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor.Companion.getInstance
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers.Main

/**
 * Created by nabillasabbaha on 13/08/18.
 */
@CommonTravelScope
@Module
class CommonTravelModule {
    @CommonTravelScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository = getInstance().graphqlRepository

    @CommonTravelScope
    @Provides
    fun provideMultiRequestGraphqlUseCase(graphqlRepository: GraphqlRepository?): MultiRequestGraphqlUseCase = MultiRequestGraphqlUseCase(graphqlRepository!!)

    @CommonTravelScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Main

    @CommonTravelScope
    @Provides
    fun provideTravelDispatcher(): TravelDispatcherProvider = TravelProductionDispatcherProvider()
}