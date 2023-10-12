package com.tokopedia.recommendation_widget_common.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationUseCaseRequest
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * Created by Lukas on 2/24/21.
 */
@Module
class RecommendationModule {

    @Provides
    fun provideGetRecommendationUseCase(
        @ApplicationContext context: Context,
        graphqlUseCase: com.tokopedia.graphql.domain.GraphqlUseCase,
        userSession: UserSessionInterface
    ): com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase {
        return com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase(
            context,
            GetRecommendationUseCaseRequest.widgetListQuery,
            graphqlUseCase,
            userSession
        )
    }

    @Provides
    fun provideGetSingleRecommendationUseCase(
        @ApplicationContext context: Context,
        graphqlUseCase: com.tokopedia.graphql.domain.GraphqlUseCase,
        userSession: UserSessionInterface
    ): com.tokopedia.recommendation_widget_common.domain.GetSingleRecommendationUseCase {
        val query = GetRecommendationUseCaseRequest.singleQuery
        return com.tokopedia.recommendation_widget_common.domain.GetSingleRecommendationUseCase(context, query, graphqlUseCase, userSession)
    }
}
