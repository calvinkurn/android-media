package com.tokopedia.sellerhome.stub.features.home.di

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.sellerhome.common.config.SellerHomeRemoteConfig
import com.tokopedia.commissionbreakdown.di.scope.CommissionBreakdownScope
import com.tokopedia.sellerhome.stub.data.UserSessionStub
import com.tokopedia.sellerhome.stub.gql.GraphqlRepositoryStub
import com.tokopedia.sellerhomecommon.data.WidgetLastUpdatedSharedPref
import com.tokopedia.sellerhomecommon.data.WidgetLastUpdatedSharedPrefInterface
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * Created by @ilhamsuaib on 06/12/21.
 */

@Module
class SellerHomeModuleStub {

    companion object {
        private const val VOUCHER_CREATION_PREF_NAME = "voucher_creation_pref_name"
    }

    @CommissionBreakdownScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSessionStub(context)
    }

    @CommissionBreakdownScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlRepositoryStub.getInstance()
    }

    @CommissionBreakdownScope
    @Provides
    fun provideRemoteConfig(@ApplicationContext context: Context): FirebaseRemoteConfigImpl {
        return FirebaseRemoteConfigImpl(context)
    }

    @CommissionBreakdownScope
    @Provides
    fun provideSellerHomeRemoteConfig(remoteConfig: FirebaseRemoteConfigImpl): SellerHomeRemoteConfig {
        return SellerHomeRemoteConfig(remoteConfig)
    }

    @CommissionBreakdownScope
    @Provides
    fun provideWidgetLastUpdatePref(@ApplicationContext context: Context): WidgetLastUpdatedSharedPrefInterface {
        return WidgetLastUpdatedSharedPref(context)
    }

    @CommissionBreakdownScope
    @Provides
    fun provideLastUpdatedInfoEnabled(): Boolean {
        return true
    }

    @CommissionBreakdownScope
    @Provides
    fun provideVoucherCreationSharedPref(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(VOUCHER_CREATION_PREF_NAME, Context.MODE_PRIVATE)
    }
}