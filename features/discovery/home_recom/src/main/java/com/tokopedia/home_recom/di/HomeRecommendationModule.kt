package com.tokopedia.home_recom.di

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import dagger.Module
import dagger.Provides

@HomeRecommendationScope
@Module
class HomeRecommendationModule {

    @Provides
    fun provideGraphQlRepository() = GraphqlInteractor.getInstance().graphqlRepository
}