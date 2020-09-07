package com.tokopedia.sellerorder.waitingpaymentorder.di

import com.tokopedia.graphql.coroutines.data.Interactor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.sellerorder.common.SomDispatcherProvider
import com.tokopedia.sellerorder.common.SomProductionDispatcherProvider
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@WaitingPaymentOrderScope
class WaitingPaymentOrderModule {
    @WaitingPaymentOrderScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @WaitingPaymentOrderScope
    @Provides
    fun providesGraphqlRepository(): GraphqlRepository {
        return Interactor.getInstance().graphqlRepository
    }

    @WaitingPaymentOrderScope
    @Provides
    fun provideSomDispatcherProvider(): SomDispatcherProvider = SomProductionDispatcherProvider()
}