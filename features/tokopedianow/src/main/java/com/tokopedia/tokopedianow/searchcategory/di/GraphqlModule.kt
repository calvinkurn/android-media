package com.tokopedia.tokopedianow.searchcategory.di

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import dagger.Module
import dagger.Provides

@Module
class GraphqlModule {

    @Provides
    fun provideGraphqlRepository() = GraphqlInteractor.getInstance().graphqlRepository
}