package com.tokopedia.seller.action.common.di

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.seller.action.common.dispatcher.SellerActionDispatcher
import com.tokopedia.seller.action.common.dispatcher.SellerActionDispatcherProvider
import dagger.Module
import dagger.Provides

@SellerActionScope
@Module
class SellerActionModule {

    @SellerActionScope
    @Provides
    fun provideGraphqlRepository() = GraphqlInteractor.getInstance().graphqlRepository

    @SellerActionScope
    @Provides
    fun provideDispatchers(): SellerActionDispatcherProvider = SellerActionDispatcher

}