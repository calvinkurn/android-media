package com.tokopedia.sellerhome.di.module

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.interceptors.authenticator.TkpdAuthenticatorGql
import com.tokopedia.interceptors.refreshtoken.RefreshTokenGql
import com.tokopedia.network.NetworkRouter
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.seller.menu.common.analytics.SellerMenuTracker
import com.tokopedia.sellerhome.common.config.SellerHomeRemoteConfig
import com.tokopedia.sellerhome.di.scope.SellerHomeScope
import com.tokopedia.sellerhome.settings.analytics.SettingFreeShippingTracker
import com.tokopedia.sellerhomecommon.data.WidgetLastUpdatedSharedPref
import com.tokopedia.sellerhomecommon.data.WidgetLastUpdatedSharedPrefInterface
import com.tokopedia.sellerhomecommon.sse.SellerHomeWidgetSSE
import com.tokopedia.sellerhomecommon.sse.SellerHomeWidgetSSEImpl
import com.tokopedia.sellerhomecommon.sse.mapper.WidgetSSEMapper
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 * Created By @ilhamsuaib on 2020-01-14
 */

@Module
class SellerHomeModule {

    companion object {
        private const val VOUCHER_CREATION_PREF_NAME = "voucher_creation"
        private const val READ_TIME_OUT = 0L
    }

    @SellerHomeScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @SellerHomeScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @SellerHomeScope
    @Provides
    fun provideRemoteConfig(@ApplicationContext context: Context): FirebaseRemoteConfigImpl =
        FirebaseRemoteConfigImpl(context)

    @SellerHomeScope
    @Provides
    fun provideSellerHomeRemoteConfig(remoteConfig: FirebaseRemoteConfigImpl): SellerHomeRemoteConfig {
        return SellerHomeRemoteConfig(remoteConfig)
    }

    @SellerHomeScope
    @Provides
    fun provideFreeShippingTracker(userSession: UserSessionInterface): SettingFreeShippingTracker {
        val analytics = TrackApp.getInstance().gtm
        return SettingFreeShippingTracker(analytics, userSession)
    }

    @SellerHomeScope
    @Provides
    fun provideSellerMenuTracker(userSession: UserSessionInterface): SellerMenuTracker {
        val analytics = TrackApp.getInstance().gtm
        return SellerMenuTracker(analytics, userSession)
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
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        userSession: UserSessionInterface
    ): OkHttpClient {
        val authenticator = getAuthenticator(context, userSession)
        val builder = OkHttpClient.Builder()
            .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
        if (authenticator != null) {
            builder.authenticator(authenticator)
        }
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

    private fun getAuthenticator(
        context: Context,
        userSession: UserSessionInterface
    ): TkpdAuthenticatorGql? {
        return try {
            TkpdAuthenticatorGql(
                context as BaseMainApplication,
                context as NetworkRouter,
                userSession as UserSession,
                RefreshTokenGql()
            )
        } catch (e: ClassCastException) {
            null
        }
    }

}
