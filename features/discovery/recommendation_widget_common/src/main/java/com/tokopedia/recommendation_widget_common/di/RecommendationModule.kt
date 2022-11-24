package com.tokopedia.recommendation_widget_common.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.recommendation_widget_common.domain.query.ListProductRecommendationQuery
import com.tokopedia.recommendation_widget_common.domain.query.ListProductRecommendationQueryV2
import com.tokopedia.recommendation_widget_common.domain.query.ProductRecommendationSingleQuery
import com.tokopedia.recommendation_widget_common.domain.query.ProductRecommendationSingleQueryV2
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
                ListProductRecommendationQueryV2()
            } else {
                ListProductRecommendationQuery()
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
                ProductRecommendationSingleQueryV2()
            } else {
                ProductRecommendationSingleQuery()
            }
        return com.tokopedia.recommendation_widget_common.domain.GetSingleRecommendationUseCase(context, query, graphqlUseCase, userSession)
    }
}
