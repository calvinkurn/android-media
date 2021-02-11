package com.tokopedia.shop_showcase.di

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.data.Interactor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import dagger.Module
import dagger.Provides

@Module
class ShopShowcaseModule {

    @ShopShowcaseScope
    @Provides
    fun provideMultiRequestGraphqluseCase() = GraphqlInteractor.getInstance().multiRequestGraphqlUseCase

    @ShopShowcaseScope
    @Provides
    fun provideGraphqlUseCase(): GraphqlUseCase = GraphqlUseCase()

    @ShopShowcaseScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository = Interactor.getInstance().graphqlRepository

    @ShopShowcaseScope
    @Provides
    fun provideMainDispatcherProvider(): CoroutineDispatchers = CoroutineDispatchersProvider

}