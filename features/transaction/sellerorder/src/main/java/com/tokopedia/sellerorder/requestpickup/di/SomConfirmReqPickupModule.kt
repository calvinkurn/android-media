package com.tokopedia.sellerorder.requestpickup.di

import com.tokopedia.graphql.coroutines.data.Interactor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.sellerorder.common.SomDispatcherProvider
import com.tokopedia.sellerorder.common.SomProductionDispatcherProvider
import com.tokopedia.sellerorder.requestpickup.data.model.SomConfirmReqPickup
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Created by fwidjaja on 12/05/20.
 */
@Module
@SomConfirmReqPickupScope
class SomConfirmReqPickupModule {
    @SomConfirmReqPickupScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @SomConfirmReqPickupScope
    @Provides
    fun providesGraphqlRepository(): GraphqlRepository {
        return Interactor.getInstance().graphqlRepository
    }

    @SomConfirmReqPickupScope
    @Provides
    fun provideSomDispatcherProvider(): SomDispatcherProvider = SomProductionDispatcherProvider()

    @SomConfirmReqPickupScope
    @Provides
    fun provideSomConfirmReqPickupUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<SomConfirmReqPickup.Data> = GraphqlUseCase(graphqlRepository)
}