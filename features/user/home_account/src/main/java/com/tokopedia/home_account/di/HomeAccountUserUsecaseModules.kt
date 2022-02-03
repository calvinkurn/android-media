package com.tokopedia.home_account.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.home_account.account_settings.domain.UserProfileSafeModeUseCase
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.repository.TopAdsRepository
import com.tokopedia.topads.sdk.utils.TopAdsIrisSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class HomeAccountUserUsecaseModules {

    @Provides
    fun provideTopAdsImageViewUseCase(userSession: UserSessionInterface,topAdsIrisSession: TopAdsIrisSession): TopAdsImageViewUseCase {
        return TopAdsImageViewUseCase(userSession.userId, TopAdsRepository(),topAdsIrisSession.getSessionId())
    }

    @Provides
    fun provideGetCoroutineRecommendationUseCase(
        @ApplicationContext context: Context,
        @ApplicationContext coroutineGqlRepository: GraphqlRepository
    ): GetRecommendationUseCase = GetRecommendationUseCase(context, coroutineGqlRepository)

    @Provides
    fun provideGetSafeModeStatusUseCase(
        @ApplicationContext context: Context,
        @ApplicationContext coroutineGqlRepository: GraphqlRepository
    ): UserProfileSafeModeUseCase = UserProfileSafeModeUseCase(context, coroutineGqlRepository)
}
