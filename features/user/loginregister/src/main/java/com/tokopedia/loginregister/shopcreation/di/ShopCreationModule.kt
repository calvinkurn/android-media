package com.tokopedia.loginregister.shopcreation.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.loginregister.common.analytics.ShopCreationAnalytics
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

/**
 * Created by Ade Fulki on 2019-12-09.
 * ade.hadian@tokopedia.com
 */

@ShopCreationScope
@Module
class ShopCreationModule {

    @ShopCreationScope
    @Provides
    @Named(ShopCreationQueryConstant.DISPATCHERS_MAIN)
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @ShopCreationScope
    @Provides
    @Named(ShopCreationQueryConstant.DISPATCHERS_IO)
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @ShopCreationScope
    @Provides
    fun provideShopCreationAnalytics(): ShopCreationAnalytics = ShopCreationAnalytics()
}