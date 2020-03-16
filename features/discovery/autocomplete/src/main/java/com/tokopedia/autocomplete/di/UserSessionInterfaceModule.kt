package com.tokopedia.autocomplete.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@AutoCompleteScope
@Module
class UserSessionInterfaceModule{
    @AutoCompleteScope
    @Provides
    internal fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }
}