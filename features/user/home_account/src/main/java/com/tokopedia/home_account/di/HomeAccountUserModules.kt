package com.tokopedia.home_account.di

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.home_account.PermissionChecker
import com.tokopedia.home_account.analytics.HomeAccountAnalytics
import com.tokopedia.home_account.pref.AccountPreference
import com.tokopedia.home_account.view.helper.StaticMenuGenerator
import com.tokopedia.home_account.view.mapper.DataViewMapper
import com.tokopedia.navigation_common.model.WalletPref
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.permission.PermissionCheckerHelper
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers.Main

/**
 * @author by nisie on 10/10/18.
 */
@Module
class HomeAccountUserModules(val context: Context) {

    @HomeAccountUserScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher {
        return Main
    }

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
    fun provideAccountPreference(@HomeAccountUserContext context: Context): AccountPreference {
        return AccountPreference(context)
    }

    @Provides
    fun provideHomeAccountAnalytics(@HomeAccountUserContext context: Context, userSession: UserSessionInterface): HomeAccountAnalytics {
        return HomeAccountAnalytics(context, userSession)
    }

    @Provides
    fun provideMenuGenerator(@HomeAccountUserContext context: Context): StaticMenuGenerator {
        return StaticMenuGenerator(context)
    }

}