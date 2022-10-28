package com.tokopedia.home_account.privacy_account.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.home_account.analytics.HomeAccountAnalytics
import com.tokopedia.home_account.privacy_account.di.LinkAccountContext
import com.tokopedia.home_account.privacy_account.tracker.LinkAccountTracker
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module class LinkAccountModule(private val context: Context) {

    @Provides
    @LinkAccountContext
    fun provideContext(): Context {
        return context
    }

    @Provides
    @ActivityScope
    fun provideUserSession(
            @LinkAccountContext context: Context
    ): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    @ActivityScope
    fun provideHomeAccountAnalytics(userSession: UserSessionInterface): HomeAccountAnalytics {
        return HomeAccountAnalytics(userSession)
    }

    @Provides
    @ActivityScope
    fun provideLinkAccountTracker(): LinkAccountTracker {
        return LinkAccountTracker()
    }

    @Provides
    @ActivityScope
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}