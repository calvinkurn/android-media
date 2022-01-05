package com.tokopedia.stickylogin.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.stickylogin.di.StickyLoginContext
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


@Module
class StickyLoginModule(private val context: Context) {

    @ActivityScope
    @StickyLoginContext
    @Provides
    fun provideInactivePhoneContext(): Context = context

    @ActivityScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @ActivityScope
    @Provides
    fun provideUserSession(): UserSessionInterface = UserSession(context)
}