package com.tokopedia.wallet.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wallet.ovoactivation.provider.WalletProvider
import com.tokopedia.wallet.ovoactivation.provider.WalletScheduler
import dagger.Module
import dagger.Provides

@Module
class WalletModule {

    @Provides
    fun provideRemoteConfig(@ApplicationContext context: Context): RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }

    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    fun provideWalletProvider() : WalletProvider {
        return WalletScheduler()
    }
}