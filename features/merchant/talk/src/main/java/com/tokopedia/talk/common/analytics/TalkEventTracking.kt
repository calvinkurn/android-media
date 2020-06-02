package com.tokopedia.talk.common.analytics

class TalkEventTracking(category: String, action: String, label: String, userId: String, productId: String) {

    private var tracking: Map<String, String> = HashMap()

    val dataTracking: Map<String, String> get() = tracking

    init {
        with(TalkTrackingConstants) {
            tracking = mapOf(
                    TRACKING_EVENT to EVENT_TALK,
                    TRACKING_EVENT_CATEGORY to category,
                    TRACKING_EVENT_ACTION to action,
                    TRACKING_EVENT_LABEL to label,
                    TRACKING_SCREEN_NAME to SCREEN_NAME_TALK,
                    TRACKING_CURRENT_SITE to CURRENT_SITE_TALK,
                    TRACKING_USER_ID to userId,
                    TRACKING_BUSINESS_UNIT to BUSINESS_UNIT_TALK,
                    TRACKING_PRODUCT_ID to productId
            )
        }
    }

}