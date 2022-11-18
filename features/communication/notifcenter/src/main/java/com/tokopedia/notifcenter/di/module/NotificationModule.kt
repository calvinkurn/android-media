package com.tokopedia.notifcenter.di.module

import android.content.Context
import com.tokopedia.notifcenter.common.network.NotifcenterCacheManager
import com.tokopedia.notifcenter.common.network.NotifcenterCacheManagerImpl
import com.tokopedia.notifcenter.di.scope.NotificationContext
import com.tokopedia.notifcenter.di.scope.NotificationScope
import com.tokopedia.recommendation_widget_common.di.RecommendationModule
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.topads.sdk.di.TopAdsWishlistModule
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.repository.TopAdsRepository
import com.tokopedia.topads.sdk.utils.TopAdsIrisSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module(includes = [RecommendationModule::class, TopAdsWishlistModule::class])
class NotificationModule {

    @Provides
    @NotificationScope
    fun provideTopAdsImageViewUseCase(userSession: UserSessionInterface,topAdsIrisSession: TopAdsIrisSession): TopAdsImageViewUseCase {
        return TopAdsImageViewUseCase(userSession.userId, TopAdsRepository(),topAdsIrisSession.getSessionId())
    }

    @NotificationScope
    @Provides
    internal fun provideNotificationCacheManager(
            @NotificationContext context: Context
    ): NotifcenterCacheManager {
        val notifCachePref = context.getSharedPreferences(
                "prefs_notifcenter",
                Context.MODE_PRIVATE
        )
        return NotifcenterCacheManagerImpl(notifCachePref)
    }

}

