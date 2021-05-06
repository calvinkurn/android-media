package com.tokopedia.recommendation_widget_common.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationFilterChips
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetSingleRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationUseCaseRequest
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * Created by Lukas on 2/24/21.
 */
@Module
class RecommendationCoroutineModule {
    @Provides
    fun provideGetCoroutineRecommendationUseCase(@ApplicationContext context: Context, coroutineGqlRepository: GraphqlRepository): GetRecommendationUseCase = GetRecommendationUseCase(context, coroutineGqlRepository)

    @Provides
    fun provideGetCoroutineSingleRecommendationUseCase(@ApplicationContext context: Context, coroutineGqlRepository: GraphqlRepository): GetSingleRecommendationUseCase = GetSingleRecommendationUseCase(context, coroutineGqlRepository)

    @Provides
    fun provideGetRecommendationFilterChips(graphqlRepository: GraphqlRepository): GetRecommendationFilterChips {
        val graphql = GraphqlUseCase<RecommendationFilterChipsEntity>(graphqlRepository)
        return GetRecommendationFilterChips(graphql)
    }

}