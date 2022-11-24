package com.tokopedia.recommendation_widget_common.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetSingleRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.query.QueryListProductRecommendationV2
import com.tokopedia.recommendation_widget_common.domain.query.QueryProductRecommendationSingleV2
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationUseCaseRequest
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
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
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        return com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase(
            context,
            if (remoteConfig.getBoolean(RemoteConfigKey.RECOM_USE_QUERY_V2, true)) {
                QueryListProductRecommendationV2.LIST_PRODUCT_RECOMMENDATION_V2_QUERY
            } else {
                GetRecommendationUseCaseRequest.widgetListQuery
            },
            graphqlUseCase,
            userSession
        )
    }

    @Provides
    fun provideGetSingleRecommendationUseCase(
        @ApplicationContext context: Context,
        graphqlUseCase: com.tokopedia.graphql.domain.GraphqlUseCase,
        userSession: UserSessionInterface,
        remoteConfig: RemoteConfig
    ): com.tokopedia.recommendation_widget_common.domain.GetSingleRecommendationUseCase {
        val query =
            if (remoteConfig.getBoolean(RemoteConfigKey.RECOM_USE_QUERY_V2, true)) {
                QueryProductRecommendationSingleV2.PRODUCT_RECOMMENDATION_SINGLE_V2_QUERY
            } else {
                GetRecommendationUseCaseRequest.singleQuery
            }
        return com.tokopedia.recommendation_widget_common.domain.GetSingleRecommendationUseCase(context, query, graphqlUseCase, userSession)
    }
}
