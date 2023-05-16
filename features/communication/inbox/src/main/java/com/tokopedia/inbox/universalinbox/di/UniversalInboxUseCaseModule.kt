package com.tokopedia.inbox.universalinbox.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.repository.TopAdsRepository
import com.tokopedia.topads.sdk.utils.TopAdsIrisSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
object UniversalInboxUseCaseModule {

    @ActivityScope
    @Provides
    fun provideGetRecommendationUseCase(
        @ApplicationContext context: Context,
        @ApplicationContext graphqlRepository: GraphqlRepository
    ): GetRecommendationUseCase {
        return GetRecommendationUseCase(context, graphqlRepository)
    }

    @ActivityScope
    @Provides
    fun provideTopAdsImageViewUseCase(
        userSession: UserSessionInterface,
        topAdsIrisSession: TopAdsIrisSession
    ): TopAdsImageViewUseCase {
        return TopAdsImageViewUseCase(
            userSession.userId,
            TopAdsRepository(),
            topAdsIrisSession.getSessionId()
        )
    }
}
