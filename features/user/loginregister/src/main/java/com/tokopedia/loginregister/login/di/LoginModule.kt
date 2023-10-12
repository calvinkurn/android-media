package com.tokopedia.loginregister.login.di

import android.content.Context
import android.content.res.Resources
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.encryption.security.AeadEncryptor
import com.tokopedia.encryption.security.AeadEncryptorImpl
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.loginregister.goto_seamless.GotoSeamlessPreference
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.sessioncommon.data.fingerprint.FingerprintPreference
import com.tokopedia.sessioncommon.data.fingerprint.FingerprintPreferenceManager
import com.tokopedia.sessioncommon.util.OclUtils
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * @author by nisie on 10/10/18.
 */
@Module
open class LoginModule {

    @ActivityScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @ActivityScope
    @Provides
    fun provideFingerprint(@ApplicationContext context: Context): FingerprintPreference {
        return FingerprintPreferenceManager(context)
    }

    @ActivityScope
    @Provides
    open fun provideIrisSession(@ApplicationContext context: Context): IrisSession {
        return IrisSession(context)
    }

    @ActivityScope
    @Provides
    open fun provideResources(@ApplicationContext context: Context): Resources {
        return context.resources
    }

    @ActivityScope
    @Provides
    open fun provideFirebaseRemoteConfig(@ApplicationContext context: Context): RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }

    @Provides
    fun provideAeadEncryptor(@ApplicationContext context: Context): AeadEncryptor = AeadEncryptorImpl(context)

    @Provides
    fun provideGotoSeamlessPreference(@ApplicationContext context: Context, aeadEncryptor: AeadEncryptor): GotoSeamlessPreference {
        return GotoSeamlessPreference(context, aeadEncryptor)
    }

    @ActivityScope
    @Provides
    open fun provideAbTestPlatform(): AbTestPlatform {
        return RemoteConfigInstance.getInstance().abTestPlatform
    }

    @Provides
    @ActivityScope
    fun provideOclUtils(abTestPlatform: AbTestPlatform): OclUtils {
        return OclUtils(abTestPlatform)
    }
}
