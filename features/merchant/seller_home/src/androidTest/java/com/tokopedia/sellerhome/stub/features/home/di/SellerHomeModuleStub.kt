package com.tokopedia.sellerhome.stub.features.home.di

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.sellerhome.common.config.SellerHomeRemoteConfig
import com.tokopedia.sellerhome.di.scope.SellerHomeScope
import com.tokopedia.sellerhome.stub.data.UserSessionStub
import com.tokopedia.sellerhome.stub.gql.GraphqlRepositoryStub
import com.tokopedia.sellerhomecommon.data.WidgetLastUpdatedSharedPref
import com.tokopedia.sellerhomecommon.data.WidgetLastUpdatedSharedPrefInterface
import com.tokopedia.sellerhomecommon.sse.SellerHomeWidgetSSE
import com.tokopedia.sellerhomecommon.sse.SellerHomeWidgetSSEImpl
import com.tokopedia.sellerhomecommon.sse.mapper.WidgetSSEMapper
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 * Created by @ilhamsuaib on 06/12/21.
 */

@Module
class SellerHomeModuleStub {

    companion object {
        private const val VOUCHER_CREATION_PREF_NAME = "voucher_creation_pref_name"
    }

    @SellerHomeScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSessionStub(context)
    }

    @SellerHomeScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlRepositoryStub.getInstance()
    }

    @SellerHomeScope
    @Provides
    fun provideRemoteConfig(@ApplicationContext context: Context): FirebaseRemoteConfigImpl {
        return FirebaseRemoteConfigImpl(context)
    }

    @SellerHomeScope
    @Provides
    fun provideSellerHomeRemoteConfig(remoteConfig: FirebaseRemoteConfigImpl): SellerHomeRemoteConfig {
        return SellerHomeRemoteConfig(remoteConfig)
    }

    @SellerHomeScope
    @Provides
    fun provideWidgetLastUpdatePref(@ApplicationContext context: Context): WidgetLastUpdatedSharedPrefInterface {
        return WidgetLastUpdatedSharedPref(context)
    }

    @SellerHomeScope
    @Provides
    fun provideLastUpdatedInfoEnabled(): Boolean {
        return true
    }

    @SellerHomeScope
    @Provides
    fun provideVoucherCreationSharedPref(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(VOUCHER_CREATION_PREF_NAME, Context.MODE_PRIVATE)
    }

    @SellerHomeScope
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .readTimeout(0L, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
        return builder.build()
    }

    @SellerHomeScope
    @Provides
    fun provideSellerHomeSSE(
        @ApplicationContext context: Context,
        sseMapper: WidgetSSEMapper,
        userSession: UserSessionInterface,
        sseOkHttpClient: OkHttpClient,
        dispatchers: CoroutineDispatchers
    ): SellerHomeWidgetSSE {
        return SellerHomeWidgetSSEImpl(
            context,
            userSession,
            sseMapper,
            sseOkHttpClient,
            dispatchers
        )
    }
}