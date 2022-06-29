package com.tokopedia.loginregister.login.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.encryption.security.AeadEncryptor
import com.tokopedia.encryption.security.AeadEncryptorImpl
import com.tokopedia.loginregister.common.view.bottomsheet.SocmedBottomSheet
import com.tokopedia.loginregister.goto_seamless.GotoSeamlessPreference
import com.tokopedia.sessioncommon.data.fingerprint.FingerprintPreference
import com.tokopedia.sessioncommon.data.fingerprint.FingerprintPreferenceManager
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

/**
 * @author by nisie on 10/10/18.
 */
@Module
open class LoginModule {
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
    @Named(NAMED_DISPATCHERS_IO)
    fun provideIoDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @LoginScope
    @Provides
    fun provideSocmedBottomSheet(@ApplicationContext context: Context): SocmedBottomSheet {
        return SocmedBottomSheet(context)
    }

    @Provides
    fun provideFingerprintPreferenceManager(@ApplicationContext context: Context): FingerprintPreference = FingerprintPreferenceManager(context)

    @Provides
    fun provideAeadEncryptor(@ApplicationContext context: Context): AeadEncryptor = AeadEncryptorImpl(context)

    @Provides
    fun provideGotoSeamlessPreference(@ApplicationContext context: Context, aeadEncryptor: AeadEncryptor): GotoSeamlessPreference {
        return GotoSeamlessPreference(context, aeadEncryptor)
    }

    companion object {
        const val NAMED_DISPATCHERS_IO = "DispatcherIO"
        const val LOGIN_CACHE = "LOGIN_CACHE"
    }
}