package com.tokopedia.notifcenter.di.module

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.notifcenter.util.cache.NotifCenterCacheManager
import com.tokopedia.notifcenter.util.cache.NotifCenterCacheManagerImpl
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
object NotificationModule {

    @Provides
    fun context(@ApplicationContext context: Context): Context {
        return context
    }

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
}
