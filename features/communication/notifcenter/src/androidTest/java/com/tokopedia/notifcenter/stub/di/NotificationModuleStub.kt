package com.tokopedia.notifcenter.stub.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.notifcenter.stub.common.NotificationCacheManagerStub
import com.tokopedia.notifcenter.stub.common.UserSessionStub
import com.tokopedia.notifcenter.util.cache.NotifCenterCacheManager
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
object NotificationModuleStub {

    @Provides
    fun context(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @ActivityScope
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSessionStub(context)
    }

    @Provides
    @ActivityScope
    internal fun provideNotifCenterCacheManager(): NotifCenterCacheManager {
        return NotificationCacheManagerStub
    }
}
