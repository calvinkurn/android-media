package com.tokopedia.kyc_centralized.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.gojek.OneKycSdk
import com.gojek.kyc.sdk.config.DefaultRemoteConfigProvider
import com.gojek.kyc.sdk.config.KycSdkAnalyticsConfig
import com.gojek.kyc.sdk.config.KycSdkClientConfig
import com.gojek.kyc.sdk.config.KycSdkConfig
import com.gojek.kyc.sdk.config.KycSdkUserInfo
import com.gojek.kyc.sdk.config.getDefaultSelfieConfigs
import com.gojek.kyc.sdk.config.getDefaultUnifiedKycConfig
import com.gojek.kyc.sdk.core.constants.KycPlusNetworkConfig
import com.gojek.kyc.sdk.core.utils.KycSdkPartner
import com.gojek.kyc.sdk.core.utils.OneKycClientApp
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.config.GlobalConfig
import com.tokopedia.interceptors.authenticator.TkpdAuthenticatorGql
import com.tokopedia.interceptors.refreshtoken.RefreshTokenGql
import com.tokopedia.kyc_centralized.ui.gotoKyc.oneKycSdk.GotoKycDefaultCard
import com.tokopedia.kyc_centralized.ui.gotoKyc.oneKycSdk.GotoKycErrorHandler
import com.tokopedia.kyc_centralized.ui.gotoKyc.oneKycSdk.GotoKycEventTrackingProvider
import com.tokopedia.kyc_centralized.ui.gotoKyc.oneKycSdk.GotoKycImageLoader
import com.tokopedia.kyc_centralized.ui.gotoKyc.oneKycSdk.GotoKycInterceptor
import com.tokopedia.kyc_centralized.ui.gotoKyc.oneKycSdk.GotoKycUnifiedConfigs
import com.tokopedia.kyc_centralized.util.KycSharedPreference
import com.tokopedia.kyc_centralized.util.KycSharedPreferenceImpl
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.network.utils.OkHttpRetryPolicy
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

@Module
open class GoToKycModule {

    @ActivityScope
    @Provides
    fun provideSharedPreference(
        @ApplicationContext context: Context
    ): SharedPreferences = context.getSharedPreferences(sharedPreferenceName, Context.MODE_PRIVATE)

    @ActivityScope
    @Provides
    open fun provideKycPrefInterface(pref: SharedPreferences): KycSharedPreference {
        return KycSharedPreferenceImpl(pref)
    }

    @ActivityScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @ActivityScope
    @Provides
    fun provideTkpdAuthInterceptor(
        @ApplicationContext context: Context,
        networkRouter: NetworkRouter,
        userSessionInterface: UserSessionInterface
    ): TkpdAuthInterceptor {
        return TkpdAuthInterceptor(context, networkRouter, userSessionInterface)
    }

    @ActivityScope
    @Provides
    fun okHttpRetryPolicy(): OkHttpRetryPolicy {
        return OkHttpRetryPolicy(
            KycPlusNetworkConfig.READ_TIMEOUT.toInt(),
            KycPlusNetworkConfig.WRITE_TIMEOUT.toInt(),
            KycPlusNetworkConfig.CONNECTION_TIMEOUT.toInt(),
            NET_RETRY
        )
    }

    @ActivityScope
    @Provides
    fun provideChuckerInterceptor(@ApplicationContext context: Context): ChuckerInterceptor {
        return ChuckerInterceptor(context)
    }

    @ActivityScope
    @Provides
    fun provideNetworkRouter(@ApplicationContext context: Context): NetworkRouter {
        return context as NetworkRouter
    }

    @ActivityScope
    @Provides
    fun provideTkpdAuthGql(
        @ApplicationContext context: Context,
        networkRouter: NetworkRouter,
        userSessionInterface: UserSessionInterface
    ): TkpdAuthenticatorGql {
        return TkpdAuthenticatorGql(
            application = context as Application,
            networkRouter = networkRouter,
            userSession = userSessionInterface,
            RefreshTokenGql()
        )
    }

    @ActivityScope
    @Provides
    fun provideOkHttpClient(
        retryPolicy: OkHttpRetryPolicy,
        loggingInterceptor: HttpLoggingInterceptor,
        chuckerInterceptor: ChuckerInterceptor,
        gotoKycInterceptor: GotoKycInterceptor,
        tkpdAuthenticatorGql: TkpdAuthenticatorGql
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(gotoKycInterceptor)
        builder.authenticator(tkpdAuthenticatorGql)

        if (GlobalConfig.isAllowDebuggingTools()) {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(loggingInterceptor)
            builder.addInterceptor(chuckerInterceptor)
        }
        builder.readTimeout(retryPolicy.readTimeout.toLong(), TimeUnit.SECONDS)
        builder.connectTimeout(retryPolicy.connectTimeout.toLong(), TimeUnit.SECONDS)
        builder.writeTimeout(retryPolicy.writeTimeout.toLong(), TimeUnit.SECONDS)
        return builder.build()
    }

