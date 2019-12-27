package com.tokopedia.saldodetails.di

import android.content.Context

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.user.session.UserSession

import dagger.Module
import dagger.Provides

@Module
class SaldoDetailsModule {

    @SaldoDetailsScope
    @Provides
    internal fun providesUserSession(@ApplicationContext context: Context): UserSession {
        return UserSession(context)
    }

}
