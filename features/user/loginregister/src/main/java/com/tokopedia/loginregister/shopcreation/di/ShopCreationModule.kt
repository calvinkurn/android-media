package com.tokopedia.loginregister.shopcreation.di

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.loginfingerprint.utils.crypto.Cryptography
import com.tokopedia.loginfingerprint.utils.crypto.CryptographyUtils
import com.tokopedia.loginregister.common.analytics.RegisterAnalytics
import com.tokopedia.loginregister.common.analytics.ShopCreationAnalytics
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Created by Ade Fulki on 2019-12-09.
 * ade.hadian@tokopedia.com
 */

@Module
class ShopCreationModule {

    @ShopCreationScope
    @Provides
    fun provideCoroutineDispatchersProvider(): CoroutineDispatchers {
        return CoroutineDispatchersProvider
    }

    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @ShopCreationScope
    @Provides
    fun provideShopCreationAnalytics(): ShopCreationAnalytics = ShopCreationAnalytics()

    @Provides
    fun provideRegisterAnalytics(): RegisterAnalytics = RegisterAnalytics()

    @ShopCreationScope
    @Provides
    @RequiresApi(Build.VERSION_CODES.M)
    fun provideCryptographyUtils(): Cryptography? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CryptographyUtils()
        } else null
    }
}