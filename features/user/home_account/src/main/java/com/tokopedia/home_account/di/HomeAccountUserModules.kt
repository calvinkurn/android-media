package com.tokopedia.home_account.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.loginfingerprint.tracker.BiometricTracker
import com.tokopedia.navigation_common.model.WalletPref
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.sessioncommon.data.fingerprint.FingerprintPreference
import com.tokopedia.sessioncommon.data.fingerprint.FingerprintPreferenceManager
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.permission.PermissionCheckerHelper
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * @author by nisie on 10/10/18.
 */
@Module
class HomeAccountUserModules(val context: Context) {

    @Provides
    fun provideContext(): Context = context

    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context?): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    @ActivityScope
    fun provideWalletPref(@ApplicationContext context: Context, gson: Gson): WalletPref {
        return WalletPref(context, gson)
    }

    @Provides
    @ActivityScope
    fun provideRemoteConfig(@ApplicationContext context: Context?): RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }

    @Provides
    @ActivityScope
    fun providePermissionChecker(): PermissionCheckerHelper {
        return PermissionCheckerHelper()
    }

    @Provides
    @ActivityScope
    fun provideHomeAccountPref(@ApplicationContext context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    @Provides
    @ActivityScope
    fun provideMultiRequestGraphql(): MultiRequestGraphqlUseCase = GraphqlInteractor.getInstance().multiRequestGraphqlUseCase

    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @ActivityScope
    fun provideBiometricTracker(): BiometricTracker = BiometricTracker()

    @Provides
    @ActivityScope
    fun provideAbTestPlatform(): AbTestPlatform {
        return RemoteConfigInstance.getInstance().abTestPlatform
    }

    @Provides
    @ActivityScope
    fun provideFingerprintPrefManager(@ApplicationContext context: Context): FingerprintPreference {
        return FingerprintPreferenceManager(context)
    }
}
