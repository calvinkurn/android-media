package com.tokopedia.review_shop.analytic

import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.ContextAnalytics
import java.util.*

/**
 * Created by zulfikarrahman on 3/13/18.
 */
class ReputationTracking {
    private val tracker: ContextAnalytics
    private fun eventShopPageOfficialStore(action: String, label: String?, shopId: String?, myShop: Boolean) {
        val eventMap = createEventMap(com.tokopedia.review_shop.analytic.ReputationTrackingConstant.CLICK_OFFICIAL_STORE, getEventCategory(myShop),
                action, label)
        eventMap[com.tokopedia.review_shop.analytic.ReputationTrackingConstant.SHOP_ID] = shopId
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    private fun getEventCategory(myShop: Boolean): String? {
        return if (myShop) {
            com.tokopedia.review_shop.analytic.ReputationTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BUYER
        } else {
            com.tokopedia.review_shop.analytic.ReputationTrackingConstant.OFFICIAL_STORE_SHOP_PAGE_BRAND
        }
    }

    private fun eventShopPageOfficialStoreProductId(action: String, label: String, productId: String?, myShop: Boolean) {
        val eventMap = createEventMap(com.tokopedia.review_shop.analytic.ReputationTrackingConstant.CLICK_OFFICIAL_STORE, getEventCategory(myShop),
                action, label)
        eventMap[com.tokopedia.review_shop.analytic.ReputationTrackingConstant.PRODUCT_ID] = productId
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    private fun createEventMap(event: String?, category: String?, action: String?, label: String?): HashMap<String?, Any?> {
        val eventMap = HashMap<String?, Any?>()
        eventMap[com.tokopedia.review_shop.analytic.ReputationTrackingConstant.EVENT] = event
        eventMap[com.tokopedia.review_shop.analytic.ReputationTrackingConstant.EVENT_CATEGORY] = category
        eventMap[com.tokopedia.review_shop.analytic.ReputationTrackingConstant.EVENT_ACTION] = action
        eventMap[com.tokopedia.review_shop.analytic.ReputationTrackingConstant.EVENT_LABEL] = label
        return eventMap
    }

    fun eventClickUserAccountPage(titlePage: String, position: Int, shopId: String?, myShop: Boolean) {
        eventShopPageOfficialStore(
                titlePage + com.tokopedia.review_shop.analytic.ReputationTrackingConstant.TOP_CONTENT_REVIEW_PAGE_CLICK,
                com.tokopedia.review_shop.analytic.ReputationTrackingConstant.CLICK_USER_ACCOUNT + (position + 1).toString(),
                shopId, myShop)
    }

    fun eventClickLikeDislikeReviewPage(titlePage: String?, status: Boolean, position: Int, shopId: String?, myShop: Boolean) {
        eventShopPageOfficialStore(String.format(com.tokopedia.review_shop.analytic.ReputationTrackingConstant.TOP_CONTENT_PAGE_CLICK, titlePage, com.tokopedia.review_shop.analytic.ReputationTrackingConstant.REVIEW),
                com.tokopedia.review_shop.analytic.ReputationTrackingConstant.CLICK_REVIEW + (if (status) com.tokopedia.review_shop.analytic.ReputationTrackingConstant.NEUTRAL else com.tokopedia.review_shop.analytic.ReputationTrackingConstant.HELPING) + "-" + (position + 1).toString(),
                shopId, myShop)
    }

    fun eventClickProductPictureOrNamePage(titlePage: String?, position: Int, productId: String?, myShop: Boolean) {
        eventShopPageOfficialStoreProductId(String.format(com.tokopedia.review_shop.analytic.ReputationTrackingConstant.TOP_CONTENT_PAGE_CLICK, titlePage, com.tokopedia.review_shop.analytic.ReputationTrackingConstant.REVIEW),
                com.tokopedia.review_shop.analytic.ReputationTrackingConstant.CLICK_PRODUCT_PICTURE_OR_NAME + (position + 1).toString(),
                productId, myShop)
    }

    fun eventCLickThreeDotMenuPage(titlePage: String?, position: Int, shopId: String?, myShop: Boolean) {
        eventShopPageOfficialStore(String.format(com.tokopedia.review_shop.analytic.ReputationTrackingConstant.TOP_CONTENT_PAGE_CLICK, titlePage, com.tokopedia.review_shop.analytic.ReputationTrackingConstant.REVIEW),
                com.tokopedia.review_shop.analytic.ReputationTrackingConstant.CLICK_THREE_DOTTED + (position + 1).toString(),
                shopId, myShop)
    }

    fun eventClickChooseThreeDotMenuPage(titlePage: String?, position: Int, report: String?, shopId: String?, myShop: Boolean) {
        eventShopPageOfficialStore(String.format(com.tokopedia.review_shop.analytic.ReputationTrackingConstant.TOP_CONTENT_PAGE_DOTTED_MENU_CLICK, titlePage, com.tokopedia.review_shop.analytic.ReputationTrackingConstant.REVIEW),
                com.tokopedia.review_shop.analytic.ReputationTrackingConstant.CLICK_THREE_DOTTED + (position + 1).toString(),
                shopId, myShop)
    }

    fun eventClickSeeRepliesPage(titlePage: String?, position: Int, shopId: String?, myShop: Boolean) {
        eventShopPageOfficialStore(String.format(com.tokopedia.review_shop.analytic.ReputationTrackingConstant.TOP_CONTENT_PAGE_DOTTED_MENU_CLICK, titlePage, com.tokopedia.review_shop.analytic.ReputationTrackingConstant.REVIEW),
                com.tokopedia.review_shop.analytic.ReputationTrackingConstant.CLICK_SEE_REPLIES + (position + 1).toString(),
                shopId, myShop)
    }

    init {
        tracker = TrackApp.getInstance().gtm
    }
}