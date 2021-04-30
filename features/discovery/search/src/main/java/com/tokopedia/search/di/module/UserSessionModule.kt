package com.tokopedia.search.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.user.session.UserSession
import dagger.Provides
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module

@Module
class UserSessionModule {
    @SearchScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context?): UserSessionInterface {
        return UserSession(context)
    }
}