package com.tokopedia.iris

import android.content.Context
import com.tokopedia.track.interfaces.ContextAnalytics

class DummyAnalyticsTest(context: Context) : ContextAnalytics(context) {
    override fun sendGeneralEvent(value: MutableMap<String, Any>?) {
        //
    }

    override fun sendGeneralEvent(
        event: String?,
        category: String?,
        action: String?,
        label: String?
    ) {
        //
    }

    override fun sendEnhanceEcommerceEvent(value: MutableMap<String, Any>?) {
    }

    override fun sendScreenAuthenticated(screenName: String?) {
        TODO("Not yet implemented")
    }

    override fun sendScreenAuthenticated(
        screenName: String?,
        customDimension: MutableMap<String, String>?
    ) {
        TODO("Not yet implemented")
    }

    override fun sendScreenAuthenticated(
        screenName: String?,
        shopID: String?,
        shopType: String?,
        pageType: String?,
        productId: String?
    ) {
        TODO("Not yet implemented")
    }

    override fun sendEvent(eventName: String?, eventValue: MutableMap<String, Any>?) {
        TODO("Not yet implemented")
    }
}
