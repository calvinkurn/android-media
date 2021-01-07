package com.tokopedia.inboxcommon.analytic

object InboxAnalyticCommon {

    private const val KEY_EVENT = "event"
    private const val KEY_EVENT_CATEGORY = "eventCategory"
    private const val KEY_EVENT_ACTION = "eventAction"
    private const val KEY_EVENT_LABEL = "eventLabel"
    private const val KEY_BUSINESS_UNIT = "businessUnit"
    private const val KEY_CURRENT_SITE = "currentSite"
    private const val KEY_USER_ID = "userId"
    private const val KEY_IS_LOGGED_IN = "isLoggedInStatus"
    private const val KEY_SCREEN_NAME = "screenName"
    private const val KEY_USER_ROLE = "userRole"

    fun createGeneralEvent(
            event: String? = null,
            eventCategory: String? = null,
            eventAction: String? = null,
            eventLabel: String? = null,
            businessUnit: String? = null,
            currentSite: String? = null,
            userId: String? = null,
            isLoggedInStatus: Boolean? = null,
            screenName: String? = null,
            userRole: String? = null
    ): Map<String, String> {
        val mapTrack: MutableMap<String, String> =  mutableMapOf()
        event?.let {
            mapTrack[KEY_EVENT] = it
        }
        businessUnit?.let {
            mapTrack[KEY_BUSINESS_UNIT] = it
        }
        currentSite?.let {
            mapTrack[KEY_CURRENT_SITE] = it
        }
        userId?.let {
            mapTrack[KEY_USER_ID] = it
        }
        eventCategory?.let {
            mapTrack[KEY_EVENT_CATEGORY] = it
        }
        eventLabel?.let {
            mapTrack[KEY_EVENT_LABEL] = it
        }
        eventAction?.let {
            mapTrack[KEY_EVENT_ACTION] = it
        }
        isLoggedInStatus?.let {
            mapTrack[KEY_IS_LOGGED_IN] = it.toString()
        }
        screenName?.let {
            mapTrack[KEY_SCREEN_NAME] = it
        }
        userRole?.let {
            mapTrack[KEY_USER_ROLE] = it
        }
        return mapTrack
    }
}