package com.tokopedia.notifcenter.analytics

import androidx.core.os.bundleOf
import com.tokopedia.notifcenter.data.uimodel.affiliate.NotificationAffiliateEducationUiModel
import com.tokopedia.track.TrackApp

object NotificationAffiliateAnalytics {

    private const val BUSINESS_UNIT = "affiliate"
    private const val CURRENT_SITE = "tokopediamarketplace"
    private val SESSION_IRIS = TrackApp.getInstance().gtm.irisSessionId

    private object Tracker {
        const val EDUCATION_IMPRESSION = 42736
        const val EDUCATION_SEE_MORE_CLICK = 42739
        const val EDUCATION_CLICK = 42737
    }

    private object EventKey {
        const val KEY_EVENT = "event"
        const val KEY_EVENT_ACTION = "eventAction"
        const val KEY_EVENT_CATEGORY = "eventCategory"
        const val KEY_EVENT_LABEL = "eventLabel"
        const val KEY_TRACKER_ID = "trackerId"
        const val KEY_BUSINESS_UNIT = "businessUnit"
        const val KEY_CURRENT_SITE = "currentSite"
        const val KEY_SESSION_IRIS = "sessionIris"
        const val KEY_USER_ID = "userId"
        const val KEY_PROMOTIONS = "promotions"
        const val KEY_ITEM_ID = "item_id"
        const val KEY_ITEM_NAME = "item_name"
        const val KEY_CREATIVE_SLOT = "creative_slot"
    }

    private object Event {
        const val VIEW_ITEM = "view_item"
        const val CLICK_CONTENT = "clickContent"
    }

    private object EventAction {
        const val IMPRESSION_EDUCATION = "impression - education section"
        const val CLICK_EDUCATION_LIHAT_SEMUA = "click - lihat semua"
        const val CLICK_EDUCATION_SECTION = "click - education section"
    }

    object EventCategory {
        const val AFFILIATE_HOME_NOTIFICATION_CENTER =
            "affiliate home page - notification center"
        const val AFFILIATE_NOTIFICATION_CENTER =
            "notif center affiliate"
    }

    private object ItemKey {
        const val AFFILIATE_NOTIFICATION_CENTER_EDU =
            "/affiliate home page - notification center - education section"
    }

    fun trackAffiliateEducationImpression(
        element: NotificationAffiliateEducationUiModel,
        position: Int,
        userId: String
    ) {
        val itemBundle = bundleOf(
            EventKey.KEY_ITEM_ID to element.data.id,
            EventKey.KEY_CREATIVE_SLOT to (position + 1).toString(),
            EventKey.KEY_ITEM_NAME to ItemKey.AFFILIATE_NOTIFICATION_CENTER_EDU
        )
        val bundle = bundleOf(
            EventKey.KEY_EVENT to Event.VIEW_ITEM,
            EventKey.KEY_EVENT_CATEGORY to EventCategory.AFFILIATE_HOME_NOTIFICATION_CENTER,
            EventKey.KEY_EVENT_ACTION to EventAction.IMPRESSION_EDUCATION,
            EventKey.KEY_EVENT_LABEL to "",
            EventKey.KEY_TRACKER_ID to Tracker.EDUCATION_IMPRESSION,
            EventKey.KEY_BUSINESS_UNIT to BUSINESS_UNIT,
            EventKey.KEY_CURRENT_SITE to CURRENT_SITE,
            EventKey.KEY_SESSION_IRIS to SESSION_IRIS,
            EventKey.KEY_USER_ID to userId,
            EventKey.KEY_PROMOTIONS to arrayListOf(itemBundle)
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(Event.VIEW_ITEM, bundle)
    }

    fun trackAffiliateEducationSeeMoreClick(
        userId: String
    ) {
        val data = mapOf(
            EventKey.KEY_EVENT to Event.CLICK_CONTENT,
            EventKey.KEY_EVENT_CATEGORY to EventCategory.AFFILIATE_HOME_NOTIFICATION_CENTER,
            EventKey.KEY_EVENT_ACTION to EventAction.CLICK_EDUCATION_LIHAT_SEMUA,
            EventKey.KEY_EVENT_LABEL to "",
            EventKey.KEY_TRACKER_ID to Tracker.EDUCATION_SEE_MORE_CLICK,
            EventKey.KEY_BUSINESS_UNIT to BUSINESS_UNIT,
            EventKey.KEY_CURRENT_SITE to CURRENT_SITE,
            EventKey.KEY_USER_ID to userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun trackAffiliateEducationClick(
        userId: String
    ) {
        val data = mapOf(
            EventKey.KEY_EVENT to Event.CLICK_CONTENT,
            EventKey.KEY_EVENT_CATEGORY to EventCategory.AFFILIATE_HOME_NOTIFICATION_CENTER,
            EventKey.KEY_EVENT_ACTION to EventAction.CLICK_EDUCATION_SECTION,
            EventKey.KEY_EVENT_LABEL to "",
            EventKey.KEY_TRACKER_ID to Tracker.EDUCATION_CLICK,
            EventKey.KEY_BUSINESS_UNIT to BUSINESS_UNIT,
            EventKey.KEY_CURRENT_SITE to CURRENT_SITE,
            EventKey.KEY_USER_ID to userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }
}
