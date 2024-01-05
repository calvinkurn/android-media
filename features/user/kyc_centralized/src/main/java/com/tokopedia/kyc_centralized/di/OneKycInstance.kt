package com.tokopedia.kyc_centralized.di

import android.content.Context
import com.gojek.kyc.sdk.config.DefaultRemoteConfigProvider
import com.gojek.kyc.sdk.config.KycSdkConfig
import com.gojek.OneKycSdk
import com.tokopedia.kyc_centralized.ui.gotoKyc.oneKycSdk.GotoKycDefaultCard
import com.tokopedia.kyc_centralized.ui.gotoKyc.oneKycSdk.GotoKycErrorHandler
import com.tokopedia.kyc_centralized.ui.gotoKyc.oneKycSdk.GotoKycEventTrackingProvider
import com.tokopedia.kyc_centralized.ui.gotoKyc.oneKycSdk.GotoKycImageLoader
import com.tokopedia.kyc_centralized.ui.gotoKyc.oneKycSdk.GotoKycUnifiedConfigs
import okhttp3.OkHttpClient


object OneKycInstance {

    private var oneKycSdk: OneKycSdk? = null

    fun getOneKycSdk(
        context: Context,
        defaultRemoteConfigProvider: DefaultRemoteConfigProvider,
        gotoKycEventTrackingProvider: GotoKycEventTrackingProvider,
        gotoKycErrorHandler: GotoKycErrorHandler,
        gotoKycImageLoader: GotoKycImageLoader,
        gotoKycUnifiedConfigs: GotoKycUnifiedConfigs,
        kycPlusDefaultCard: GotoKycDefaultCard,
        okHttpClient: OkHttpClient,
        kycSdkConfig: KycSdkConfig
    ): OneKycSdk {

        return oneKycSdk?.let {
            return it
        } ?: initSdk(
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

    private fun initSdk(
        context: Context,
        defaultRemoteConfigProvider: DefaultRemoteConfigProvider,
        gotoKycEventTrackingProvider: GotoKycEventTrackingProvider,
        gotoKycErrorHandler: GotoKycErrorHandler,
        gotoKycImageLoader: GotoKycImageLoader,
        gotoKycUnifiedConfigs: GotoKycUnifiedConfigs,
        kycPlusDefaultCard: GotoKycDefaultCard,
        okHttpClient: OkHttpClient,
        kycSdkConfig: KycSdkConfig
    ): OneKycSdk {
        val tempOneKycSdk = OneKycSdk(
            context = context,
            remoteConfig = defaultRemoteConfigProvider,
            eventTracker = gotoKycEventTrackingProvider,
            kycSdkConfig = kycSdkConfig,
            experimentProvider = gotoKycUnifiedConfigs,
            errorHandler = gotoKycErrorHandler,
            okHttpClient = okHttpClient,
            imageLoader = gotoKycImageLoader,
            kycPlusCardFactory = kycPlusDefaultCard
        )

        oneKycSdk = tempOneKycSdk
        return tempOneKycSdk
    }
}
