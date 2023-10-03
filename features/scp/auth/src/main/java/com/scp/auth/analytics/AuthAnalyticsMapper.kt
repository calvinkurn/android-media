package com.scp.auth.analytics

import com.tokopedia.track.TrackAppUtils

class AuthAnalyticsMapper {

    private fun createData(trackerId: String, event: String, category: String, action: String, label: String, customDimension: Map<String, Any>? = null): MutableMap<String, Any> {
        val commonData = TrackAppUtils.gtmData(event, category, action, label)
        commonData.put(TRACKER_ID, trackerId)
        if (customDimension != null) {
            commonData.putAll(customDimension)
        }
        return commonData
    }

    companion object {
        private const val FAILED_ACCOUNT_EVENT = "todo please replace with the correct value"
        private const val CLICK_ACCOUNT_EVENT = "clickAccount"
        private const val VIEW_ACCOUNT_EVENT = "viewAccountIris"
        private const val CVSDK_VERIFICATION_CATEGORY = "cvsdk verification page"

        private const val ANDROID_ID = "androidId"
        private const val SDK_VERSION = "sdkVersion"
        private const val BUSINESS_UNIT = "businessUnit"
        private const val TRACKER_ID = "trackerId"
    }
}
