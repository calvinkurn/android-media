package com.tokopedia.user.session.di

import android.content.Context
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.user.session.datastore.DataStorePreference
import dagger.Module
import dagger.Provides

@Module
open class SessionModule {

    @Provides
    open fun provideUserSession(context: Context): UserSessionInterface = UserSession(context)

    @Provides
    open fun provideAbPlatform(context: Context): DataStorePreference {
        return DataStorePreference(context)
    }

}