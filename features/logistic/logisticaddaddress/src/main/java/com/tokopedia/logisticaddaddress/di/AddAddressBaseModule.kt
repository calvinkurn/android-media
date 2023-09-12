package com.tokopedia.logisticaddaddress.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
object AddAddressBaseModule {

    @Provides
    @ActivityScope
    fun providesUserSession(@ApplicationContext context: Context): UserSessionInterface =
        UserSession(context)
}
