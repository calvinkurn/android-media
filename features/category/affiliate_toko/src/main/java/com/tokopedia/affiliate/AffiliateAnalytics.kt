package com.tokopedia.affiliate

import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.Analytics

object AffiliateAnalytics {

    private fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }

    fun sendEvent(event: String, action: String, category: String,
                  label: String, userId : String) {
        HashMap<String,Any>().apply {
            put(EventKeys.KEY_EVENT,event)
            put(EventKeys.KEY_EVENT_ACTION,action)
            put(EventKeys.KEY_EVENT_CATEGORY,category)
            put(EventKeys.KEY_EVENT_LABEL,label)
            put(EventKeys.KEY_USER_ID,userId)
            put(EventKeys.KEY_BUSINESS_UNIT,EventKeys.BUSINESS_UNIT_VALUE)
            put(EventKeys.KEY_CURRENT_SITE,EventKeys.CURRENT_SITE_VALUE)
        }.also {
            getTracker().sendGeneralEvent(it)
        }
    }

    interface EventKeys {
        companion object {
            const val KEY_EVENT = "event"
            const val KEY_EVENT_CATEGORY = "eventCategory"
            const val KEY_EVENT_ACTION = "eventAction"
            const val KEY_EVENT_LABEL = "eventLabel"
            const val KEY_USER_ID = "userId"

            const val KEY_BUSINESS_UNIT = "businessUnit"
            const val KEY_CURRENT_SITE = "currentSite"

            const val BUSINESS_UNIT_VALUE= "affiliate"
            const val CURRENT_SITE_VALUE = "tokopediamarketplace"

            const val EVENT_VALUE_CLICK = "clickAffiliate"
            const val EVENT_VALUE_VIEW = "viewAffiliateIris"
        }
    }

    interface CategoryKeys {
        companion object {
            const val PROMOSIKAN_SRP_B_S = "promosikan srp - bottom sheet"
            const val HOME_PORTAL_B_S = "home portal - bottom sheet"
            const val PROMOSIKAN_SRP = "promosikan srp"
            const val HOME_PORTAL = "home portal"
        }
    }

    interface ActionKeys {
        companion object {
            const val CLICK_SALIN_LINK = "click - salin link"
            const val IMPRESSION_LINK_GEN_ERROR = "impression - link generation error"
            const val IMPRESSION_NOT_LINK_ERROR = "impression - not link error"
            const val IMPRESSION_NOT_FOUND_ERROR = "impression - not found error"
            const val IMPRESSION_NOT_ELIGIBLE = "impression - not eligible error"
            const val IMPRESSION_NOT_OS_PM_ERROR = "impression - not os pm error"
            const val IMPRESSION_PROMOSIKAN_SRP = "impression - promosikan srp"
            const val IMPRESSION_PROMOSIKAN_SRP_B_S = "impression - promosikan srp - bottom sheet"
            const val IMPRESSION_HOME_PORTAL = "impression - home portal"
            const val IMPRESSION_HOME_PORTAL_B_S = "impression - home portal - bottom sheet"
            const val CLICK_SEARCH = "click - search"
        }
    }
}