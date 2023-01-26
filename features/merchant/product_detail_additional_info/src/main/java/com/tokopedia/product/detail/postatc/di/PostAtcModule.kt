package com.tokopedia.product.detail.postatc.di

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.recommendation_widget_common.di.RecommendationCoroutineModule
import dagger.Module
import dagger.Provides

@Module(includes = [RecommendationCoroutineModule::class])
class PostAtcModule {
    @PostAtcScope
    @Provides
    fun provideGraphqlRepository() = GraphqlInteractor.getInstance().graphqlRepository
}
