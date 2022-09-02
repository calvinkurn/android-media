package com.tokopedia.universal_sharing.tracker

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.user.session.UserSession

class UniversalSharebottomSheetTracker (private val userSession: UserSession) {

    companion object {
        private const val EVENT_CATEGORY = "eventCategory"
        private const val EVENT_ACTION = "eventAction"
        private const val EVENT_LABEL = "eventLabel"
        private const val EVENT_BUSINESS_UNIT = "businessUnit"
        private const val EVENT_CURRENT_SITE = "currentSite"
        private const val EVENT_PROMOTIONS = ""
        private const val EVENT_USER_ID = "userId"

        private const val EVENT_CREATIVE_NAME = "creative_name"
        private const val EVENT_CREATIVE_SLOT = "creative_slot"
        private const val EVENT_ITEM_ID = "item_id"
        private const val EVENT_ITEM_NAME = "item_name"

        private const val VALUE_ACTION_CLICK_AFFILIATE = "click - ticker affiliate"
        private const val VALUE_ACTION_IMPRESSION_AFFILIATE = "impression - ticker affiliate"
        private const val VALUE_EVENT_VIEW = "view_item"
        private const val VALUE_EVENT_CLICK = "clickCommunication"
        private const val VALUE_CATEGORY = "product detail page"
        private const val VALUE_CURRENT_SITE = "tokopediamarketplace"
        private const val VALUE_BUSINESS_UNIT = "sharingexperience"

        private const val TICKER_TYPE_AFFILIATE = "is_affiliate"
        private const val TICKER_TYPE_NON_AFFILIATE = "non_affiliate"
        private const val NOT_SET = "notset"
    }


    fun viewOnAffiliateRegisterTicker(isAffiliate: Boolean, productId: String) {
        val data = generateDefaultTracker(VALUE_EVENT_VIEW, VALUE_ACTION_IMPRESSION_AFFILIATE, isAffiliate, productId).apply {
            this[EVENT_PROMOTIONS] = generatePromotions()
        }
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun onClickRegisterTicker(isAffiliate: Boolean, productId: String) {
        val data = generateDefaultTracker(VALUE_EVENT_CLICK, VALUE_ACTION_CLICK_AFFILIATE, isAffiliate, productId)
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    private fun getTickerType(isAffiliate: Boolean): String {
        return if (isAffiliate) TICKER_TYPE_AFFILIATE
        else TICKER_TYPE_NON_AFFILIATE
    }

    private fun generatePromotions(): List<Map<String, Any>> {
        return listOf(mapOf(EVENT_CREATIVE_NAME to NOT_SET, EVENT_CREATIVE_SLOT to NOT_SET, EVENT_ITEM_ID to NOT_SET, EVENT_ITEM_NAME to NOT_SET))
    }

    private fun generateDefaultTracker(event: String, eventAction: String, isAffiliate: Boolean, productId: String): MutableMap<String, Any> {
        val data = TrackAppUtils.gtmData(event, VALUE_CATEGORY, eventAction, "${getTickerType(isAffiliate)} - $productId")
        return data.apply {
            this[EVENT_BUSINESS_UNIT] = VALUE_BUSINESS_UNIT
            this[EVENT_CURRENT_SITE] = VALUE_CURRENT_SITE
            this[EVENT_USER_ID] = userSession.userId
        }
    }
}