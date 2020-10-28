package com.tokopedia.shop.review.analytic

import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.ContextAnalytics
import java.util.*

/**
 * Created by zulfikarrahman on 3/13/18.
 */
class ReputationTracking {
    private val tracker: ContextAnalytics
    private fun eventShopPageOfficialStore(action: String, label: String?, shopId: String?, myShop: Boolean) {
        val eventMap = createEventMap(ReputationTrackingConstant.CLICK_OFFICIAL_STORE, getEventCategory(myShop),
                action, label)
        eventMap[ReputationTrackingConstant.SHOP_ID] = shopId
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    private fun getEventCategory(myShop: Boolean): String? {
        return if (myShop) {
            ReputationTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER
        } else {
            ReputationTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BRAND
        }
    }

    private fun eventShopPageOfficialStoreProductId(action: String, label: String, productId: String?, myShop: Boolean) {
        val eventMap = createEventMap(ReputationTrackingConstant.CLICK_OFFICIAL_STORE, getEventCategory(myShop),
                action, label)
        eventMap[ReputationTrackingConstant.PRODUCT_ID] = productId
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    private fun createEventMap(event: String?, category: String?, action: String?, label: String?): HashMap<String?, Any?> {
        val eventMap = HashMap<String?, Any?>()
        eventMap[ReputationTrackingConstant.EVENT] = event
        eventMap[ReputationTrackingConstant.EVENT_CATEGORY] = category
        eventMap[ReputationTrackingConstant.EVENT_ACTION] = action
        eventMap[ReputationTrackingConstant.EVENT_LABEL] = label
        return eventMap
    }

    fun eventClickUserAccountPage(titlePage: String, position: Int, shopId: String?, myShop: Boolean) {
        eventShopPageOfficialStore(
                titlePage + ReputationTrackingConstant.TOP_CONTENT_REVIEW_PAGE_CLICK,
                ReputationTrackingConstant.CLICK_USER_ACCOUNT + (position + 1).toString(),
                shopId, myShop)
    }

    fun eventClickLikeDislikeReviewPage(titlePage: String?, status: Boolean, position: Int, shopId: String?, myShop: Boolean) {
        eventShopPageOfficialStore(String.format(ReputationTrackingConstant.TOP_CONTENT_PAGE_CLICK, titlePage, ReputationTrackingConstant.REVIEW),
                ReputationTrackingConstant.CLICK_REVIEW + (if (status) ReputationTrackingConstant.NEUTRAL else ReputationTrackingConstant.HELPING) + "-" + (position + 1).toString(),
                shopId, myShop)
    }

    fun eventClickProductPictureOrNamePage(titlePage: String?, position: Int, productId: String?, myShop: Boolean) {
        eventShopPageOfficialStoreProductId(String.format(ReputationTrackingConstant.TOP_CONTENT_PAGE_CLICK, titlePage, ReputationTrackingConstant.REVIEW),
                ReputationTrackingConstant.CLICK_PRODUCT_PICTURE_OR_NAME + (position + 1).toString(),
                productId, myShop)
    }

    fun eventCLickThreeDotMenuPage(titlePage: String?, position: Int, shopId: String?, myShop: Boolean) {
        eventShopPageOfficialStore(String.format(ReputationTrackingConstant.TOP_CONTENT_PAGE_CLICK, titlePage, ReputationTrackingConstant.REVIEW),
                ReputationTrackingConstant.CLICK_THREE_DOTTED + (position + 1).toString(),
                shopId, myShop)
    }

    fun eventClickChooseThreeDotMenuPage(titlePage: String?, position: Int, report: String?, shopId: String?, myShop: Boolean) {
        eventShopPageOfficialStore(String.format(ReputationTrackingConstant.TOP_CONTENT_PAGE_DOTTED_MENU_CLICK, titlePage, ReputationTrackingConstant.REVIEW),
                ReputationTrackingConstant.CLICK_THREE_DOTTED + (position + 1).toString(),
                shopId, myShop)
    }

    fun eventClickSeeRepliesPage(titlePage: String?, position: Int, shopId: String?, myShop: Boolean) {
        eventShopPageOfficialStore(String.format(ReputationTrackingConstant.TOP_CONTENT_PAGE_DOTTED_MENU_CLICK, titlePage, ReputationTrackingConstant.REVIEW),
                ReputationTrackingConstant.CLICK_SEE_REPLIES + (position + 1).toString(),
                shopId, myShop)
    }

    init {
        tracker = TrackApp.getInstance().gtm
    }
}