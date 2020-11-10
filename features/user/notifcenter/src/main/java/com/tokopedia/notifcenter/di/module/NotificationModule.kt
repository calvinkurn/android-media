package com.tokopedia.notifcenter.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.notifcenter.di.scope.NotificationScope
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.repository.TopAdsRepository
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class NotificationModule {

    @Provides
    @NotificationScope
    fun provideTopAdsImageViewUseCase(userSession: UserSessionInterface): TopAdsImageViewUseCase {
        return TopAdsImageViewUseCase(userSession.userId, TopAdsRepository())
    }

    @Provides
    @NotificationScope
    fun provideGetRecomendationUseCase(@Named("recommendationQuery") recomQuery: String,
                                       graphqlUseCase: GraphqlUseCase,
                                       userSession: UserSessionInterface): GetRecommendationUseCase {
        return GetRecommendationUseCase(recomQuery, graphqlUseCase, userSession)
    }

    @Provides
    @Named("recommendationQuery")
    fun provideRecommendationRawQuery(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(
                context.resources,
                com.tokopedia.recommendation_widget_common.R.raw.query_recommendation_widget
        )
    }
}