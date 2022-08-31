package com.tokopedia.universal_sharing.di

import android.content.Context
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class UniversalShareBottomSheetModule(private val context: Context) {

    @Provides
    fun provideContext(): Context = context

    @Provides
    fun provideUserSessionInterface(context: Context): UserSessionInterface {
        return UserSession(context)
    }
}