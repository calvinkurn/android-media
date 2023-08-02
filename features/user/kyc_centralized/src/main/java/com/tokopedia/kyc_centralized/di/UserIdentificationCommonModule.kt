package com.tokopedia.kyc_centralized.di

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.kyc_centralized.util.CipherProvider
import com.tokopedia.kyc_centralized.util.CipherProviderImpl
import com.tokopedia.kyc_centralized.util.KycSharedPreference
import com.tokopedia.kyc_centralized.util.KycSharedPreferenceImpl
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
class UserIdentificationCommonModule {

    /*
    * WARNING!!!
    * the value of this variable [sharedPreferenceName] must be the same as the value
    * of the [sharedPreferenceName] variable in [com.tokopedia.liveness.di.LivenessDetectionModule]
    * */
    private val sharedPreferenceName = "kyc_centralized"

    @ActivityScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @ActivityScope
    @Provides
    fun provideSharedPreference(
        @ApplicationContext context: Context
    ): SharedPreferences = context.getSharedPreferences(sharedPreferenceName, Context.MODE_PRIVATE)

    @ActivityScope
    @Provides
    fun provideKycPrefInterface(pref: SharedPreferences): KycSharedPreference {
        return KycSharedPreferenceImpl(pref)
    }

    @Provides
    fun provideCipher(): CipherProvider {
        return CipherProviderImpl()
    }

    @Provides
    fun provideRemoteConfig(@ApplicationContext context: Context): RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }
}
