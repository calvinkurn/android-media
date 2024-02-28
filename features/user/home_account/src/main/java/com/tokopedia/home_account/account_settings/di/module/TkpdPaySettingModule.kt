package com.tokopedia.home_account.account_settings.di.module

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.home_account.account_settings.analytics.AccountAnalytics
import com.tokopedia.navigation_common.model.WalletPref
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class TkpdPaySettingModule {
    @ActivityScope
    @Provides
    fun provideWalletPref(@ApplicationContext context: Context, gson: Gson): WalletPref {
        return WalletPref(context, gson)
    }

    @Provides
    @ActivityScope
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    @ActivityScope
    fun provideAccountAnalytics(
        userSessionInterface: UserSessionInterface
    ): AccountAnalytics {
        return AccountAnalytics(userSessionInterface)
    }
}
