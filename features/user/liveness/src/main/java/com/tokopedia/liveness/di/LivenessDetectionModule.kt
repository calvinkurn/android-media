package com.tokopedia.liveness.di

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.liveness.analytics.LivenessDetectionAnalytics
import com.tokopedia.liveness.utils.LivenessSharedPreference
import com.tokopedia.liveness.utils.LivenessSharedPreferenceImpl
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import dagger.Module
import dagger.Provides

@Module
class LivenessDetectionModule {

    /*
    * WARNING!!!
    * the value of this variable (sharedPreferenceName) must be the same as the value
    * of the [sharedPreferenceName] variable in [com.tokopedia.kyc_centralized.di.UserIdentificationCommonModule]
    * */
    private val sharedPreferenceName = "kyc_centralized"

    @ActivityScope
    @Provides
    fun provideLivenessPrefInterface(pref: SharedPreferences): LivenessSharedPreference {
        return LivenessSharedPreferenceImpl(pref)
    }

    @ActivityScope
    @Provides
    fun provideAnalytics(
        preference: LivenessSharedPreference
    ): LivenessDetectionAnalytics {
        return LivenessDetectionAnalytics(preference)
    }

    @ActivityScope
    @Provides
    fun provideFirebaseRemoteConfig(@ApplicationContext context: Context): RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }

    @ActivityScope
    @Provides
    fun provideSharedPreference(
        @ApplicationContext context: Context
    ): SharedPreferences = context.getSharedPreferences(sharedPreferenceName, Context.MODE_PRIVATE)
}
