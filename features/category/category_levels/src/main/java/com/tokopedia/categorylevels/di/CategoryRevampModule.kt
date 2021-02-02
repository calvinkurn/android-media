package com.tokopedia.categorylevels.di

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationFilterChips
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import dagger.Module
import dagger.Provides


@Module
class CategoryRevampModule {
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @Provides
    fun provideGetRecommendationUseCase(coroutineGqlRepository: GraphqlRepository): GetRecommendationUseCase = GetRecommendationUseCase(coroutineGqlRepository)

    @Provides
    fun provideGetRecommendationFilterChips(graphqlRepository: GraphqlRepository): GetRecommendationFilterChips{
        val graphql = GraphqlUseCase<RecommendationFilterChipsEntity>(graphqlRepository)
        return GetRecommendationFilterChips(graphql)
    }
}
