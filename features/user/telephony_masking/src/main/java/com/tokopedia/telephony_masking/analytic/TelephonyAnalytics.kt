package com.tokopedia.telephony_masking.analytic

import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface

class TelephonyAnalytics(private val userSession: UserSessionInterface) {

    private val trackApp: TrackApp
        get() = TrackApp.getInstance()

    fun eventGiveAccess() {
        trackApp.gtm.sendGeneralEvent(getTrackerData(ACTION_CONTACT_US_GIVE_ACCESS))
    }

    fun eventNantiSaja() {
        trackApp.gtm.sendGeneralEvent(getTrackerData(ACTION_CONTACT_US_NANTI_SAJA))
    }

    fun eventClose() {
        trackApp.gtm.sendGeneralEvent(getTrackerData(ACTION_CONTACT_US_CLOSE))
    }

    fun eventSaveContact() {
        trackApp.gtm.sendGeneralEvent(getTrackerData(ACTION_CONTACT_SAVE_CONTACT))
    }

    private fun getTrackerData(action: String): HashMap<String, Any> {
        return HashMap<String,Any>().apply {
            put(KEY_EVENT, EVENT_CONTACT_US)
            put(KEY_EVENT_CATEGORY, CATEGORY_CONTACT_US)
            put(KEY_EVENT_ACTION, action)
            put(KEY_EVENT_LABEL, LABEL_CONTACT_US)
            put(KEY_USER_ID, userSession.userId)
            put(KEY_BUSINESS_UNIT, BUSINESS_UNIT_VALUE)
            put(KEY_CURRENT_SITE, CURRENT_SITE_VALUE)
        }
    }

    companion object {
        private const val KEY_EVENT = "event"
        private const val KEY_EVENT_CATEGORY = "eventCategory"
        private const val KEY_EVENT_ACTION = "eventAction"
        private const val KEY_EVENT_LABEL = "eventLabel"
        private const val KEY_USER_ID = "userId"

        private const val KEY_BUSINESS_UNIT = "businessUnit"
        private const val KEY_CURRENT_SITE = "currentSite"

        private const val BUSINESS_UNIT_VALUE= "Customer Excellence"
        private const val CURRENT_SITE_VALUE = "tokopediamarketplace"

        private const val EVENT_CONTACT_US = "clickContactUs"
        private const val CATEGORY_CONTACT_US = "contact us v3"

        private const val ACTION_CONTACT_US_GIVE_ACCESS = "telephony - click kasih akses"
        private const val ACTION_CONTACT_US_NANTI_SAJA = "telephony - click nanti saja"
        private const val ACTION_CONTACT_US_CLOSE = "telephony - click close"
        private const val ACTION_CONTACT_SAVE_CONTACT = "click save contact"

        private const val LABEL_CONTACT_US = ""
    }
}