    @ActivityScope
    @Provides
    fun provideKycSdkClientConfig(): KycSdkClientConfig = KycSdkClientConfig(
        partner = KycSdkPartner.TOKOPEDIA_CORE,
        clientAppId = GlobalConfig.APPLICATION_ID,
        clientAppVersion = GlobalConfig.VERSION_NAME,
        clientKey = GlobalConfig.PACKAGE_APPLICATION,
        clientAppName = OneKycClientApp.TOKOPEDIA_CUSTOMER
    )

    @Provides
    @ActivityScope
    fun provideKycSdkUserInfo(
        userSessionInterface: UserSessionInterface
    ): KycSdkUserInfo {
        return KycSdkUserInfo(
            userId = userSessionInterface.userId
        )
    }

    @Provides
    @ActivityScope
    fun provideKycSdkConfig(
        kycSdkUserInfo: KycSdkUserInfo,
        kycSdkClientConfig: KycSdkClientConfig
    ): KycSdkConfig {
        return KycSdkConfig(
            isDebugMode = GlobalConfig.isAllowDebuggingTools(),
            baseUrl = TokopediaUrl.getInstance().ACCOUNTS,
            clientConfig = kycSdkClientConfig,
            userInfo = kycSdkUserInfo
        )
    }

    @Provides
    @ActivityScope
    fun provideDefaultRemoteConfigProvider(
        @ApplicationContext context: Context
    ): DefaultRemoteConfigProvider {
        val gson = Gson()
        val auroraConfigs = getDefaultSelfieConfigs(context, gson)
            .copy(isAuroraEnabled = isRollenceEnabled(KEY_AURORA))
        val kycConfigs = getDefaultUnifiedKycConfig(context, gson)
            .copy(uploadSelfieSecondImageEnabled = isRollenceEnabled(KEY_UPLOAD_SELFIE))
        return DefaultRemoteConfigProvider(context, gson, kycConfigs, auroraConfigs)
    }

    private fun isRollenceEnabled(key: String): Boolean {
        val remoteConfigInstance = RemoteConfigInstance.getInstance().abTestPlatform
        val rollence = remoteConfigInstance.getFilteredKeyByKeyName(key)
        return if (rollence.isNotEmpty()) {
            remoteConfigInstance.getString(key).isNotEmpty()
        } else {
            true
        }
    }

    @Provides
    @ActivityScope
    fun provideUnifiedKycConfigsDefault() = GotoKycUnifiedConfigs()

    @Provides
    @ActivityScope
    fun provideKycPlusDefaultCard() = GotoKycDefaultCard()

    @Provides
    @ActivityScope
    fun provideRemoteConfig(
        @ApplicationContext context: Context
    ): FirebaseRemoteConfigImpl {
        return FirebaseRemoteConfigImpl(context)
    }

    @Provides
    @ActivityScope
    fun provideKycSdkAnalyticsConfig(
        @ApplicationContext context: Context
    ) = KycSdkAnalyticsConfig(
        apiKey = context.getString(com.tokopedia.keys.R.string.one_kyc_click_stream_api_key),
        url = "${TokopediaUrl.getInstance().ONE_KYC_CLICKSTREAM}$clickstreamEndPoint",
        enableDebugLogs = GlobalConfig.isAllowDebuggingTools()
    )

    @ActivityScope
    @Provides
    fun provideOneKycSdk(
        @ApplicationContext context: Context,
        defaultRemoteConfigProvider: DefaultRemoteConfigProvider,
        gotoKycEventTrackingProvider: GotoKycEventTrackingProvider,
        gotoKycErrorHandler: GotoKycErrorHandler,
        gotoKycImageLoader: GotoKycImageLoader,
        gotoKycUnifiedConfigs: GotoKycUnifiedConfigs,
        kycPlusDefaultCard: GotoKycDefaultCard,
        okHttpClient: OkHttpClient,
        kycSdkConfig: KycSdkConfig
    ): OneKycSdk {
        return OneKycInstance.getOneKycSdk(
            context,
            defaultRemoteConfigProvider,
            gotoKycEventTrackingProvider,
            gotoKycErrorHandler,
            gotoKycImageLoader,
            gotoKycUnifiedConfigs,
            kycPlusDefaultCard,
            okHttpClient,
            kycSdkConfig
        )
    }

    companion object {
        private const val NET_RETRY = 3
        private const val sharedPreferenceName = "kyc_centralized"
        private const val clickstreamEndPoint = "/api/v1/events"
        private const val KEY_UPLOAD_SELFIE = "goto_kyc_selfie_and"
        private const val KEY_AURORA = "goto_kyc_and_aurora"
    }
}
