package com.tokopedia.kyc_centralized.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.gojek.jago.onekyc.configs.UnifiedJagoKycConfigsDefault
import com.gojek.kyc.sdk.config.DefaultRemoteConfigProvider
import com.gojek.kyc.sdk.config.KycSdkClientConfig
import com.gojek.kyc.sdk.config.KycSdkConfig
import com.gojek.kyc.sdk.core.constants.KycPlusNetworkConfig
import com.gojek.kyc.sdk.core.utils.KycSdkPartner
import com.gojek.onekyc.OneKycSdk
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor
import com.tokopedia.akamai_bot_lib.interceptor.AkamaiBotInterceptor
import com.tokopedia.config.GlobalConfig
import com.tokopedia.imageuploader.data.entity.ImageUploaderResponseError
import com.tokopedia.kyc_centralized.ui.gotoKyc.oneKycSdk.GotoKycErrorHandler
import com.tokopedia.kyc_centralized.ui.gotoKyc.oneKycSdk.GotoKycEventTrackingProvider
import com.tokopedia.kyc_centralized.ui.gotoKyc.oneKycSdk.GotoKycImageLoader
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.network.utils.OkHttpRetryPolicy
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
open class GoToKycModule {

    private val NET_RETRY = 3

    @ActivityScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @ActivityScope
    @Provides
    fun provideTkpdAuthInterceptor(@ApplicationContext context: Context,
                                   networkRouter: NetworkRouter,
                                   userSessionInterface: UserSessionInterface): TkpdAuthInterceptor {
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
    fun provideOkHttpClient(@ApplicationContext context: Context,
                            tkpdAuthInterceptor: TkpdAuthInterceptor,
                            retryPolicy: OkHttpRetryPolicy,
                            loggingInterceptor: HttpLoggingInterceptor,
                            chuckerInterceptor: ChuckerInterceptor
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(tkpdAuthInterceptor)
        builder.addInterceptor(AkamaiBotInterceptor(context))

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
    fun provideKycSdkClientConfig(): KycSdkClientConfig = KycSdkClientConfig(partner = KycSdkPartner.TOKO, clientId = "KycDemoApp", clientAppVersion =  "0.0.1")

    @Provides
    @ActivityScope
    fun provideKycSdkConfig(
        userSessionInterface: UserSessionInterface,
        kycSdkClientConfig: KycSdkClientConfig
    ): KycSdkConfig {
        return KycSdkConfig(
            isDebugMode = GlobalConfig.isAllowDebuggingTools(),
            userId = userSessionInterface.userId,
            userName = userSessionInterface.name,
            baseUrl = "https://accounts-staging.tokopedia.com",
            clientConfig = kycSdkClientConfig
        )
    }

    @Provides
    @ActivityScope
    fun provideDefaultRemoteConfigProvider(
        @ApplicationContext context: Context,
        gson: Gson
    ): DefaultRemoteConfigProvider {
        return DefaultRemoteConfigProvider(context, gson)
    }

    @Provides
    @ActivityScope
    fun provideGotoKycEventTrackingProvider() = GotoKycEventTrackingProvider()

    @Provides
    @ActivityScope
    fun provideGotoKycErrorHandler() = GotoKycErrorHandler()

    @Provides
    @ActivityScope
    fun provideGotoKycImageLoader() = GotoKycImageLoader()

    @Provides
    @ActivityScope
    fun provideUnifiedJagoKycConfigsDefault() = UnifiedJagoKycConfigsDefault()
    //TODO check the code above, and make sure that use the class (why the name contain jago?)

    @ActivityScope
    @Provides
    fun provideOneKycSdk(
        @ApplicationContext context: Context,
        defaultRemoteConfigProvider: DefaultRemoteConfigProvider,
        gotoKycEventTrackingProvider: GotoKycEventTrackingProvider,
        gotoKycErrorHandler: GotoKycErrorHandler,
        gotoKycImageLoader: GotoKycImageLoader,
        unifiedJagoKycConfigsDefault: UnifiedJagoKycConfigsDefault,
        okHttpClient: OkHttpClient,
        kycSdkConfig: KycSdkConfig
    ): OneKycSdk {
        return OneKycSdk(
            context = context,
            remoteConfig = defaultRemoteConfigProvider,
            eventTracker = gotoKycEventTrackingProvider,
            kycSdkConfig = kycSdkConfig,
            experimentProvider = unifiedJagoKycConfigsDefault,
            errorHandler = gotoKycErrorHandler,
            okHttpClient = okHttpClient,
            imageLoader = gotoKycImageLoader
        )
    }

}
