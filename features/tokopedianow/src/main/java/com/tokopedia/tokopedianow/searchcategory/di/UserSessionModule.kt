package com.tokopedia.tokopedianow.searchcategory.di

import android.content.Context
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class UserSessionModule {

    @Provides
    fun provideUserSession(context: Context): UserSessionInterface {
        return UserSession(context)
    }
}