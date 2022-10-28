package com.tokopedia.commissionbreakdown.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.commissionbreakdown.di.scope.CommissionBreakdownScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides


@Module
class CommissionBreakdownModule {

    @CommissionBreakdownScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @CommissionBreakdownScope
    @Provides
    fun provideRemoteConfig(@ApplicationContext context: Context): FirebaseRemoteConfigImpl =
        FirebaseRemoteConfigImpl(context)

    @CommissionBreakdownScope
    @Provides
    fun providesContexts(@ApplicationContext context: Context): Context {
        return context
    }
}