package com.tokopedia.home_account.stub.di.user

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.home_account.AccountConstants
import com.tokopedia.home_account.PermissionChecker
import com.tokopedia.home_account.analytics.AddVerifyPhoneAnalytics
import com.tokopedia.home_account.analytics.HomeAccountAnalytics
import com.tokopedia.home_account.analytics.TokopediaPlusAnalytics
import com.tokopedia.home_account.stub.domain.FakeUserSession
import com.tokopedia.home_account.view.helper.StaticMenuGenerator
import com.tokopedia.home_account.view.mapper.DataViewMapper
import com.tokopedia.loginfingerprint.tracker.BiometricTracker
import com.tokopedia.navigation_common.model.WalletPref
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.user.session.datastore.UserSessionDataStore
import com.tokopedia.utils.permission.PermissionCheckerHelper
import dagger.Lazy
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * @author by nisie on 10/10/18.
 */
@Module
class FakeHomeAccountUserModules(val context: Context) {

    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return FakeUserSession(context)
    }

    @Provides
    fun provideWalletPref(@ApplicationContext context: Context, gson: Gson): WalletPref {
        return WalletPref(context, gson)
    }

    @Provides
    fun provideRemoteConfig(@ApplicationContext context: Context?): RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }

    @Provides
    fun provideDataViewMapper(userSession: UserSessionInterface, userSessionDataStore: Lazy<UserSessionDataStore>): DataViewMapper {
        return DataViewMapper(userSession, userSessionDataStore)
    }

    @Provides
    fun providePermissionChecker(): PermissionCheckerHelper {
        return PermissionCheckerHelper()
    }


    @Provides
    fun providePermissionCheck(@ApplicationContext context: Context, permissionChecker: PermissionCheckerHelper): PermissionChecker {
        return PermissionChecker(context, permissionChecker)
    }

    @Provides
    @ActivityScope
    fun provideHomeAccountPref(@ApplicationContext context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context).apply {
            val editor = edit()
            editor?.putBoolean(AccountConstants.KEY.KEY_SHOW_COACHMARK, false)
            editor?.apply()
        }
    }

    @Provides
    fun provideHomeAccountAnalytics(userSession: UserSessionInterface): HomeAccountAnalytics {
        return HomeAccountAnalytics(userSession)
    }

    @Provides
    fun provideBiometricTracker(): BiometricTracker = BiometricTracker()

    @Provides
    fun provideMenuGenerator(@ApplicationContext context: Context): StaticMenuGenerator {
        return StaticMenuGenerator(context)
    }

    @ActivityScope
    @Provides
    fun provideMultiRequestGraphql(): MultiRequestGraphqlUseCase = GraphqlInteractor.getInstance().multiRequestGraphqlUseCase

    @Provides
    @ActivityScope
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @ActivityScope
    fun provideTokopediaPlusAnalytics(): TokopediaPlusAnalytics {
        return TokopediaPlusAnalytics()
    }

    @Provides
    @ActivityScope
    fun provideAddVerifyPhoneAnalytics(): AddVerifyPhoneAnalytics {
        return AddVerifyPhoneAnalytics()
    }
}
