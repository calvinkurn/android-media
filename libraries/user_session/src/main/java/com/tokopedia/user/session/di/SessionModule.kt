package com.tokopedia.user.session.di

import android.content.Context
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class SessionModule {

    @Provides
    fun provideUserSession(context: Context): UserSessionInterface = UserSession(context)

}