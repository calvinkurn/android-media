package com.tokopedia.seller.action.common.di

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import dagger.Module
import dagger.Provides

@SellerActionScope
@Module
class SellerActionModule {

    @SellerActionScope
    @Provides
    fun provideGraphqlRepository() = GraphqlInteractor.getInstance().graphqlRepository

}