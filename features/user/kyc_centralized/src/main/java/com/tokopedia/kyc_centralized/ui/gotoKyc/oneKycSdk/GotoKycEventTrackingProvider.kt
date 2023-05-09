package com.tokopedia.kyc_centralized.ui.gotoKyc.oneKycSdk

import android.util.Log
import com.gojek.kyc.sdk.config.KycSdkAnalyticsConfig
import com.gojek.kyc.sdk.core.analytics.IKycSdkEventTrackingProvider
import javax.inject.Inject

class GotoKycEventTrackingProvider @Inject constructor(
    private val kycSdkAnalyticsConfig: KycSdkAnalyticsConfig
) : IKycSdkEventTrackingProvider {
    override fun getAnalyticsConfigForClickStream(): KycSdkAnalyticsConfig {
        return kycSdkAnalyticsConfig
    }

    override fun sendPeopleProperty(customerId: String, propertyName: String, propertyValue: Any?): Boolean {
        return true
    }

    override fun sendPeopleProperty(map: MutableMap<String, Any>): Boolean {
        return true
    }

    override fun track(
        eventName: String,
        eventProperties: Map<String, Any?>,
        productName: String?
    ) {
        Log.i("Tracker", "track: $eventName")
    }
}
