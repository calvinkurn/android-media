package com.tokopedia.recommendation_widget_common.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.recommendation_widget_common.byteio.RecommendationByteIoUseCase
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationFilterChips
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetSingleRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetViewToViewRecommendationUseCase
import dagger.Module
import dagger.Provides

/**
 * Created by Lukas on 2/24/21.
 */
@Module
class RecommendationCoroutineModule {
    @Provides
    fun provideRecommendationByteIoUseCase(): RecommendationByteIoUseCase = RecommendationByteIoUseCase()

    @Provides
    fun provideGetCoroutineRecommendationUseCase(
        @ApplicationContext context: Context,
        coroutineGqlRepository: GraphqlRepository,
    ): GetRecommendationUseCase = GetRecommendationUseCase(context, coroutineGqlRepository)

    @Provides
    fun provideGetCoroutineSingleRecommendationUseCase(
        @ApplicationContext context: Context,
        coroutineGqlRepository: GraphqlRepository
    ): GetSingleRecommendationUseCase =
        GetSingleRecommendationUseCase(context, coroutineGqlRepository)

    @Provides
    fun provideGetViewToViewRecommendationUseCase(
        @ApplicationContext context: Context,
        coroutineGqlRepository: GraphqlRepository,
    ): GetViewToViewRecommendationUseCase = GetViewToViewRecommendationUseCase(context, coroutineGqlRepository)

    @Provides
    fun provideGetRecommendationFilterChips(graphqlRepository: GraphqlRepository, @ApplicationContext context: Context): GetRecommendationFilterChips {
        val graphql = GraphqlUseCase<RecommendationFilterChipsEntity>(graphqlRepository)
        return GetRecommendationFilterChips(graphql, context)
    }
}
