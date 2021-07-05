package com.tokopedia.shop.analytic

import android.app.Activity
import android.text.TextUtils
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.shop.analytic.model.CustomDimensionShopPageAttribution
import com.tokopedia.shop.analytic.model.CustomDimensionShopPageProduct
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue
import java.util.*

open class OldShopPageTrackingUser(
        protected val trackingQueue: TrackingQueue) {
    private fun sendScreenName(activity: Activity, screenName: String, customDimensionShopPage: CustomDimensionShopPage) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName,
                customDimensionShopPage.shopId, customDimensionShopPage.shopType, SHOPPAGE, null)
    }

    protected fun sendDataLayerEvent(eventTracking: Map<String, Any>) {
        if (eventTracking.containsKey(OldShopPageTrackingConstant.ECOMMERCE)) {
            trackingQueue.putEETracking(eventTracking as HashMap<String, Any>)
        } else {
            TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(eventTracking)
        }
    }

    protected fun sendEvent(event: String?, category: String?, action: String?, label: String?,
                            customDimensionShopPage: CustomDimensionShopPage?) {
        val eventMap = createMap(event, category, action, label, customDimensionShopPage)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(eventMap)
    }

    fun sendAllTrackingQueue() {
        trackingQueue.sendAll()
    }

    private fun createMvcListMap(viewModelList: List<MerchantVoucherViewModel>, shopId: String, startIndex: Int): List<Any> {
        val list: MutableList<Any> = ArrayList()
        for (i in viewModelList.indices) {
            val position = startIndex + i + 1
            val viewModel = viewModelList[i]
            if (viewModel.isAvailable()) {
                list.add(
                        DataLayer.mapOf(
                                OldShopPageTrackingConstant.ID, shopId,
                                OldShopPageTrackingConstant.NAME, joinDash(SHOP_PAGE, position.toString(), viewModel.voucherName),
                                OldShopPageTrackingConstant.POSITION, position,
                                OldShopPageTrackingConstant.CREATIVE, "",
                                OldShopPageTrackingConstant.PROMO_ID, viewModel.voucherId,  //optional
                                OldShopPageTrackingConstant.PROMO_CODE, viewModel.voucherCode //optional
                        )
                )
            }
        }
        return if (list.size == 0) {
            ArrayList()
        } else DataLayer.listOf(*list.toTypedArray())
    }

    protected fun createMap(event: String?, category: String?, action: String?, label: String?,
                            customDimensionShopPage: CustomDimensionShopPage?): HashMap<String?, Any?> {
        val eventMap = HashMap<String?, Any?>()
        eventMap[OldShopPageTrackingConstant.EVENT] = event
        eventMap[OldShopPageTrackingConstant.EVENT_CATEGORY] = category
        eventMap[OldShopPageTrackingConstant.EVENT_ACTION] = action
        eventMap[OldShopPageTrackingConstant.EVENT_LABEL] = label
        if (customDimensionShopPage != null) {
            addCustomDimension(eventMap, customDimensionShopPage)
            if (customDimensionShopPage is CustomDimensionShopPageProduct) {
                eventMap[OldShopPageTrackingConstant.PRODUCT_ID] = customDimensionShopPage.productId
            }
            if (customDimensionShopPage is CustomDimensionShopPageAttribution) {
                eventMap[OldShopPageTrackingConstant.ATTRIBUTION] = customDimensionShopPage.attribution
            }
        }
        return eventMap
    }

    private fun shopPageBuyerOrSeller(isOwner: Boolean): String? {
        return if (isOwner) {
            OldShopPageTrackingConstant.SHOP_PAGE_SELLER
        } else {
            OldShopPageTrackingConstant.SHOP_PAGE_BUYER
        }
    }

    private fun addCustomDimension(eventMap: HashMap<String?, Any?>,
                                   customDimensionShopPage: CustomDimensionShopPage) {
        eventMap[OldShopPageTrackingConstant.SHOP_ID] = customDimensionShopPage.shopId
        eventMap[OldShopPageTrackingConstant.SHOP_TYPE] = customDimensionShopPage.shopType
        eventMap[OldShopPageTrackingConstant.PAGE_TYPE] = SHOPPAGE
    }

    protected fun joinDash(vararg s: String?): String {
        return TextUtils.join(" - ", s)
    }

    protected fun joinSpace(vararg s: String?): String {
        return TextUtils.join(" ", s)
    }

    fun sendScreenShopPage(activity: Activity, customDimensionShopPage: CustomDimensionShopPage) {
        sendScreenName(activity, joinDash(SHOPPAGE, customDimensionShopPage.shopId), customDimensionShopPage)
    }

    fun clickManageShop(customDimensionShopPage: CustomDimensionShopPage?) {
        sendEvent(OldShopPageTrackingConstant.CLICK_SHOP_PAGE,
                OldShopPageTrackingConstant.SHOP_PAGE_SELLER,
                joinDash(OldShopPageTrackingConstant.MANAGE_SHOP, OldShopPageTrackingConstant.CLICK),
                OldShopPageTrackingConstant.CLICK_MANAGE_SHOP,
                customDimensionShopPage)
    }

    fun clickAddProduct(customDimensionShopPage: CustomDimensionShopPage?) {
        sendEvent(OldShopPageTrackingConstant.CLICK_SHOP_PAGE,
                OldShopPageTrackingConstant.SHOP_PAGE_SELLER,
                joinDash(OldShopPageTrackingConstant.MANAGE_SHOP, OldShopPageTrackingConstant.CLICK),
                OldShopPageTrackingConstant.CLICK_ADD_PRODUCT,
                customDimensionShopPage)
    }

    fun sendOpenShop() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                "clickManageShop",
                "Manage Shop",
                "Click",
                "Shop Info"
        )
    }

    fun clickFollowerList(isOwner: Boolean,
                          customDimensionShopPage: CustomDimensionShopPage?) {
        sendEvent(OldShopPageTrackingConstant.CLICK_SHOP_PAGE,
                shopPageBuyerOrSeller(isOwner),
                joinDash(OldShopPageTrackingConstant.TOP_SECTION, OldShopPageTrackingConstant.CLICK),
                OldShopPageTrackingConstant.CLICK_FOLLOWER_LIST,
                customDimensionShopPage)
    }

    fun clickShareButton(isOwner: Boolean,
                         customDimensionShopPage: CustomDimensionShopPage?) {
        sendEvent(OldShopPageTrackingConstant.CLICK_SHOP_PAGE,
                shopPageBuyerOrSeller(isOwner),
                joinDash(OldShopPageTrackingConstant.TOP_SECTION, OldShopPageTrackingConstant.CLICK),
                OldShopPageTrackingConstant.CLICK_SHARE_BUTTON,
                customDimensionShopPage)
    }

    fun clickCartButton(isOwner: Boolean,
                        customDimensionShopPage: CustomDimensionShopPage?) {
        sendEvent(OldShopPageTrackingConstant.CLICK_SHOP_PAGE,
                shopPageBuyerOrSeller(isOwner),
                joinDash(OldShopPageTrackingConstant.TOP_SECTION, OldShopPageTrackingConstant.CLICK),
                OldShopPageTrackingConstant.CLICK_CART_BUTTON,
                customDimensionShopPage)
    }

    fun clickTab(isOwner: Boolean,
                 tabName: String?,
                 customDimensionShopPage: CustomDimensionShopPage?) {
        sendEvent(OldShopPageTrackingConstant.CLICK_SHOP_PAGE,
                shopPageBuyerOrSeller(isOwner),
                joinDash(OldShopPageTrackingConstant.TOP_SECTION, OldShopPageTrackingConstant.CLICK),
                joinSpace(OldShopPageTrackingConstant.CLICK_TAB, tabName),
                customDimensionShopPage)
    }

    fun clickRequestOpenShop(customDimensionShopPage: CustomDimensionShopPage?) {
        sendEvent(OldShopPageTrackingConstant.CLICK_SHOP_PAGE,
                OldShopPageTrackingConstant.SHOP_PAGE_SELLER,
                joinDash(OldShopPageTrackingConstant.MANAGE_SHOP, OldShopPageTrackingConstant.CLICK),
                OldShopPageTrackingConstant.CLICK_REQUEST_OPEN_SHOP,
                customDimensionShopPage)
    }

    fun impressionRequestOpenShop(customDimensionShopPage: CustomDimensionShopPage?) {
        sendEvent(OldShopPageTrackingConstant.VIEW_SHOP_PAGE,
                OldShopPageTrackingConstant.SHOP_PAGE_SELLER,
                joinDash(OldShopPageTrackingConstant.MANAGE_SHOP, OldShopPageTrackingConstant.IMPRESSION),
                OldShopPageTrackingConstant.IMPRESSION_OF_REQUEST_OPEN_SHOP,
                customDimensionShopPage)
    }

    fun impressionOpenOperationalShop(customDimensionShopPage: CustomDimensionShopPage?) {
        sendEvent(OldShopPageTrackingConstant.VIEW_SHOP_PAGE,
                OldShopPageTrackingConstant.SHOP_PAGE_SELLER,
                joinDash(OldShopPageTrackingConstant.MANAGE_SHOP, OldShopPageTrackingConstant.IMPRESSION),
                OldShopPageTrackingConstant.IMPRESSION_OPEN_OPERATIONAL_SHOP,
                customDimensionShopPage)
    }

    fun clickOpenOperationalShop(customDimensionShopPage: CustomDimensionShopPage?) {
        sendEvent(OldShopPageTrackingConstant.CLICK_SHOP_PAGE,
                OldShopPageTrackingConstant.SHOP_PAGE_SELLER,
                joinDash(OldShopPageTrackingConstant.MANAGE_SHOP, OldShopPageTrackingConstant.CLICK),
                OldShopPageTrackingConstant.CLICK_OPEN_OPERATIONAL_SHOP,
                customDimensionShopPage)
    }

    fun impressionHowToActivateShop(customDimensionShopPage: CustomDimensionShopPage?) {
        sendEvent(OldShopPageTrackingConstant.VIEW_SHOP_PAGE,
                OldShopPageTrackingConstant.SHOP_PAGE_SELLER,
                joinDash(OldShopPageTrackingConstant.MANAGE_SHOP, OldShopPageTrackingConstant.IMPRESSION),
                OldShopPageTrackingConstant.IMPRESSION_HOW_TO_ACTIVATE_SHOP,
                customDimensionShopPage)
    }

    fun clickHowToActivateShop(customDimensionShopPage: CustomDimensionShopPage?) {
        sendEvent(OldShopPageTrackingConstant.CLICK_SHOP_PAGE,
                OldShopPageTrackingConstant.SHOP_PAGE_SELLER,
                joinDash(OldShopPageTrackingConstant.MANAGE_SHOP, OldShopPageTrackingConstant.CLICK),
                OldShopPageTrackingConstant.CLICK_HOW_TO_ACTIVATE_SHOP,
                customDimensionShopPage)
    }

    fun searchKeyword(isOwner: Boolean,
                      keyword: String?,
                      hasResult: Boolean,
                      customDimensionShopPage: CustomDimensionShopPage?) {
        sendEvent(OldShopPageTrackingConstant.CLICK_SHOP_PAGE,
                shopPageBuyerOrSeller(isOwner),
                joinDash(OldShopPageTrackingConstant.SEARCH_BAR, OldShopPageTrackingConstant.CLICK),
                joinDash(joinSpace(OldShopPageTrackingConstant.SEARCH, keyword), if (hasResult) OldShopPageTrackingConstant.SEARCH_RESULT else OldShopPageTrackingConstant.NO_SEARCH_RESULT),
                customDimensionShopPage)
    }

    fun clickEtalaseChip(isOwner: Boolean,
                         etalaseName: String?,
                         customDimensionShopPage: CustomDimensionShopPage?) {
        sendEvent(OldShopPageTrackingConstant.CLICK_SHOP_PAGE,
                shopPageBuyerOrSeller(isOwner),
                joinDash(OldShopPageTrackingConstant.PRODUCT_NAVIGATION, OldShopPageTrackingConstant.CLICK),
                joinDash(OldShopPageTrackingConstant.CLICK_MENU, etalaseName),
                customDimensionShopPage)
    }

    fun clickMoreMenuChip(isOwner: Boolean,
                          customDimensionShopPage: CustomDimensionShopPage?) {
        sendEvent(OldShopPageTrackingConstant.CLICK_SHOP_PAGE,
                shopPageBuyerOrSeller(isOwner),
                joinDash(OldShopPageTrackingConstant.PRODUCT_NAVIGATION, OldShopPageTrackingConstant.CLICK),
                OldShopPageTrackingConstant.CLICK_MORE_MENU,
                customDimensionShopPage)
    }

    fun clickMenuFromMoreMenu(isOwner: Boolean,
                              etalaseName: String?,
                              customDimensionShopPage: CustomDimensionShopPage?) {
        sendEvent(OldShopPageTrackingConstant.CLICK_SHOP_PAGE,
                shopPageBuyerOrSeller(isOwner),
                joinDash(OldShopPageTrackingConstant.PRODUCT_NAVIGATION, OldShopPageTrackingConstant.CLICK),
                joinDash(OldShopPageTrackingConstant.CLICK_MENU_FROM_MORE_MENU, etalaseName),
                customDimensionShopPage)
    }

    fun clickSort(isOwner: Boolean,
                  customDimensionShopPage: CustomDimensionShopPage?) {
        sendEvent(OldShopPageTrackingConstant.CLICK_SHOP_PAGE,
                shopPageBuyerOrSeller(isOwner),
                joinDash(OldShopPageTrackingConstant.PRODUCT_NAVIGATION, OldShopPageTrackingConstant.CLICK),
                OldShopPageTrackingConstant.CLICK_SORT,
                customDimensionShopPage)
    }

    fun clickSortBy(isOwner: Boolean,
                    sortName: String?,
                    customDimensionShopPage: CustomDimensionShopPage?) {
        sendEvent(OldShopPageTrackingConstant.CLICK_SHOP_PAGE,
                shopPageBuyerOrSeller(isOwner),
                joinDash(OldShopPageTrackingConstant.PRODUCT_NAVIGATION, OldShopPageTrackingConstant.CLICK),
                joinDash(OldShopPageTrackingConstant.CLICK_SORT_BY, sortName),
                customDimensionShopPage)
    }

    fun clickZeroProduct(customDimensionShopPage: CustomDimensionShopPage?) {
        sendEvent(OldShopPageTrackingConstant.CLICK_SHOP_PAGE,
                OldShopPageTrackingConstant.SHOP_PAGE_SELLER,
                joinDash(OldShopPageTrackingConstant.MANAGE_PRODUCT, OldShopPageTrackingConstant.CLICK),
                OldShopPageTrackingConstant.CLICK_ADD_PRODUCT_FROM_ZERO_PRODUCT,
                customDimensionShopPage)
    }

    fun impressionZeroProduct(customDimensionShopPage: CustomDimensionShopPage?) {
        sendEvent(OldShopPageTrackingConstant.VIEW_SHOP_PAGE,
                OldShopPageTrackingConstant.SHOP_PAGE_SELLER,
                joinDash(OldShopPageTrackingConstant.MANAGE_PRODUCT, OldShopPageTrackingConstant.IMPRESSION),
                OldShopPageTrackingConstant.IMPRESSION_ADD_PRODUCT_FROM_ZERO_PRODUCT,
                customDimensionShopPage)
    }

    fun clickReadNotes(isOwner: Boolean, noteRowIndex: Int,
                       customDimensionShopPage: CustomDimensionShopPage?) {
        sendEvent(OldShopPageTrackingConstant.CLICK_SHOP_PAGE,
                shopPageBuyerOrSeller(isOwner),
                joinDash(OldShopPageTrackingConstant.INFO, OldShopPageTrackingConstant.CLICK),
                joinDash(OldShopPageTrackingConstant.CLICK_READ_NOTES, (noteRowIndex + 1).toString()),
                customDimensionShopPage)
    }

    fun clickReview(isOwner: Boolean,
                    customDimensionShopPage: CustomDimensionShopPage?) {
        sendEvent(OldShopPageTrackingConstant.CLICK_SHOP_PAGE,
                shopPageBuyerOrSeller(isOwner),
                joinDash(OldShopPageTrackingConstant.INFO, OldShopPageTrackingConstant.CLICK),
                OldShopPageTrackingConstant.CLICK_REVIEW,
                customDimensionShopPage)
    }

    fun clickDiscussion(isOwner: Boolean,
                        customDimensionShopPage: CustomDimensionShopPage?) {
        sendEvent(OldShopPageTrackingConstant.CLICK_SHOP_PAGE,
                shopPageBuyerOrSeller(isOwner),
                joinDash(OldShopPageTrackingConstant.INFO, OldShopPageTrackingConstant.CLICK),
                OldShopPageTrackingConstant.CLICK_DISCUSSION,
                customDimensionShopPage)
    }

    fun clickAddNote(customDimensionShopPage: CustomDimensionShopPage?) {
        sendEvent(OldShopPageTrackingConstant.CLICK_SHOP_PAGE,
                OldShopPageTrackingConstant.SHOP_PAGE_SELLER,
                joinDash(OldShopPageTrackingConstant.INFO, OldShopPageTrackingConstant.CLICK),
                OldShopPageTrackingConstant.CLICK_ADD_NOTE,
                customDimensionShopPage)
    }

    fun clickSeeAllMerchantVoucher(isOwner: Boolean) {
        val eventMap: MutableMap<String?, Any?> = HashMap()
        eventMap[OldShopPageTrackingConstant.EVENT] = OldShopPageTrackingConstant.CLICK_SHOP_PAGE
        eventMap[OldShopPageTrackingConstant.EVENT_CATEGORY] = shopPageBuyerOrSeller(isOwner)
        eventMap[OldShopPageTrackingConstant.EVENT_ACTION] = joinDash(OldShopPageTrackingConstant.CLICK, OldShopPageTrackingConstant.MERCHANT_VOUCHER, OldShopPageTrackingConstant.SEE_ALL)
        eventMap[OldShopPageTrackingConstant.EVENT_LABEL] = ""
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(eventMap)
    }

    fun clickDetailMerchantVoucher(isOwner: Boolean, voucherId: String?) {
        val eventMap: MutableMap<String?, Any?> = HashMap()
        eventMap[OldShopPageTrackingConstant.EVENT] = OldShopPageTrackingConstant.CLICK_SHOP_PAGE
        eventMap[OldShopPageTrackingConstant.EVENT_CATEGORY] = shopPageBuyerOrSeller(isOwner)
        eventMap[OldShopPageTrackingConstant.EVENT_ACTION] = joinDash(OldShopPageTrackingConstant.CLICK, OldShopPageTrackingConstant.MERCHANT_VOUCHER, OldShopPageTrackingConstant.MVC_DETAIL)
        eventMap[OldShopPageTrackingConstant.EVENT_LABEL] = ""
        eventMap[OldShopPageTrackingConstant.EVENT_PROMO_ID] = voucherId
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(eventMap)
    }

    fun clickReviewMore(shopId: String, myShop: Boolean) {
        val eventMap = HashMap<String, Any>()
        eventMap["event"] = "clickOfficialStore"
        eventMap["eventCategory"] = getEventReputationCategory(myShop)
        eventMap["eventAction"] = "Ulasan - bottom navigation - click"
        eventMap["eventLabel"] = "click see more"
        eventMap["shop_id"] = shopId
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    private fun getEventReputationCategory(myShop: Boolean): String {
        return if (myShop) {
            "official store shop page - buyer"
        } else {
            "official store shop page - brand"
        }
    }

    companion object {
        const val SHOPPAGE = "/shoppage"
        const val SHOP_PAGE = "Shop page"
    }
}