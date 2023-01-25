package com.tokopedia.product.detail.postatc.di

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import dagger.Module
import dagger.Provides

@Module
class PostAtcModule {
    @PostAtcScope
    @Provides
    fun provideGraphqlRepository() = GraphqlInteractor.getInstance().graphqlRepository
}
