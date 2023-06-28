package com.tokopedia.notifcenter.di.module

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.notifcenter.util.cache.NotifCenterCacheManager
import com.tokopedia.notifcenter.util.cache.NotifCenterCacheManagerImpl
import com.tokopedia.recommendation_widget_common.di.RecommendationModule
import com.tokopedia.topads.sdk.di.TopAdsWishlistModule
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.repository.TopAdsRepository
import com.tokopedia.topads.sdk.utils.TopAdsIrisSession
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module(includes = [RecommendationModule::class, TopAdsWishlistModule::class])
object NotificationModule {

    @Provides
    @ActivityScope
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    @ActivityScope
    internal fun provideNotifCenterSharedPref(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences(
            NotifCenterCacheManagerImpl.PREF_NOTIF_CENTER,
            Context.MODE_PRIVATE
        )
    }

    @Provides
    @ActivityScope
    internal fun provideNotifCenterCacheManager(
        sharedPreferences: SharedPreferences
    ): NotifCenterCacheManager {
        return NotifCenterCacheManagerImpl(sharedPreferences)
    }

    @Provides
    @ActivityScope
    fun provideTopAdsImageViewUseCase(
        userSession: UserSessionInterface,
        topAdsIrisSession: TopAdsIrisSession
    ): TopAdsImageViewUseCase {
        return TopAdsImageViewUseCase(userSession.userId, TopAdsRepository(), topAdsIrisSession.getSessionId())
    }

    @Provides
    fun provideGraphQlRepository() = GraphqlInteractor.getInstance().graphqlRepository
}
