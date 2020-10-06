package com.tokopedia.loginregister.login.dagger

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.tokopedia.abstraction.common.data.model.storage.CacheManager
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.loginfingerprint.data.preference.FingerprintPreferenceHelper
import com.tokopedia.loginfingerprint.data.preference.FingerprintSetting
import com.tokopedia.loginfingerprint.utils.crypto.Cryptography
import com.tokopedia.loginfingerprint.utils.crypto.CryptographyUtils
import com.tokopedia.loginregister.login.di.LoginScope
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers.Main
import javax.inject.Named

/**
 * Created by Yoris Prayogo on 09/07/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

@Module
class MockLoginmodule {

    @LoginScope
    @Provides
    @Named("LOGIN_CACHE")
    fun provideLocalCacheHandler(@ApplicationContext context: Context?): LocalCacheHandler? {
//        return mock(LocalCacheHandler::class.java)
        return LocalCacheHandler(context, "LOGIN_CACHE")
    }

    @LoginScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher {
        return Main
    }

    @LoginScope
    @Provides
    @RequiresApi(Build.VERSION_CODES.M)
    fun provideCryptographyUtils(): Cryptography? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CryptographyUtils()
        } else null
    }

    @LoginScope
    @Provides
    fun provideFingerprintSetting(@ApplicationContext context: Context?): FingerprintSetting {
        return FingerprintPreferenceHelper(context!!)
    }

}