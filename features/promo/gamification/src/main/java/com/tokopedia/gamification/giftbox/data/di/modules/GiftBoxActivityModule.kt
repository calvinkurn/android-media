package com.tokopedia.gamification.giftbox.data.di.modules

import android.content.Context
import com.tokopedia.gamification.di.ActivityContextModule
import com.tokopedia.user.session.UserSession
import dagger.Module
import dagger.Provides

@Module(includes = [ActivityContextModule::class])
class GiftBoxActivityModule {

    @Provides
    fun provideUserSession(context: Context): UserSession {
        return UserSession(context)
    }
}