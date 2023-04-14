package com.tokopedia.kyc_centralized.ui.gotoKyc.oneKycSdk

import com.gojek.kyc.sdk.core.analytics.IKycSdkEventTrackingProvider
import javax.inject.Inject

//TODO: change implementation
class GotoKycEventTrackingProvider @Inject constructor() : IKycSdkEventTrackingProvider {

    override fun track(eventName: String, eventProperties: Map<String, Any>, productName: String?) {

    }

    override fun sendPeopleProperty(customerId: String, propertyName: String, propertyValue: Any?): Boolean {
        return true
    }

    override fun sendPeopleProperty(map: MutableMap<String, Any>): Boolean {
        return true
    }
}
