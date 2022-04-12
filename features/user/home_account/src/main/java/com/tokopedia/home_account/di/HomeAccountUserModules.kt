package com.tokopedia.home_account.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.home_account.PermissionChecker
import com.tokopedia.home_account.analytics.HomeAccountAnalytics
import com.tokopedia.home_account.view.helper.StaticMenuGenerator
import com.tokopedia.home_account.view.mapper.DataViewMapper
import com.tokopedia.loginfingerprint.tracker.BiometricTracker
import com.tokopedia.navigation_common.model.WalletPref
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
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
    @HomeAccountUserContext
    fun provideContext(): Context = context

    @Provides
    fun provideUserSessionInterface(@HomeAccountUserContext context: Context?): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    fun provideWalletPref(@HomeAccountUserContext context: Context, gson: Gson): WalletPref {
        return WalletPref(context, gson)
    }

    @Provides
    fun provideRemoteConfig(@HomeAccountUserContext context: Context?): RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }

    @Provides
    fun provideDataViewMapper(userSession: UserSessionInterface): DataViewMapper {
        return DataViewMapper(userSession)
    }

    @Provides
    fun providePermissionChecker(): PermissionCheckerHelper {
        return PermissionCheckerHelper()
    }


    @Provides
    fun providePermissionCheck(@HomeAccountUserContext context: Context, permissionChecker: PermissionCheckerHelper): PermissionChecker {
        return PermissionChecker(context, permissionChecker)
    }

    @Provides
    @HomeAccountUserScope
    fun provideHomeAccountPref(@HomeAccountUserContext context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    @Provides
    fun provideHomeAccountAnalytics(userSession: UserSessionInterface): HomeAccountAnalytics {
        return HomeAccountAnalytics(userSession)
    }

    @Provides
    fun provideMenuGenerator(@HomeAccountUserContext context: Context): StaticMenuGenerator {
        return StaticMenuGenerator(context)
    }

    @HomeAccountUserScope
    @Provides
    fun provideMultiRequestGraphql(): MultiRequestGraphqlUseCase = GraphqlInteractor.getInstance().multiRequestGraphqlUseCase

    @Provides
    @HomeAccountUserScope
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    fun provideBiometricTracker(): BiometricTracker = BiometricTracker()
}