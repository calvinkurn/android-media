package com.tokopedia.feedplus.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.feedplus.presentation.onboarding.OnBoardingPreferences
import com.tokopedia.feedplus.presentation.onboarding.OnBoardingPreferencesImpl
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * Created By : Muhammad Furqan on 09/02/23
 */
@Module
class FeedMainModule {
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    fun provideOnBoardingPreferences(@ApplicationContext context: Context): OnBoardingPreferences {
        return OnBoardingPreferencesImpl(context)
    }

    @Provides
    fun provideTrackingQueue(@ApplicationContext context: Context) =
        TrackingQueue(context)
}
