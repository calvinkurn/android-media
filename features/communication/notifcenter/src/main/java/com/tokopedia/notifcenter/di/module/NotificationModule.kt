package com.tokopedia.notifcenter.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.notifcenter.common.network.NotifcenterCacheManager
import com.tokopedia.notifcenter.common.network.NotifcenterCacheManagerImpl
import com.tokopedia.recommendation_widget_common.di.RecommendationModule
import com.tokopedia.topads.sdk.di.TopAdsWishlistModule
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.repository.TopAdsRepository
import com.tokopedia.topads.sdk.utils.TopAdsIrisSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module(includes = [RecommendationModule::class, TopAdsWishlistModule::class])
object NotificationModule {

    @Provides
    @ActivityScope
    fun provideTopAdsImageViewUseCase(
        userSession: UserSessionInterface,
        topAdsIrisSession: TopAdsIrisSession
    ): TopAdsImageViewUseCase {
        return TopAdsImageViewUseCase(userSession.userId, TopAdsRepository(),topAdsIrisSession.getSessionId())
    }

    @ActivityScope
    @Provides
    internal fun provideNotificationCacheManager(
            @ApplicationContext context: Context
    ): NotifcenterCacheManager {
        val notifCachePref = context.getSharedPreferences(
                "prefs_notifcenter",
                Context.MODE_PRIVATE
        )
        return NotifcenterCacheManagerImpl(notifCachePref)
    }

}

