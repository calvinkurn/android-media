package com.tokopedia.loginregister.login.behaviour.di.modules

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.loginfingerprint.data.preference.FingerprintPreferenceHelper
import com.tokopedia.loginfingerprint.data.preference.FingerprintSetting
import com.tokopedia.loginfingerprint.utils.crypto.Cryptography
import com.tokopedia.loginfingerprint.utils.crypto.CryptographyUtils
import com.tokopedia.loginregister.common.view.bottomsheet.SocmedBottomSheet
import com.tokopedia.loginregister.login.di.LoginScope
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

@Module
class MockLoginModule {
    @LoginScope
    @Provides
    @Named(LOGIN_CACHE)
    fun provideLocalCacheHandler(@ApplicationContext context: Context): LocalCacheHandler {
        return LocalCacheHandler(context, LOGIN_CACHE)
    }

    @LoginScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher {
        return Dispatchers.Main
    }

    @LoginScope
    @Provides
    @RequiresApi(Build.VERSION_CODES.M)
    open fun provideCryptographyUtils(): Cryptography? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CryptographyUtils()
        } else null
    }

    @LoginScope
    @Provides
    fun provideFingerprintSetting(@ApplicationContext context: Context): FingerprintSetting {
        return FingerprintPreferenceHelper(context)
    }

    @LoginScope
    @Provides
    fun provideSocmedBottomSheet(@ApplicationContext context: Context): SocmedBottomSheet {
        return SocmedBottomSheet(context)
    }

    companion object {
        const val LOGIN_CACHE = "LOGIN_CACHE"
    }
}