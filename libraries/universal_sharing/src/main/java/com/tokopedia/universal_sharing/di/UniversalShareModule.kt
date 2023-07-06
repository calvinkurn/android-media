package com.tokopedia.universal_sharing.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class UniversalShareModule {

    @Provides
    open fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }
}
