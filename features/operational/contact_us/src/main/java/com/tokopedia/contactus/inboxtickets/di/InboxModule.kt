package com.tokopedia.contactus.inboxtickets.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.contactus.inboxtickets.domain.usecase.GetFileUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class InboxModule(private val context: Context) {

    @Provides
    @ActivityScope
    fun provideContext(): Context {
        return context
    }

    @Provides
    fun provideUserSession(): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    fun provideUploadImageUseCase():
        GetFileUseCase{
        return GetFileUseCase(context)
    }
}
