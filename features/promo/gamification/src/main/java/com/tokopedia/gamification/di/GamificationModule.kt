package com.tokopedia.gamification.di

import android.content.Context
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * Created by zulfikarrahman on 10/20/17.
 */
@GamificationScope
@Module(includes = [ActivityContextModule::class])
class GamificationModule {
    @GamificationScope
    @Provides
    fun provideUserSessionInterface(context: Context): UserSessionInterface {
        return UserSession(context)
    }
}