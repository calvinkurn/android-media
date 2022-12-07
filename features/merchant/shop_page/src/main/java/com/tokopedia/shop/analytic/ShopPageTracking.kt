package com.tokopedia.shop.analytic

import android.os.Bundle
import android.text.TextUtils
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.linker.LinkerConstants
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_AFFILIATE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_NOT_AFFILIATE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_SHOP_PAGE_OPEN_SCREEN
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.shop.analytic.model.CustomDimensionShopPageProduct
import com.tokopedia.shop.common.data.model.ShopAffiliateData
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopHeaderWidgetUiModel.WidgetType.SHOP_ACTION
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopHeaderWidgetUiModel.WidgetType.SHOP_BASIC_INFO
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopHeaderWidgetUiModel.WidgetType.SHOP_PERFORMANCE
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopHeaderWidgetUiModel.WidgetType.SHOP_PLAY
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue
import java.util.*

open class ShopPageTracking(
    protected val trackingQueue: TrackingQueue
) {

    protected fun sendEnhanceEcommerceDataLayerEvent(eventName: String, bundle: Bundle) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(eventName, bundle)
    }

    protected fun sendDataLayerEvent(eventTracking: Map<String, Any>) {
        if (eventTracking.containsKey(ShopPageTrackingConstant.ECOMMERCE)) {
            trackingQueue.putEETracking(eventTracking as HashMap<String, Any>)
        } else {
            TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(eventTracking)
        }
    }

    protected fun sendEvent(
        event: String?,
        category: String?,
        action: String?,
        label: String?,
        customDimensionShopPage: CustomDimensionShopPage?
    ) {
        val eventMap = createMap(event, category, action, label, customDimensionShopPage)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(eventMap)
    }

    protected fun sendGeneralEvent(
        event: String?,
        category: String?,
        action: String?,
        label: String?,
        customDimensionShopPage: CustomDimensionShopPage?
    ) {
        val eventMap = createMap(event, category, action, label, customDimensionShopPage)
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    protected fun sendGeneralEventNplFollower(
        event: String?,
        category: String?,
        action: String?,
        label: String?,
        businessUnit: String?,
        currentSite: String?,
        userId: String?,
        customDimensionShopPage: CustomDimensionShopPage?
    ) {
        val eventMap = createMap(
            event,
            category,
            action,
            label,
            customDimensionShopPage
        )
        eventMap[ShopPageTrackingConstant.BUSINESS_UNIT] = businessUnit.orEmpty()
        eventMap[ShopPageTrackingConstant.CURRENT_SITE] = currentSite.orEmpty()
        eventMap[ShopPageTrackingConstant.USER_ID] = userId.orEmpty()
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
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
                        ShopPageTrackingConstant.ID, shopId,
                        ShopPageTrackingConstant.NAME, joinDash(SHOP_PAGE, position.toString(), viewModel.voucherName),
                        ShopPageTrackingConstant.POSITION, position,
                        ShopPageTrackingConstant.CREATIVE, "",
                        ShopPageTrackingConstant.PROMO_ID, viewModel.voucherId, // optional
                        ShopPageTrackingConstant.PROMO_CODE, viewModel.voucherCode // optional
                    )
                )
            }
        }
        return if (list.size == 0) {
            ArrayList()
        } else DataLayer.listOf(*list.toTypedArray())
    }

    protected fun createMap(
        event: String?,
        category: String?,
        action: String?,
        label: String?,
        customDimensionShopPage: CustomDimensionShopPage?
    ): HashMap<String, Any> {
        val eventMap = HashMap<String, Any>()
        eventMap[ShopPageTrackingConstant.EVENT] = event.orEmpty()
        eventMap[ShopPageTrackingConstant.EVENT_CATEGORY] = category.orEmpty()
        eventMap[ShopPageTrackingConstant.EVENT_ACTION] = action.orEmpty()
        eventMap[ShopPageTrackingConstant.EVENT_LABEL] = label.orEmpty()
        if (customDimensionShopPage != null) {
            addCustomDimension(eventMap, customDimensionShopPage)
            if (customDimensionShopPage is CustomDimensionShopPageProduct) {
                eventMap[ShopPageTrackingConstant.PRODUCT_ID] = customDimensionShopPage.productId.orEmpty()
                eventMap[ShopPageTrackingConstant.DIMENSION_90] = customDimensionShopPage.shopRef
            }
        }
        return eventMap
    }

    protected fun getShopPageCategory(isOwner: Boolean): String {
        return if (isOwner) {
            ShopPageTrackingConstant.SHOP_PAGE_SELLER
        } else {
            ShopPageTrackingConstant.SHOP_PAGE_BUYER
        }
    }

    private fun addCustomDimension(
        eventMap: HashMap<String, Any>,
        customDimensionShopPage: CustomDimensionShopPage
    ) {
        eventMap[ShopPageTrackingConstant.SHOP_ID] = customDimensionShopPage.shopId.orEmpty()
        eventMap[ShopPageTrackingConstant.SHOP_TYPE] = customDimensionShopPage.shopType.orEmpty()
        eventMap[ShopPageTrackingConstant.PAGE_TYPE] = SHOPPAGE
    }

    protected fun joinDash(vararg s: String?): String {
        return TextUtils.join(" - ", s)
    }

    protected fun joinSpace(vararg s: String?): String {
        return TextUtils.join(" ", s)
    }

    fun sendScreenShopPage(
        shopId: String,
        isLogin: Boolean,
        selectedTabName: String,
        campaignId: String,
        variantId: String,
        affiliateData: ShopAffiliateData?
    ) {
        val screenName = joinDash(SHOPPAGE, shopId)
        val loginNonLoginEventValue = if (isLogin) ShopPageTrackingConstant.LOGIN else ShopPageTrackingConstant.NON_LOGIN
        val shopAffiliateStatus = getShopAffiliateStatus(affiliateData?.affiliateUUId.orEmpty())
        val affiliateTrackerId = getShopAffiliateTrackerId(affiliateData)
        val pageSource = String.format(ShopPageTrackingConstant.FIRST_LANDING_PAGE, selectedTabName, shopAffiliateStatus)
        val customDimension: MutableMap<String, String> = HashMap()
        customDimension[ShopPageTrackingConstant.PAGE_TYPE] = SHOPPAGE
        customDimension[ShopPageTrackingConstant.BUSINESS_UNIT] = ShopPageTrackingConstant.PHYSICAL_GOODS
        customDimension[ShopPageTrackingConstant.CURRENT_SITE] = ShopPageTrackingConstant.TOKOPEDIA_MARKETPLACE
        customDimension[ShopPageTrackingConstant.IS_LOGGED_IN_STATUS] = loginNonLoginEventValue
        customDimension[ShopPageTrackingConstant.PAGE_SOURCE] = pageSource
        customDimension[ShopPageTrackingConstant.SHOP_ID] = shopId
        customDimension[ShopPageTrackingConstant.Key.CAMPAIGN_ID] = campaignId
        customDimension[ShopPageTrackingConstant.Key.VARIANT_ID] = variantId
        customDimension[ShopPageTrackingConstant.TRACKER_ID] = TRACKER_SHOP_PAGE_OPEN_SCREEN
        customDimension[ShopPageTrackingConstant.Key.AFFILIATE_CHANNEL_ID] = affiliateTrackerId
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName, customDimension)
    }

    fun sendBranchScreenShop(userId: String) {
        LinkerManager.getInstance().sendEvent(LinkerUtils.createGenericRequest(LinkerConstants.ENUM_EVENT_PAGE_VIEW_STORE, userId))
    }

    private fun getShopAffiliateTrackerId(affiliateData: ShopAffiliateData?): String {
        return if (affiliateData?.affiliateUUId?.isNotEmpty() == true) {
            joinDash(
                affiliateData.affiliateUUId,
                affiliateData.affiliateTrackerId
            )
        } else {
            ""
        }
    }

    private fun getShopAffiliateStatus(affiliateUuid: String): String {
        return if (affiliateUuid.isNotEmpty()) {
            SHOP_AFFILIATE
        } else {
            SHOP_NOT_AFFILIATE
        }
    }

    fun sendOpenScreenAddProduct(shopId: String?, shopType: String) {
        val screenName = joinDash(SHOPPAGE, shopId)
        val customDimension: MutableMap<String, String> = HashMap()
        customDimension[ShopPageTrackingConstant.SHOP_TYPE] = shopType
        customDimension[ShopPageTrackingConstant.PAGE_TYPE] = SHOPPAGE
        customDimension[ShopPageTrackingConstant.PAGE_SOURCE] =
            ShopPageTrackingConstant.SCREEN_ADD_PRODUCT
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName, customDimension)
    }

    fun clickManageShop(customDimensionShopPage: CustomDimensionShopPage?) {
        sendEvent(
            ShopPageTrackingConstant.CLICK_SHOP_PAGE,
            ShopPageTrackingConstant.SHOP_PAGE_SELLER,
            joinDash(ShopPageTrackingConstant.MANAGE_SHOP, ShopPageTrackingConstant.CLICK),
            ShopPageTrackingConstant.CLICK_MANAGE_SHOP,
            customDimensionShopPage
        )
    }

    fun clickAddProduct(customDimensionShopPage: CustomDimensionShopPage?) {
        sendGeneralEvent(
            ShopPageTrackingConstant.CLICK_SHOP_PAGE,
            ShopPageTrackingConstant.SHOP_PAGE_SELLER,
            ShopPageTrackingConstant.CLICK_ADD_PRODUCT,
            "",
            customDimensionShopPage
        )
    }

    open fun clickBackArrow(isMyShop: Boolean, customDimensionShopPage: CustomDimensionShopPage?) {
        sendGeneralEvent(
            ShopPageTrackingConstant.CLICK_SHOP_PAGE,
            getShopPageCategory(isMyShop),
            ShopPageTrackingConstant.CLICK_BACK,
            "",
            customDimensionShopPage
        )
    }

    fun sendOpenShop() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            "clickManageShop",
            "Manage Shop",
            "Click",
            "Shop Info"
        )
    }

    fun clickCartButton(
        isOwner: Boolean,
        customDimensionShopPage: CustomDimensionShopPage?
    ) {
        sendGeneralEvent(
            ShopPageTrackingConstant.CLICK_SHOP_PAGE,
            getShopPageCategory(isOwner),
            ShopPageTrackingConstant.CLICK_CART_BUTTON,
            "",
            customDimensionShopPage
        )
    }

    fun clickTab(
        tabName: String,
        shopId: String,
        userId: String
    ) {
        val eventLabel = String.format(ShopPageTrackingConstant.LABEL_CLICK_TAB, tabName)
        val eventMap: MutableMap<String, Any> = mutableMapOf(
            ShopPageTrackingConstant.EVENT to ShopPageTrackingConstant.CLICK_SHOP_PAGE,
            ShopPageTrackingConstant.EVENT_ACTION to ShopPageTrackingConstant.CLICK_TAB,
            ShopPageTrackingConstant.EVENT_CATEGORY to ShopPageTrackingConstant.SHOP_PAGE_BUYER,
            ShopPageTrackingConstant.EVENT_LABEL to eventLabel,
            ShopPageTrackingConstant.BUSINESS_UNIT to ShopPageTrackingConstant.PHYSICAL_GOODS,
            ShopPageTrackingConstant.CURRENT_SITE to ShopPageTrackingConstant.TOKOPEDIA_MARKETPLACE,
            ShopPageTrackingConstant.SHOP_ID to shopId,
            ShopPageTrackingConstant.USER_ID to userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    fun clickRequestOpenShop(customDimensionShopPage: CustomDimensionShopPage?) {
        sendEvent(
            ShopPageTrackingConstant.CLICK_SHOP_PAGE,
            ShopPageTrackingConstant.SHOP_PAGE_SELLER,
            joinDash(ShopPageTrackingConstant.MANAGE_SHOP, ShopPageTrackingConstant.CLICK),
            ShopPageTrackingConstant.CLICK_REQUEST_OPEN_SHOP,
            customDimensionShopPage
        )
    }

    fun impressionRequestOpenShop(customDimensionShopPage: CustomDimensionShopPage?) {
        sendEvent(
            ShopPageTrackingConstant.VIEW_SHOP_PAGE,
            ShopPageTrackingConstant.SHOP_PAGE_SELLER,
            joinDash(ShopPageTrackingConstant.MANAGE_SHOP, ShopPageTrackingConstant.IMPRESSION),
            ShopPageTrackingConstant.IMPRESSION_OF_REQUEST_OPEN_SHOP,
            customDimensionShopPage
        )
    }

    fun impressionOpenOperationalShop(customDimensionShopPage: CustomDimensionShopPage?) {
        sendEvent(
            ShopPageTrackingConstant.VIEW_SHOP_PAGE,
            ShopPageTrackingConstant.SHOP_PAGE_SELLER,
            joinDash(ShopPageTrackingConstant.MANAGE_SHOP, ShopPageTrackingConstant.IMPRESSION),
            ShopPageTrackingConstant.IMPRESSION_OPEN_OPERATIONAL_SHOP,
            customDimensionShopPage
        )
    }

    fun clickOpenOperationalShop(customDimensionShopPage: CustomDimensionShopPage?) {
        sendEvent(
            ShopPageTrackingConstant.CLICK_SHOP_PAGE,
            ShopPageTrackingConstant.SHOP_PAGE_SELLER,
            joinDash(ShopPageTrackingConstant.MANAGE_SHOP, ShopPageTrackingConstant.CLICK),
            ShopPageTrackingConstant.CLICK_OPEN_OPERATIONAL_SHOP,
            customDimensionShopPage
        )
    }

    fun impressionHowToActivateShop(customDimensionShopPage: CustomDimensionShopPage?) {
        sendEvent(
            ShopPageTrackingConstant.VIEW_SHOP_PAGE,
            ShopPageTrackingConstant.SHOP_PAGE_SELLER,
            joinDash(ShopPageTrackingConstant.MANAGE_SHOP, ShopPageTrackingConstant.IMPRESSION),
            ShopPageTrackingConstant.IMPRESSION_HOW_TO_ACTIVATE_SHOP,
            customDimensionShopPage
        )
    }

    fun clickHowToActivateShop(customDimensionShopPage: CustomDimensionShopPage?) {
        sendEvent(
            ShopPageTrackingConstant.CLICK_SHOP_PAGE,
            ShopPageTrackingConstant.SHOP_PAGE_SELLER,
            joinDash(ShopPageTrackingConstant.MANAGE_SHOP, ShopPageTrackingConstant.CLICK),
            ShopPageTrackingConstant.CLICK_HOW_TO_ACTIVATE_SHOP,
            customDimensionShopPage
        )
    }

    fun clickEtalaseChip(
        tabName: String,
        shopId: String,
        userId: String
    ) {
        val eventLabel = String.format(ShopPageTrackingConstant.LABEL_CLICK_SHOWCASE_CHIP, tabName)
        val eventMap: MutableMap<String, Any> = mutableMapOf(
            ShopPageTrackingConstant.EVENT to ShopPageTrackingConstant.CLICK_SHOP_PAGE,
            ShopPageTrackingConstant.EVENT_ACTION to ShopPageTrackingConstant.ACTION_CLICK_SHOWCASE_CHIP,
            ShopPageTrackingConstant.EVENT_CATEGORY to ShopPageTrackingConstant.SHOP_PAGE_BUYER,
            ShopPageTrackingConstant.EVENT_LABEL to eventLabel,
            ShopPageTrackingConstant.BUSINESS_UNIT to ShopPageTrackingConstant.PHYSICAL_GOODS,
            ShopPageTrackingConstant.CURRENT_SITE to ShopPageTrackingConstant.TOKOPEDIA_MARKETPLACE,
            ShopPageTrackingConstant.SHOP_ID to shopId,
            ShopPageTrackingConstant.USER_ID to userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    fun clickMenuFromMoreMenu(
        isOwner: Boolean,
        etalaseName: String?,
        customDimensionShopPage: CustomDimensionShopPage?
    ) {
        sendEvent(
            ShopPageTrackingConstant.CLICK_SHOP_PAGE,
            getShopPageCategory(isOwner),
            joinDash(ShopPageTrackingConstant.PRODUCT_NAVIGATION, ShopPageTrackingConstant.CLICK),
            joinDash(ShopPageTrackingConstant.CLICK_MENU_FROM_MORE_MENU, etalaseName),
            customDimensionShopPage
        )
    }

    fun clickSort(
        isOwner: Boolean,
        customDimensionShopPage: CustomDimensionShopPage?
    ) {
        sendGeneralEvent(
            ShopPageTrackingConstant.CLICK_SHOP_PAGE,
            getShopPageCategory(isOwner),
            ShopPageTrackingConstant.CLICK_SORT,
            "",
            customDimensionShopPage
        )
    }

    fun clickSortBy(
        isOwner: Boolean,
        sortName: String?,
        customDimensionShopPage: CustomDimensionShopPage?
    ) {
        sendGeneralEvent(
            ShopPageTrackingConstant.CLICK_SHOP_PAGE,
            getShopPageCategory(isOwner),
            ShopPageTrackingConstant.SORT_PRODUCT,
            String.format(ShopPageTrackingConstant.CLICK_SORT_BY, sortName),
            customDimensionShopPage
        )
    }

    fun clickHighLightSeeAll(customDimensionShopPage: CustomDimensionShopPage?) {
        sendGeneralEvent(
            ShopPageTrackingConstant.CLICK_SHOP_PAGE,
            ShopPageTrackingConstant.SHOP_PAGE_BUYER,
            ShopPageTrackingConstant.CLICK_VIEW_ALL,
            "",
            customDimensionShopPage
        )
    }

    fun impressionZeroProduct(customDimensionShopPage: CustomDimensionShopPage?) {
        sendEvent(
            ShopPageTrackingConstant.VIEW_SHOP_PAGE,
            ShopPageTrackingConstant.SHOP_PAGE_SELLER,
            joinDash(ShopPageTrackingConstant.MANAGE_PRODUCT, ShopPageTrackingConstant.IMPRESSION),
            ShopPageTrackingConstant.IMPRESSION_ADD_PRODUCT_FROM_ZERO_PRODUCT,
            customDimensionShopPage
        )
    }

    fun clickReadNotes(
        shopId: String,
        userId: String
    ) {
        val eventMap: MutableMap<String, Any> = mutableMapOf(
            ShopPageTrackingConstant.EVENT to ShopPageTrackingConstant.CLICK_SHOP_PAGE,
            ShopPageTrackingConstant.EVENT_ACTION to ShopPageTrackingConstant.CLICK_GLOBAL_HEADER,
            ShopPageTrackingConstant.EVENT_CATEGORY to ShopPageTrackingConstant.SHOP_PAGE_BUYER,
            ShopPageTrackingConstant.EVENT_LABEL to ShopPageTrackingConstant.LABEL_CLICK_GLOBAL_HEADER_SHOP_NOTES,
            ShopPageTrackingConstant.BUSINESS_UNIT to ShopPageTrackingConstant.PHYSICAL_GOODS,
            ShopPageTrackingConstant.CURRENT_SITE to ShopPageTrackingConstant.TOKOPEDIA_MARKETPLACE,
            ShopPageTrackingConstant.SHOP_ID to shopId,
            ShopPageTrackingConstant.USER_ID to userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    fun clickReview(
        isOwner: Boolean,
        customDimensionShopPage: CustomDimensionShopPage?
    ) {
        sendEvent(
            ShopPageTrackingConstant.CLICK_SHOP_PAGE,
            getShopPageCategory(isOwner),
            joinDash(ShopPageTrackingConstant.INFO, ShopPageTrackingConstant.CLICK),
            ShopPageTrackingConstant.CLICK_REVIEW,
            customDimensionShopPage
        )
    }

    fun clickDiscussion(
        isOwner: Boolean,
        customDimensionShopPage: CustomDimensionShopPage?
    ) {
        sendEvent(
            ShopPageTrackingConstant.CLICK_SHOP_PAGE,
            getShopPageCategory(isOwner),
            joinDash(ShopPageTrackingConstant.INFO, ShopPageTrackingConstant.CLICK),
            ShopPageTrackingConstant.CLICK_DISCUSSION,
            customDimensionShopPage
        )
    }

    fun clickAddNote(customDimensionShopPage: CustomDimensionShopPage?) {
        sendEvent(
            ShopPageTrackingConstant.CLICK_SHOP_PAGE,
            ShopPageTrackingConstant.SHOP_PAGE_SELLER,
            joinDash(ShopPageTrackingConstant.INFO, ShopPageTrackingConstant.CLICK),
            ShopPageTrackingConstant.CLICK_ADD_NOTE,
            customDimensionShopPage
        )
    }

    fun clickSeeAllMerchantVoucher(isOwner: Boolean) {
        val eventMap: MutableMap<String, Any> = HashMap()
        eventMap[ShopPageTrackingConstant.EVENT] = ShopPageTrackingConstant.CLICK_SHOP_PAGE
        eventMap[ShopPageTrackingConstant.EVENT_CATEGORY] = getShopPageCategory(isOwner)
        eventMap[ShopPageTrackingConstant.EVENT_ACTION] = joinDash(ShopPageTrackingConstant.CLICK, ShopPageTrackingConstant.MERCHANT_VOUCHER, ShopPageTrackingConstant.SEE_ALL)
        eventMap[ShopPageTrackingConstant.EVENT_LABEL] = ""
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(eventMap)
    }

    fun clickDetailMerchantVoucher(isOwner: Boolean, voucherId: String) {
        val eventMap: MutableMap<String, Any> = HashMap()
        eventMap[ShopPageTrackingConstant.EVENT] = ShopPageTrackingConstant.CLICK_SHOP_PAGE
        eventMap[ShopPageTrackingConstant.EVENT_CATEGORY] = getShopPageCategory(isOwner)
        eventMap[ShopPageTrackingConstant.EVENT_ACTION] = joinDash(ShopPageTrackingConstant.CLICK, ShopPageTrackingConstant.MERCHANT_VOUCHER, ShopPageTrackingConstant.MVC_DETAIL)
        eventMap[ShopPageTrackingConstant.EVENT_LABEL] = ""
        eventMap[ShopPageTrackingConstant.EVENT_PROMO_ID] = voucherId
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(eventMap)
    }

    fun clickUseMerchantVoucher(isOwner: Boolean, viewModel: MerchantVoucherViewModel, shopId: String, positionIndex: Int) {
        if (isOwner) return
        val viewModelList = ArrayList<MerchantVoucherViewModel>()
        viewModelList.add(viewModel)
        if (viewModelList.isEmpty()) return
        val eventMap = HashMap<String, Any>()
        eventMap[ShopPageTrackingConstant.EVENT] = ShopPageTrackingConstant.PROMO_CLICK
        eventMap[ShopPageTrackingConstant.EVENT_CATEGORY] = ShopPageTrackingConstant.SHOP_PAGE_BUYER
        eventMap[ShopPageTrackingConstant.EVENT_ACTION] = joinDash(ShopPageTrackingConstant.CLICK, ShopPageTrackingConstant.MERCHANT_VOUCHER, ShopPageTrackingConstant.USE_VOUCHER)
        eventMap[ShopPageTrackingConstant.EVENT_LABEL] = ""
        eventMap[ShopPageTrackingConstant.EVENT_PROMO_ID] = viewModel.voucherId.toString()
        eventMap[ShopPageTrackingConstant.ECOMMERCE] = DataLayer.mapOf(
            ShopPageTrackingConstant.PROMO_CLICK,
            DataLayer.mapOf(
                ShopPageTrackingConstant.PROMOTIONS,
                createMvcListMap(viewModelList, shopId, positionIndex)
            )
        )
        sendDataLayerEvent(eventMap)
    }

    fun impressionUseMerchantVoucher(isOwner: Boolean, merchantVoucherViewModelList: List<MerchantVoucherViewModel>, shopId: String) {
        if (isOwner || merchantVoucherViewModelList.isEmpty()) return
        val index = 0
        val voucherId = merchantVoucherViewModelList[index].voucherId.toString()
        val eventMap = HashMap<String, Any>()
        eventMap[ShopPageTrackingConstant.EVENT] = ShopPageTrackingConstant.PROMO_VIEW
        eventMap[ShopPageTrackingConstant.EVENT_CATEGORY] = ShopPageTrackingConstant.SHOP_PAGE_BUYER
        eventMap[ShopPageTrackingConstant.EVENT_ACTION] = joinDash(ShopPageTrackingConstant.IMPRESSION, ShopPageTrackingConstant.MERCHANT_VOUCHER, ShopPageTrackingConstant.USE_VOUCHER)
        eventMap[ShopPageTrackingConstant.EVENT_LABEL] = ""
        eventMap[ShopPageTrackingConstant.EVENT_PROMO_ID] = voucherId
        eventMap[ShopPageTrackingConstant.ECOMMERCE] = DataLayer.mapOf(
            ShopPageTrackingConstant.PROMO_VIEW,
            DataLayer.mapOf(
                ShopPageTrackingConstant.PROMOTIONS,
                createMvcListMap(merchantVoucherViewModelList, shopId, index)
            )
        )
        sendDataLayerEvent(eventMap)
    }

    protected fun followUnfollowShop(
        event: String?,
        action: String?,
        label: String?,
        userId: String?
    ) {
        val eventMap = HashMap<String, Any>()
        eventMap[ShopPageTrackingConstant.EVENT] = event.orEmpty()
        eventMap[ShopPageTrackingConstant.EVENT_CATEGORY] = ShopPageTrackingConstant.SHOP_PAGE_BUYER
        eventMap[ShopPageTrackingConstant.EVENT_ACTION] = action.orEmpty()
        eventMap[ShopPageTrackingConstant.EVENT_LABEL] = label.orEmpty()
        eventMap[ShopPageTrackingConstant.BUSINESS_UNIT] = ShopPageTrackingConstant.PHYSICAL_GOODS
        eventMap[ShopPageTrackingConstant.CURRENT_SITE] = ShopPageTrackingConstant.TOKOPEDIA_MARKETPLACE
        eventMap[ShopPageTrackingConstant.USER_ID] = userId.orEmpty()
        sendDataLayerEvent(eventMap)
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

    fun clickSettingButton(customDimension: CustomDimensionShopPage?) {
        sendGeneralEvent(ShopPageTrackingConstant.CLICK_SHOP_PAGE, ShopPageTrackingConstant.SHOP_PAGE_SELLER, ShopPageTrackingConstant.CLICK_SETTING, "", customDimension)
    }

    fun sortProduct(sortName: String?, isOwner: Boolean, customDimensionShopPage: CustomDimensionShopPage?) {
        sendGeneralEvent(
            ShopPageTrackingConstant.CLICK_SHOP_PAGE,
            getShopPageCategory(isOwner),
            ShopPageTrackingConstant.SORT_PRODUCT,
            String.format(ShopPageTrackingConstant.CLICK_SORT_BY, sortName),
            customDimensionShopPage
        )
    }

    protected fun formatPrice(displayedPrice: String): String {
        var displayedPrice = displayedPrice
        return if (!TextUtils.isEmpty(displayedPrice)) {
            displayedPrice = displayedPrice.replace("[^\\d]".toRegex(), "")
            displayedPrice
        } else {
            ""
        }
    }

    fun clickShopHeaderComponent(
        isMyShop: Boolean,
        shopId: String?,
        userId: String?,
        valueDisplayed: String,
        componentId: String,
        componentName: String,
        headerId: String,
        headerType: String,
        componentPosition: Int,
        customDimensionShopPage: CustomDimensionShopPage?
    ) {
        val eventCategory = if (isMyShop) ShopPageTrackingConstant.SHOP_PAGE_SELLER else ShopPageTrackingConstant.SHOP_PAGE_BUYER
        val eventLabel: String
        eventLabel = if (isMyShop) {
            String.format(ShopPageTrackingConstant.LABEL_CLICK_SHOP_HEADER_SELLER, shopId)
        } else {
            String.format(ShopPageTrackingConstant.LABEL_CLICK_SHOP_HEADER_BUYER, shopId)
        }
        val eventMap = createMap(
            ShopPageTrackingConstant.PROMO_CLICK,
            eventCategory,
            ShopPageTrackingConstant.CLICK,
            eventLabel,
            customDimensionShopPage
        )
        eventMap[ShopPageTrackingConstant.BUSINESS_UNIT] = ShopPageTrackingConstant.PHYSICAL_GOODS
        eventMap[ShopPageTrackingConstant.CURRENT_SITE] = ShopPageTrackingConstant.TOKOPEDIA_MARKETPLACE
        eventMap[ShopPageTrackingConstant.USER_ID] = userId.orEmpty()
        val headerTrackerType = getShopHeaderTrackerType(headerType)
        eventMap[ShopPageTrackingConstant.ECOMMERCE] = DataLayer.mapOf(
            ShopPageTrackingConstant.PROMO_CLICK,
            DataLayer.mapOf(
                ShopPageTrackingConstant.PROMOTIONS,
                DataLayer.listOf(
                    createShopHeaderPromotionItemMap(
                        valueDisplayed,
                        componentId,
                        componentName,
                        headerId,
                        headerTrackerType,
                        componentPosition
                    )
                )
            )
        )
        sendDataLayerEvent(eventMap)
    }

    fun impressionShopHeaderComponent(
        isMyShop: Boolean,
        shopId: String?,
        userId: String?,
        valueDisplayed: String,
        componentId: String,
        componentName: String,
        headerId: String,
        headerType: String,
        componentPosition: Int,
        customDimensionShopPage: CustomDimensionShopPage?
    ) {
        val eventCategory = if (isMyShop) ShopPageTrackingConstant.SHOP_PAGE_SELLER else ShopPageTrackingConstant.SHOP_PAGE_BUYER
        val eventAction = if (isMyShop) ShopPageTrackingConstant.ACTION_IMPRESSION_SHOP_HEADER_SELLER else ShopPageTrackingConstant.ACTION_IMPRESSION_SHOP_HEADER_BUYER
        val eventLabel: String
        eventLabel = if (isMyShop) {
            String.format(ShopPageTrackingConstant.LABEL_IMPRESSION_SHOP_HEADER_SELLER, shopId)
        } else {
            String.format(ShopPageTrackingConstant.LABEL_IMPRESSION_SHOP_HEADER_BUYER, shopId)
        }
        val eventMap = createMap(
            ShopPageTrackingConstant.PROMO_VIEW,
            eventCategory,
            eventAction,
            eventLabel,
            customDimensionShopPage
        )
        eventMap[ShopPageTrackingConstant.BUSINESS_UNIT] = ShopPageTrackingConstant.PHYSICAL_GOODS
        eventMap[ShopPageTrackingConstant.CURRENT_SITE] = ShopPageTrackingConstant.TOKOPEDIA_MARKETPLACE
        eventMap[ShopPageTrackingConstant.USER_ID] = userId.orEmpty()
        val headerTrackerType = getShopHeaderTrackerType(headerType)
        eventMap[ShopPageTrackingConstant.ECOMMERCE] = DataLayer.mapOf(
            ShopPageTrackingConstant.PROMO_VIEW,
            DataLayer.mapOf(
                ShopPageTrackingConstant.PROMOTIONS,
                DataLayer.listOf(
                    createShopHeaderPromotionItemMap(
                        valueDisplayed,
                        componentId,
                        componentName,
                        headerId,
                        headerTrackerType,
                        componentPosition
                    )
                )
            )
        )
        sendDataLayerEvent(eventMap)
    }

    private fun getShopHeaderTrackerType(headerType: String): String {
        return when (headerType) {
            SHOP_BASIC_INFO -> ShopPageTrackingConstant.SHOP_HEADER_BASIC_INFO_TRACKER_TYPE
            SHOP_PERFORMANCE -> ShopPageTrackingConstant.SHOP_HEADER_PERFORMANCE_TRACKER_TYPE
            SHOP_ACTION -> ShopPageTrackingConstant.SHOP_HEADER_ACTION_TRACKER_TYPE
            SHOP_PLAY -> ShopPageTrackingConstant.SHOP_HEADER_PLAY_TRACKER_TYPE
            else -> ""
        }
    }

    private fun createShopHeaderPromotionItemMap(
        valueDisplayed: String,
        componentId: String,
        componentName: String,
        headerId: String,
        headerTrackerType: String,
        componentPosition: Int
    ): Map<String, Any> {
        val eventMap = HashMap<String, Any>()
        eventMap[ShopPageTrackingConstant.CREATIVE] = valueDisplayed
        eventMap[ShopPageTrackingConstant.ID] = componentId
        eventMap[ShopPageTrackingConstant.NAME] = joinDash(componentName, headerId, headerTrackerType)
        eventMap[ShopPageTrackingConstant.POSITION] = componentPosition
        return eventMap
    }

    fun sendImpressionShopTab(shopId: String, tabTitle: String) {
        val eventLabel = String.format(ShopPageTrackingConstant.IMPRESSION_TAB, tabTitle)
        val eventMap: MutableMap<String, Any> = mutableMapOf(
            ShopPageTrackingConstant.EVENT to ShopPageTrackingConstant.VIEW_SHOP_PAGE_IRIS,
            ShopPageTrackingConstant.EVENT_ACTION to ShopPageTrackingConstant.IMPRESSION_TAB_ICON,
            ShopPageTrackingConstant.EVENT_CATEGORY to ShopPageTrackingConstant.SHOP_PAGE_BUYER,
            ShopPageTrackingConstant.EVENT_LABEL to eventLabel,
            ShopPageTrackingConstant.BUSINESS_UNIT to ShopPageTrackingConstant.PHYSICAL_GOODS,
            ShopPageTrackingConstant.CURRENT_SITE to ShopPageTrackingConstant.TOKOPEDIA_MARKETPLACE,
            ShopPageTrackingConstant.SHOP_ID to shopId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    companion object {
        const val SHOPPAGE = "/shoppage"
        const val SHOP_PAGE = "Shop page"
    }
}
