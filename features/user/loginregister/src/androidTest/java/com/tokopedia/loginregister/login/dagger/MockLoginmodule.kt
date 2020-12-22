package com.tokopedia.loginregister.login.dagger

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.loginfingerprint.data.preference.FingerprintPreferenceHelper
import com.tokopedia.loginfingerprint.data.preference.FingerprintSetting
import com.tokopedia.loginfingerprint.utils.crypto.Cryptography
import com.tokopedia.loginfingerprint.utils.crypto.CryptographyUtils
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

    @Provides
    @Named("LOGIN_CACHE")
    fun provideLocalCacheHandler(@ApplicationContext context: Context?): LocalCacheHandler? {
        return LocalCacheHandler(context, "LOGIN_CACHE")
    }

    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher {
        return Main
    }

    @Provides
    @RequiresApi(Build.VERSION_CODES.M)
    fun provideCryptographyUtils(): Cryptography? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CryptographyUtils()
        } else null
    }

    @Provides
    fun provideFingerprintSetting(@ApplicationContext context: Context): FingerprintSetting {
        return FingerprintPreferenceHelper(context)
    }

}