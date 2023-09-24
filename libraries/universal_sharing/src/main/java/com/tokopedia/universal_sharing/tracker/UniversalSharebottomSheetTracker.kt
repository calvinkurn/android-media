package com.tokopedia.universal_sharing.tracker

import android.os.Bundle
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class UniversalSharebottomSheetTracker @Inject constructor(private val userSession: UserSessionInterface) {

    companion object {
        private const val EVENT_BUSINESS_UNIT = "businessUnit"
        private const val EVENT_CURRENT_SITE = "currentSite"
        private const val EVENT_PROMOTIONS = "promotions"
        private const val EVENT_USER_ID = "userId"
        private const val TRACKER_ID = "trackerId"
        private const val SHOP_ID = "shopId"

        private const val EVENT_CREATIVE_NAME = "creative_name"
        private const val EVENT_CREATIVE_SLOT = "creative_slot"
        private const val EVENT_ITEM_ID = "item_id"
        private const val EVENT_ITEM_NAME = "item_name"

        private const val VALUE_ACTION_CLICK_AFFILIATE = "click - ticker affiliate"
        private const val VALUE_ACTION_IMPRESSION_AFFILIATE = "impression - ticker affiliate"
        private const val VALUE_ACTION_CLICK_SHARE = "click - share button"
        private const val VALUE_ACTION_CLOSE_SHARE = "click - close share bottom sheet"
        private const val VALUE_ACTION_VIEW_SHARE = "view on sharing channel"
        private const val VALUE_ACTION_CLICK_CHANNEL = "click - sharing channel"
        private const val VALUE_ACTION_VIEW_PRODUCT_LIST = "view on share product list sheet"
        private const val VALUE_ACTION_CLICK_CLOSE_SHARE_PRODUCT_LIST = "click - close share product list sheet"

        private const val VALUE_CATEGORY_THANKYOU = "share - thank you page"

        private const val VALUE_EVENT_VIEW = "view_item"
        private const val VALUE_EVENT_CLICK = "clickCommunication"
        private const val VALUE_EVENT_VIEW_COMMUNICATION = "viewCommunicationIris"
        private const val VALUE_CURRENT_SITE = "tokopediamarketplace"
        private const val VALUE_BUSINESS_UNIT = "sharingexperience"

        private const val VALUE_TRACKER_ID_VIEW_AFFILIATE = "36616"
        private const val VALUE_TRACKER_ID_CLICK_AFFILIATE = "36617"
        private const val VALUE_TRACKER_ID_45899 = "45899"
        private const val VALUE_TRACKER_ID_45898 = "45898"
        private const val VALUE_TRACKER_ID_45900 = "45900"
        private const val VALUE_TRACKER_ID_45901 = "45901"
        private const val VALUE_TRACKER_ID_46219 = "46219"
        private const val VALUE_TRACKER_ID_46220 = "46220"

        private const val TICKER_TYPE_AFFILIATE = "is_affiliate"
        private const val TICKER_TYPE_NON_AFFILIATE = "non_affiliate"
        private const val NOT_SET = "notset"
    }

    fun trackClickShare(id: String, eventCategory: String, trackerId: String, currentSite: String) {
        trackShare(id, VALUE_EVENT_CLICK, eventCategory, VALUE_ACTION_CLICK_SHARE, trackerId, currentSite)
    }

    fun trackCloseShare(id: String, eventCategory: String, trackerId: String, currentSite: String) {
        trackShare(id, VALUE_EVENT_CLICK, eventCategory, VALUE_ACTION_CLOSE_SHARE, trackerId, currentSite)
    }

    fun trackClickShareChannel(id: String, channel: String, imageType: String, eventCategory: String, trackerId: String, currentSite: String) {
        trackShare("$channel - $id - $imageType", VALUE_EVENT_CLICK, eventCategory, VALUE_ACTION_CLICK_CHANNEL, trackerId, currentSite)
    }

    fun trackViewShare(id: String, eventCategory: String, trackerId: String, currentSite: String) {
        trackShare(id, VALUE_EVENT_VIEW_COMMUNICATION, eventCategory, VALUE_ACTION_VIEW_SHARE, trackerId, currentSite)
    }

    private fun trackShare(eventLabel: String, event: String, eventCategory: String, eventAction: String, trackerId: String, currentSite: String) {
        val data = TrackAppUtils.gtmData(event, eventCategory, eventAction, eventLabel)
        data.apply {
            this[EVENT_BUSINESS_UNIT] = VALUE_BUSINESS_UNIT
            this[EVENT_CURRENT_SITE] = currentSite
            this[TRACKER_ID] = trackerId
        }
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun viewOnAffiliateRegisterTicker(isAffiliate: Boolean, id: String, page: String) {
        val data = generateDefaultTracker(VALUE_EVENT_VIEW, VALUE_ACTION_IMPRESSION_AFFILIATE, isAffiliate, id, getCategory(page))
        if (page == PageType.SHOP.value) {
            data[TRACKER_ID] = VALUE_TRACKER_ID_VIEW_AFFILIATE
            data[SHOP_ID] = id
        }
        val bundle = Bundle().apply {
            data.onEach { map ->
                if (map.value is String) {
                    putString(map.key, map.value as String)
                }
            }
        }
        bundle.putParcelableArrayList(EVENT_PROMOTIONS, generatePromotions())
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(VALUE_EVENT_VIEW, bundle)
    }

    fun onClickRegisterTicker(isAffiliate: Boolean, id: String, page: String) {
        val data = generateDefaultTracker(VALUE_EVENT_CLICK, VALUE_ACTION_CLICK_AFFILIATE, isAffiliate, id, getCategory(page))

        if (page == PageType.SHOP.value) {
            data[TRACKER_ID] = VALUE_TRACKER_ID_CLICK_AFFILIATE
            data[SHOP_ID] = id
        }

        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun onClickShareProductPostPurchase(
        userShareType: String,
        productId: String,
        orderId: String,
        orderStatus: String
    ) {
        trackShare(
            eventLabel = "$userShareType - $productId - $orderId - $orderStatus",
            event = VALUE_EVENT_CLICK,
            eventCategory = VALUE_CATEGORY_THANKYOU,
            eventAction = VALUE_ACTION_CLICK_SHARE,
            trackerId = VALUE_TRACKER_ID_45898,
            currentSite = VALUE_CURRENT_SITE
        )
    }

    fun onClickCloseBottomSheetSharePostPurchase(
        userShareType: String,
        productId: String,
        orderId: String,
        orderStatus: String
    ) {
        trackShare(
            eventLabel = "$userShareType - $productId - $orderId - $orderStatus",
            event = VALUE_EVENT_CLICK,
            eventCategory = VALUE_CATEGORY_THANKYOU,
            eventAction = VALUE_ACTION_CLOSE_SHARE,
            trackerId = VALUE_TRACKER_ID_45899,
            currentSite = VALUE_CURRENT_SITE
        )
    }

    fun onClickSharingChannelBottomSheetSharePostPurchase(
        channel: String,
        userShareType: String,
        productId: String,
        orderId: String,
        orderStatus: String
    ) {
        trackShare(
            eventLabel = "$channel - $userShareType - $productId - $orderId - $orderStatus",
            event = VALUE_EVENT_CLICK,
            eventCategory = VALUE_CATEGORY_THANKYOU,
            eventAction = VALUE_ACTION_CLICK_CHANNEL,
            trackerId = VALUE_TRACKER_ID_45900,
            currentSite = VALUE_CURRENT_SITE
        )
    }

    fun onViewSharingChannelBottomSheetSharePostPurchase(
        userShareType: String,
        productId: String,
        orderId: String,
        orderStatus: String
    ) {
        trackShare(
            eventLabel = "$userShareType - $productId - $orderId - $orderStatus",
            event = VALUE_EVENT_VIEW_COMMUNICATION,
            eventCategory = VALUE_CATEGORY_THANKYOU,
            eventAction = VALUE_ACTION_VIEW_SHARE,
            trackerId = VALUE_TRACKER_ID_45901,
            currentSite = VALUE_CURRENT_SITE
        )
    }

    fun onViewProductListPostPurchase(
        userShareType: String,
        productIdList: String,
        orderIdList: String,
        orderStatus: String
    ) {
        trackShare(
            eventLabel = "$userShareType - $productIdList - $orderIdList - $orderStatus",
            event = VALUE_EVENT_VIEW_COMMUNICATION,
            eventCategory = VALUE_CATEGORY_THANKYOU,
            eventAction = VALUE_ACTION_VIEW_PRODUCT_LIST,
            trackerId = VALUE_TRACKER_ID_46219,
            currentSite = VALUE_CURRENT_SITE
        )
    }

    fun onClickCloseProductListPostPurchase(
        userShareType: String,
        orderIdList: String,
        orderStatus: String
    ) {
        trackShare(
            eventLabel = "$userShareType - $orderIdList - $orderStatus",
            event = VALUE_EVENT_CLICK,
            eventCategory = VALUE_CATEGORY_THANKYOU,
            eventAction = VALUE_ACTION_CLICK_CLOSE_SHARE_PRODUCT_LIST,
            trackerId = VALUE_TRACKER_ID_46220,
            currentSite = VALUE_CURRENT_SITE
        )
    }

    private fun getTickerType(isAffiliate: Boolean): String {
        return if (isAffiliate) {
            TICKER_TYPE_AFFILIATE
        } else {
            TICKER_TYPE_NON_AFFILIATE
        }
    }

    private fun generatePromotions(): ArrayList<Bundle> {
        return arrayListOf(
            Bundle().apply {
                putString(EVENT_CREATIVE_NAME, NOT_SET)
                putString(EVENT_CREATIVE_SLOT, "1")
                putString(EVENT_ITEM_ID, "")
                putString(EVENT_ITEM_NAME, "")
            },
            Bundle().apply {
                putString(EVENT_CREATIVE_NAME, NOT_SET)
                putString(EVENT_CREATIVE_SLOT, "2")
                putString(EVENT_ITEM_ID, "")
                putString(EVENT_ITEM_NAME, "")
            }
        )
    }

    private fun getCategory(page: String): String {
        return when (page) {
            PageType.PDP.value -> EventCategoryAffiliate.PDP.value
            PageType.SHOP.value -> EventCategoryAffiliate.SHOP_PAGE.value
            else -> ""
        }
    }

    private fun generateDefaultTracker(event: String, eventAction: String, isAffiliate: Boolean, id: String, category: String): MutableMap<String, Any> {
        val data = TrackAppUtils.gtmData(event, category, eventAction, "${getTickerType(isAffiliate)} - $id - ${Int.ZERO}")
        return data.apply {
            this[EVENT_BUSINESS_UNIT] = VALUE_BUSINESS_UNIT
            this[EVENT_CURRENT_SITE] = VALUE_CURRENT_SITE
            this[EVENT_USER_ID] = userSession.userId
        }
    }
}
