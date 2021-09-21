package com.tokopedia.loginregister.shopcreation.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.loginregister.common.analytics.RegisterAnalytics
import com.tokopedia.loginregister.common.analytics.ShopCreationAnalytics
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * Created by Ade Fulki on 2019-12-09.
 * ade.hadian@tokopedia.com
 */

@Module
class ShopCreationModule {

    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @ShopCreationScope
    @Provides
    fun provideShopCreationAnalytics(): ShopCreationAnalytics = ShopCreationAnalytics()

    @Provides
    fun provideRegisterAnalytics(): RegisterAnalytics = RegisterAnalytics()
}