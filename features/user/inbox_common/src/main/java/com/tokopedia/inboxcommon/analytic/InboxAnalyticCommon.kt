package com.tokopedia.inboxcommon.analytic

object InboxAnalyticCommon {

    private const val KEY_EVENT = "event"
    private const val KEY_EVENT_CATEGORY = "eventCategory"
    private const val KEY_EVENT_ACTION = "eventAction"
    private const val KEY_EVENT_LABEL = "eventLabel"
    private const val KEY_BUSINESS_UNIT = "businessUnit"
    private const val KEY_CURRENT_SITE = "currentSite"
    private const val KEY_USER_ID = "userId"

    fun createGeneralEvent(
            event: String,
            category: String,
            action: String,
            label: String,
            businessUnit: String,
            currentSite: String,
            userId: String
    ): Map<String, Any> {
        return mapOf(
                KEY_EVENT to event,
                KEY_EVENT_CATEGORY to category,
                KEY_EVENT_ACTION to action,
                KEY_EVENT_LABEL to label,
                KEY_BUSINESS_UNIT to businessUnit,
                KEY_CURRENT_SITE to currentSite,
                KEY_USER_ID to userId
        )
    }
}