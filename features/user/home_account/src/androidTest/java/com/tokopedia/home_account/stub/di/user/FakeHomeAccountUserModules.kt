package com.tokopedia.home_account.stub.di.user

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.applink.user.DeeplinkMapperUser.ROLLENCE_PRIVACY_CENTER
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.home_account.data.pref.AccountPreference
import com.tokopedia.home_account.stub.data.GraphqlRepositoryStub
import com.tokopedia.home_account.stub.domain.FakeUserSession
import com.tokopedia.home_account.view.fragment.HomeAccountUserFragment
import com.tokopedia.loginfingerprint.tracker.BiometricTracker
import com.tokopedia.navigation_common.model.WalletPref
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey.SETTING_SHOW_DARK_MODE_TOGGLE
import com.tokopedia.remoteconfig.RemoteConfigKey.SETTING_SHOW_SCREEN_RECORDER
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.sessioncommon.data.fingerprint.FingerprintPreference
import com.tokopedia.sessioncommon.data.fingerprint.FingerprintPreferenceManager
import com.tokopedia.sessioncommon.util.OclUtils.Companion.OCL_ROLLENCE
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.permission.PermissionCheckerHelper
import dagger.Module
import dagger.Provides
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * @author by nisie on 10/10/18.
 */
@Module
class FakeHomeAccountUserModules(val context: Context) {

    @Provides
    fun provideContext(): Context = context

    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context?): UserSessionInterface {
        return FakeUserSession(context!!)
    }

    @Provides
    fun provideGraphqlRepositoryStub(@ApplicationContext gql: GraphqlRepository): GraphqlRepositoryStub {
        return gql as GraphqlRepositoryStub
    }

    @Provides
    @ActivityScope
    fun provideWalletPref(@ApplicationContext context: Context, gson: Gson): WalletPref {
        return WalletPref(context, gson)
    }

    @Provides
    @ActivityScope
    fun provideRemoteConfig(@ApplicationContext context: Context?): RemoteConfig {
        return mockk(relaxed = true) {
            every { getBoolean(HomeAccountUserFragment.REMOTE_CONFIG_KEY_PRIVACY_ACCOUNT, any()) } returns true
            every { getBoolean(SETTING_SHOW_DARK_MODE_TOGGLE, any()) } returns true
            every { getBoolean(SETTING_SHOW_SCREEN_RECORDER, any()) } returns true
        }
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
    fun provideAccountPref(pref: SharedPreferences): AccountPreference {
        return spyk(AccountPreference(pref)) {
            every { isShowCoachmark() } returns false
        }
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
        return mockk() {
            every { getString(ROLLENCE_PRIVACY_CENTER) } returns ""
            every { getString(OCL_ROLLENCE, any()) } returns ""
        }
    }

    @Provides
    @ActivityScope
    fun provideFingerprintPrefManager(@ApplicationContext context: Context): FingerprintPreference {
        return FingerprintPreferenceManager(context)
    }
}
