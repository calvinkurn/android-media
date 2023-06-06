package com.tokopedia.kyc_centralized.ui.gotoKyc.oneKycSdk

import com.gojek.kyc.sdk.core.analytics.IKycSdkEventTrackingProvider

//TODO: change implementation
class GotoKycEventTrackingProvider : IKycSdkEventTrackingProvider {

    override fun track(eventName: String, eventProperties: Map<String, Any>, productName: String?) {

    }

    override fun sendPeopleProperty(customerId: String, propertyName: String, propertyValue: Any?): Boolean {
        return true
    }

    override fun sendPeopleProperty(map: MutableMap<String, Any>): Boolean {
        return true
    }
}
