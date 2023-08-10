package com.tokopedia.shop.analytic

import android.os.Bundle
import android.text.TextUtils
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.kotlin.extensions.view.digitsOnly
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.BUSINESS_UNIT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CATEGORY_ID
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_PG
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_SHARE_AFFILIATE_ICON
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_SHARE_REGULER
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CREATIVE_NAME
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CREATIVE_SLOT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CURRENT_SITE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.DIMENSION_45
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EVENT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EVENT_ACTION
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EVENT_CATEGORY
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EVENT_LABEL
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.Event.DIRECT_PURCHASE_ADD_TO_CART
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.Event.OPEN_SCREEN
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.ALL_PRODUCT_CLICKED
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.ALL_PRODUCT_IMPRESSION
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.CLICK_PRODUCT_ATC
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.CLICK_PRODUCT_ATC_QUANTITY
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.CLICK_PRODUCT_ATC_RESET
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.IMPRESSION_PRODUCT_ATC
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventCategory.SHOP_PAGE_BUYER_DIRECT_PURCHASE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.IS_LOGGED_IN_STATUS
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ITEMS
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ITEMS_SHOP_ID
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ITEMS_SHOP_TYPE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ITEM_BRAND
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ITEM_CATEGORY
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ITEM_ID
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ITEM_NAME
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ITEM_VARIANT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.NONE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.PHYSICAL_GOODS
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.PRICE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.PRODUCT_ID
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.PROMOTIONS
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.QUANTITY
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_ID
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_NAME
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_PAGE_BUYER
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_PRODUCT_ATC_QUANTITY_DECREASE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_PRODUCT_ATC_QUANTITY_INCREASE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TOKOPEDIA_MARKETPLACE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TRACKER_ID
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_ATC_CLICK
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_ATC_CLICK_DELETE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_ATC_CLICK_QUANTITY
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_OPEN_SCREEN_CAMPAIGN_TAB
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.USER_ID
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.VIEW_ITEM
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.shop.analytic.model.CustomDimensionShopPageAttribution
import com.tokopedia.shop.analytic.model.CustomDimensionShopPageProduct
import com.tokopedia.shop.common.constant.*
import com.tokopedia.shop.common.data.model.ShopPageAtcTracker
import com.tokopedia.shop.common.util.ShopProductViewGridType
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.product.view.datamodel.ShopProductUiModel
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue
import java.util.*
import kotlin.collections.HashMap

class ShopPageTrackingBuyer(
    trackingQueue: TrackingQueue?
) : ShopPageTracking(trackingQueue!!) {
    private fun createProductListMap(
        shopProductUiModelList: List<ShopProductUiModel>,
        selectedEtalaseName: String,
        etalaseName: String,
        productPosition: Int,
        shopTypeDef: String?,
        loginNonLoginString: String,
        shopId: String,
        shopRef: String,
        isFulfillmentExist: Boolean?,
        isFreeOngkirActive: Boolean?,
        sortAndFilterValue: String,
        listEventValue: String,
        selectedTabName: String
    ): List<Any> {
        val boe: String
        boe = if (isFulfillmentExist!! && isFreeOngkirActive!!) {
            ShopPageTrackingConstant.BOE
        } else if (!isFulfillmentExist && isFreeOngkirActive!!) {
            ShopPageTrackingConstant.BO_PRODUCT
        } else {
            ShopPageTrackingConstant.NON_BO_PRODUCT
        }
        val list: MutableList<Any> = ArrayList()
        for (i in shopProductUiModelList.indices) {
            val viewModel = shopProductUiModelList[i]
            val event = HashMap(
                DataLayer.mapOf(
                    ShopPageTrackingConstant.NAME, viewModel.name,
                    ShopPageTrackingConstant.ID, viewModel.id,
                    ShopPageTrackingConstant.PRICE, formatPrice(viewModel.displayedPrice!!),
                    ShopPageTrackingConstant.BRAND, ShopPageTrackingConstant.NONE,
                    ShopPageTrackingConstant.CATEGORY, ShopPageTrackingConstant.NONE,
                    ShopPageTrackingConstant.VARIANT, selectedTabName,
                    ShopPageTrackingConstant.LIST, listEventValue,
                    ShopPageTrackingConstant.POSITION, productPosition,
                    ShopPageTrackingConstant.DIMENSION_81, shopTypeDef,
                    ShopPageTrackingConstant.DIMENSION_79, shopId,
                    ShopPageTrackingConstant.DIMENSION_90, shopRef,
                    ShopPageTrackingConstant.DIMENSION_83, boe,
                    ShopPageTrackingConstant.DIMENSION_61, sortAndFilterValue
                )
            )
            list.add(event)
        }
        return list
    }

    private fun createProductListSearchResultMap(
        shopProductUiModelList: List<ShopProductUiModel>,
        selectedEtalaseName: String,
        etalaseName: String,
        productPosition: Int,
        shopTypeDef: String?,
        loginNonLoginString: String,
        shopId: String,
        dimension90: String,
        sortAndFilterValue: String,
        listEventValue: String,
        selectedTabName: String
    ): List<Any> {
        val list: MutableList<Any> = ArrayList()
        for (i in shopProductUiModelList.indices) {
            val viewModel = shopProductUiModelList[i]
            val event = HashMap(
                DataLayer.mapOf(
                    ShopPageTrackingConstant.NAME, viewModel.name,
                    ShopPageTrackingConstant.ID, viewModel.id,
                    ShopPageTrackingConstant.PRICE, formatPrice(viewModel.displayedPrice!!),
                    ShopPageTrackingConstant.BRAND, ShopPageTrackingConstant.NONE,
                    ShopPageTrackingConstant.CATEGORY, ShopPageTrackingConstant.NONE,
                    ShopPageTrackingConstant.VARIANT, selectedTabName,
                    ShopPageTrackingConstant.LIST, listEventValue,
                    ShopPageTrackingConstant.POSITION, productPosition,
                    ShopPageTrackingConstant.DIMENSION_81, shopTypeDef,
                    ShopPageTrackingConstant.DIMENSION_79, shopId,
                    ShopPageTrackingConstant.DIMENSION_90, dimension90,
                    ShopPageTrackingConstant.DIMENSION_61, sortAndFilterValue
                )
            )
            list.add(event)
        }
        return list
    }

    private fun createProductImpressionMap(
        event: String,
        category: String,
        loginNonLoginString: String,
        action: String,
        label: String,
        customDimensionShopPage: CustomDimensionShopPageAttribution,
        shopProductUiModel: ShopProductUiModel,
        selectedEtalaseChipName: String,
        etalaseName: String,
        productPositionStart: Int,
        shopId: String,
        sortAndFilterValue: String,
        listEventValue: String,
        selectedTabName: String
    ): HashMap<String, Any> {
        val shopProductUiModelArrayList = ArrayList<ShopProductUiModel>()
        shopProductUiModelArrayList.add(shopProductUiModel)
        val eventMap = createMap(event, category, action, label, customDimensionShopPage)
        eventMap[ShopPageTrackingConstant.ECOMMERCE] = DataLayer.mapOf(
            ShopPageTrackingConstant.CURRENCY_CODE,
            ShopPageTrackingConstant.IDR,
            ShopPageTrackingConstant.IMPRESSIONS,
            createProductListMap(
                shopProductUiModelArrayList,
                selectedEtalaseChipName,
                etalaseName,
                productPositionStart,
                customDimensionShopPage.shopType,
                loginNonLoginString,
                shopId,
                customDimensionShopPage.shopRef,
                customDimensionShopPage.isFulfillmentExist,
                customDimensionShopPage.isFreeOngkirActive,
                sortAndFilterValue,
                listEventValue,
                selectedTabName
            )
        )
        return eventMap
    }

    private fun createProductImpressionSearchResultMap(
        event: String,
        category: String,
        loginNonLoginString: String,
        action: String,
        label: String,
        customDimensionShopPage: CustomDimensionShopPageAttribution,
        shopProductUiModel: ShopProductUiModel,
        selectedEtalaseChipName: String,
        etalaseName: String,
        productPositionStart: Int,
        shopId: String,
        shopName: String,
        navSource: String,
        sortAndFilterValue: String,
        listEventValue: String,
        selectedTabName: String
    ): HashMap<String, Any> {
        val shopProductUiModelArrayList = ArrayList<ShopProductUiModel>()
        shopProductUiModelArrayList.add(shopProductUiModel)
        val eventMap = createMap(event, category, action, label, customDimensionShopPage)
        val dimension90Value = "$shopName.$navSource.local_search.$shopId"
        eventMap[ShopPageTrackingConstant.ECOMMERCE] = DataLayer.mapOf(
            ShopPageTrackingConstant.CURRENCY_CODE,
            ShopPageTrackingConstant.IDR,
            ShopPageTrackingConstant.IMPRESSIONS,
            createProductListSearchResultMap(
                shopProductUiModelArrayList,
                selectedEtalaseChipName,
                etalaseName,
                productPositionStart,
                customDimensionShopPage.shopType,
                loginNonLoginString,
                shopId,
                dimension90Value,
                sortAndFilterValue,
                listEventValue,
                selectedTabName
            )
        )
        return eventMap
    }

    private fun createProductClickMap(
        event: String,
        category: String,
        loginNonLoginString: String,
        action: String,
        label: String?,
        customDimensionShopPage: CustomDimensionShopPageAttribution,
        shopProductUiModel: ShopProductUiModel,
        selectedEtalaseChipName: String,
        etalaseName: String,
        productPositionStart: Int,
        shopId: String,
        sortAndFilterValue: String,
        listEventValue: String,
        selectedTabName: String
    ): HashMap<String, Any> {
        val shopProductUiModelArrayList = ArrayList<ShopProductUiModel>()
        shopProductUiModelArrayList.add(shopProductUiModel)
        val eventMap = createMap(event, category, action, label, customDimensionShopPage)
        eventMap[ShopPageTrackingConstant.ECOMMERCE] = DataLayer.mapOf(
            ShopPageTrackingConstant.CLICK,
            DataLayer.mapOf(
                ShopPageTrackingConstant.ACTION_FIELD,
                DataLayer.mapOf(ShopPageTrackingConstant.LIST, joinDash(SHOPPAGE, shopId, getProductEtalaseEvent(selectedEtalaseChipName, etalaseName), loginNonLoginString)),
                ShopPageTrackingConstant.PRODUCTS,
                createProductListMap(
                    shopProductUiModelArrayList,
                    selectedEtalaseChipName,
                    etalaseName,
                    productPositionStart,
                    customDimensionShopPage.shopType,
                    loginNonLoginString,
                    shopId,
                    customDimensionShopPage.shopRef,
                    customDimensionShopPage.isFulfillmentExist,
                    customDimensionShopPage.isFreeOngkirActive,
                    sortAndFilterValue,
                    listEventValue,
                    selectedTabName
                )
            )
        )
        return eventMap
    }

    private fun createProductClickSearchResultMap(
        event: String,
        category: String,
        loginNonLoginString: String,
        action: String,
        label: String,
        customDimensionShopPage: CustomDimensionShopPageAttribution,
        shopProductUiModel: ShopProductUiModel,
        selectedEtalaseChipName: String,
        etalaseName: String,
        productPositionStart: Int,
        shopId: String,
        shopName: String,
        navSource: String,
        sortAndFilterValue: String,
        listEventValue: String,
        selectedTabName: String
    ): HashMap<String, Any> {
        val shopProductUiModelArrayList = ArrayList<ShopProductUiModel>()
        shopProductUiModelArrayList.add(shopProductUiModel)
        val eventMap = createMap(event, category, action, label, customDimensionShopPage)
        val dimension90Value = "$shopName.$navSource.local_search.$shopId"
        eventMap[ShopPageTrackingConstant.ECOMMERCE] = DataLayer.mapOf(
            ShopPageTrackingConstant.CLICK,
            DataLayer.mapOf(
                ShopPageTrackingConstant.ACTION_FIELD,
                DataLayer.mapOf(
                    ShopPageTrackingConstant.LIST,
                    joinDash(
                        SHOPPAGE,
                        shopId,
                        getProductEtalaseEvent(
                            selectedEtalaseChipName,
                            etalaseName
                        ),
                        loginNonLoginString,
                        ShopPageTrackingConstant.SEARCH_RESULT
                    )
                ),
                ShopPageTrackingConstant.PRODUCTS,
                createProductListSearchResultMap(
                    shopProductUiModelArrayList,
                    selectedEtalaseChipName,
                    etalaseName,
                    productPositionStart,
                    customDimensionShopPage.shopType,
                    loginNonLoginString,
                    shopId,
                    dimension90Value,
                    sortAndFilterValue,
                    listEventValue,
                    selectedTabName
                )
            )
        )
        return eventMap
    }

    fun getListNameOfProduct(tabName: String, etalaseName: String): String {
        var etalaseName = etalaseName
        etalaseName = if (TextUtils.isEmpty(etalaseName)) ShopPageTrackingConstant.ALL_ETALASE else etalaseName
        return SHOPPAGE + " - " +
            tabName + " - " +
            etalaseName
    }

    fun impressionVoucherFollowUnfollowShop(
        shopId: String,
        userId: String?
    ) {
        followUnfollowShop(
            ShopPageTrackingConstant.VIEW_SHOP_PAGE_IRIS,
            ShopPageTrackingConstant.VIEW_FOLLOW_VOUCHER_ICON,
            ShopPageTrackingConstant.SHOP_PAGE_LABEL + shopId,
            userId
        )
    }

    fun impressionCoachMarkFollowUnfollowShop(
        shopId: String,
        userId: String?
    ) {
        followUnfollowShop(
            ShopPageTrackingConstant.VIEW_SHOP_PAGE_IRIS,
            ShopPageTrackingConstant.VIEW_COACHMARK_FOLLOW,
            ShopPageTrackingConstant.SHOP_PAGE_LABEL + shopId,
            userId
        )
    }

    fun clickFollowUnfollowShop(
        isFollowing: Boolean,
        shopId: String,
        userId: String?
    ) {
        val action: String
        action = if (isFollowing) {
            ShopPageTrackingConstant.CLICK_FOLLOW
        } else {
            ShopPageTrackingConstant.CLICK_UNFOLLOW
        }
        followUnfollowShop(
            ShopPageTrackingConstant.CLICK_SHOP_PAGE,
            action,
            ShopPageTrackingConstant.SHOP_PAGE_LABEL + shopId,
            userId
        )
    }

    fun clickFollowUnfollowShopWithoutShopFollower(
        isFollow: Boolean,
        customDimensionShopPage: CustomDimensionShopPage?
    ) {
        val followUnfollow = if (isFollow) ShopPageTrackingConstant.FOLLOW else ShopPageTrackingConstant.UNFOLLOW
        sendGeneralEvent(
            ShopPageTrackingConstant.CLICK_SHOP_PAGE,
            ShopPageTrackingConstant.SHOP_PAGE_BUYER,
            joinSpace(ShopPageTrackingConstant.CLICK, followUnfollow),
            "",
            customDimensionShopPage
        )
    }

    fun impressionToasterFollow(
        isSuccess: Boolean,
        shopId: String,
        userId: String?
    ) {
        val action: String
        action = if (isSuccess) {
            ShopPageTrackingConstant.VIEW_TOASTER_FOLLOW_SUCCESS
        } else {
            ShopPageTrackingConstant.VIEW_TOASTER_FOLLOW_ERROR
        }
        followUnfollowShop(
            ShopPageTrackingConstant.VIEW_SHOP_PAGE_IRIS,
            action,
            ShopPageTrackingConstant.SHOP_PAGE_LABEL + shopId,
            userId
        )
    }

    fun impressionToasterUnfollow(
        isSuccess: Boolean,
        shopId: String,
        userId: String?
    ) {
        val action: String
        action = if (isSuccess) {
            ShopPageTrackingConstant.VIEW_TOASTER_UNFOLLOW_SUCCESS
        } else {
            ShopPageTrackingConstant.VIEW_TOASTER_UNFOLLOW_ERROR
        }
        followUnfollowShop(
            ShopPageTrackingConstant.VIEW_SHOP_PAGE_IRIS,
            action,
            ShopPageTrackingConstant.SHOP_PAGE_LABEL + shopId,
            userId
        )
    }

    fun clickCekToasterSuccess(
        shopId: String,
        userId: String?
    ) {
        followUnfollowShop(
            ShopPageTrackingConstant.CLICK_SHOP_PAGE,
            ShopPageTrackingConstant.CLICK_CEK_TOASTER_SUCCESS,
            ShopPageTrackingConstant.SHOP_PAGE_LABEL + shopId,
            userId
        )
    }

    fun impressionSeeEntryPointMerchantVoucherCoupon(
        shopId: String,
        userId: String?
    ) {
        followUnfollowShop(
            ShopPageTrackingConstant.VIEW_SHOP_PAGE_IRIS,
            ShopPageTrackingConstant.SEE_ENTRY_POINT,
            ShopPageTrackingConstant.SHOP_PAGE_LABEL + shopId,
            userId
        )
    }

    fun clickFollowShowcaseNplButton(
        shopId: String?,
        userId: String?,
        customDimensionShopPage: CustomDimensionShopPage?
    ) {
        sendGeneralEventNplFollower(
            ShopPageTrackingConstant.CLICK_SHOP_PAGE,
            ShopPageTrackingConstant.SHOP_PAGE_BUYER,
            String.format(ShopPageTrackingConstant.CLICK_SHOWCASE_FOLLOW_NPL, ShopPageTrackingConstant.FOLLOW),
            shopId,
            ShopPageTrackingConstant.PHYSICAL_GOODS,
            TOKOPEDIA_MARKETPLACE,
            userId,
            customDimensionShopPage
        )
    }

    fun clickCTASuccessFollowNplToaster(
        shopId: String?,
        userId: String?,
        customDimensionShopPage: CustomDimensionShopPage?
    ) {
        sendGeneralEventNplFollower(
            ShopPageTrackingConstant.CLICK_SHOP_PAGE,
            ShopPageTrackingConstant.SHOP_PAGE_BUYER,
            ShopPageTrackingConstant.CLICK_OK_SUCCESS_FOLLOW_TOASTER_NPL,
            shopId,
            ShopPageTrackingConstant.PHYSICAL_GOODS,
            TOKOPEDIA_MARKETPLACE,
            userId,
            customDimensionShopPage
        )
    }

    fun clickMessageSeller(customDimensionShopPage: CustomDimensionShopPage?) {
        sendGeneralEvent(
            ShopPageTrackingConstant.CLICK_SHOP_PAGE,
            ShopPageTrackingConstant.SHOP_PAGE_BUYER,
            ShopPageTrackingConstant.CLICK_CHAT_SELLER,
            "",
            customDimensionShopPage
        )
    }

    fun clickProduct(
        isLogin: Boolean,
        selectedEtalaseChipName: String?,
        etalaseSection: String,
        customDimensionShopPage: CustomDimensionShopPageAttribution,
        shopProductUiModel: ShopProductUiModel,
        productPosStart: Int,
        shopId: String,
        isSelectedEtalaseCampaign: Boolean,
        isEtalaseSectionCampaign: Boolean,
        isUpcoming: Boolean,
        sortAndFilterValue: String = "",
        userId: String = "",
        selectedTabName: String = ""
    ) {
        val loginNonLoginString = if (isLogin) ShopPageTrackingConstant.LOGIN else ShopPageTrackingConstant.NON_LOGIN
        val etalaseNameTrackerString = if (isSelectedEtalaseCampaign) String.format(ShopPageTrackingConstant.LABEL_ETALASE_CAMPAIGN, selectedEtalaseChipName) else selectedEtalaseChipName!!
        val etalaseSectionTrackerString = getEtalaseNameTrackerString(isEtalaseSectionCampaign, isUpcoming, etalaseSection, ShopPageConstant.DEFAULT_VALUE_ETALASE_TYPE)
        val etalaseChip = String.format(
            ShopPageTrackingConstant.SELECTED_ETALASE_CHIP,
            ShopPageTrackingConstant.ALL_PRODUCT
        )
        val listEventValue = joinDash(
            SHOPPAGE,
            customDimensionShopPage.shopId,
            etalaseChip,
            loginNonLoginString,
            ShopPageTrackingConstant.NOT_SEARCH_RESULT
        )
        val eventAction = joinDash(
            ALL_PRODUCT_CLICKED,
            etalaseChip,
            loginNonLoginString,
            ShopPageTrackingConstant.NOT_SEARCH_RESULT
        )
        val event = createProductClickMap(
            ShopPageTrackingConstant.PRODUCT_CLICK,
            SHOP_PAGE_BUYER,
            loginNonLoginString,
            eventAction,
            "",
            customDimensionShopPage,
            shopProductUiModel,
            etalaseNameTrackerString, etalaseSectionTrackerString,
            productPosStart,
            shopId,
            sortAndFilterValue,
            listEventValue,
            selectedTabName
        )
        event[TRACKER_ID] = ShopPageTrackingConstant.TrackerId.TRACKER_ID_ALL_PRODUCT_CLICKED
        event[BUSINESS_UNIT] = PHYSICAL_GOODS
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[ShopPageTrackingConstant.ITEM_LIST] = listEventValue
        event[SHOP_ID] = customDimensionShopPage.shopId.orEmpty()
        event[USER_ID] = userId
        sendDataLayerEvent(event)
    }

    fun clickProductSearchResult(
        isLogin: Boolean,
        selectedEtalaseChipName: String,
        etalaseSection: String,
        customDimensionShopPage: CustomDimensionShopPageAttribution,
        shopProductUiModel: ShopProductUiModel,
        productPosStart: Int,
        shopId: String,
        isEtalaseCampaign: Boolean,
        isUpcoming: Boolean,
        etalaseType: Int,
        shopName: String,
        navSource: String,
        sortAndFilterValue: String = "",
        userId: String,
        selectedTabName: String
    ) {
        val loginNonLoginString = if (isLogin) ShopPageTrackingConstant.LOGIN else ShopPageTrackingConstant.NON_LOGIN
        val etalaseNameTrackerString = getEtalaseNameTrackerString(isEtalaseCampaign, isUpcoming, selectedEtalaseChipName, etalaseType)
        val etalaseChip = String.format(
            ShopPageTrackingConstant.SELECTED_ETALASE_CHIP,
            etalaseNameTrackerString
        )
        val listEventValue = joinDash(
            SHOPPAGE,
            customDimensionShopPage.shopId,
            etalaseChip,
            loginNonLoginString,
            ShopPageTrackingConstant.SEARCH_RESULT
        )
        val eventAction = joinDash(
            ALL_PRODUCT_CLICKED,
            etalaseChip,
            loginNonLoginString,
            ShopPageTrackingConstant.SEARCH_RESULT
        )
        val event = createProductClickSearchResultMap(
            ShopPageTrackingConstant.PRODUCT_CLICK,
            SHOP_PAGE_BUYER,
            loginNonLoginString,
            eventAction,
            "",
            customDimensionShopPage,
            shopProductUiModel,
            etalaseNameTrackerString,
            etalaseSection,
            productPosStart,
            shopId,
            shopName,
            navSource,
            sortAndFilterValue,
            listEventValue,
            selectedTabName
        )
        event[TRACKER_ID] = ShopPageTrackingConstant.TrackerId.TRACKER_ID_ALL_PRODUCT_CLICKED
        event[BUSINESS_UNIT] = PHYSICAL_GOODS
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[ShopPageTrackingConstant.ITEM_LIST] = listEventValue
        event[SHOP_ID] = customDimensionShopPage.shopId.orEmpty()
        event[USER_ID] = userId
        sendDataLayerEvent(event)
    }

    fun clickProductListEmptyState(
        isLogin: Boolean,
        customDimensionShopPage: CustomDimensionShopPageAttribution,
        shopProductUiModel: ShopProductUiModel,
        productPosStart: Int,
        shopId: String
    ) {
        val loginNonLoginString = if (isLogin) ShopPageTrackingConstant.LOGIN else ShopPageTrackingConstant.NON_LOGIN
        val event: Map<String, Any> = createProductClickEmptyStateMap(
            ShopPageTrackingConstant.PRODUCT_CLICK,
            ShopPageTrackingConstant.SHOP_PAGE_BUYER,
            ShopPageTrackingConstant.CLICK_PRODUCT_SEARCH_SUGGESTION,
            "",
            loginNonLoginString,
            customDimensionShopPage,
            shopProductUiModel,
            productPosStart,
            shopId
        )
        sendDataLayerEvent(event)
    }

    private fun createProductClickEmptyStateMap(
        event: String,
        category: String,
        action: String,
        label: String,
        loginNonLoginString: String,
        customDimensionShopPage: CustomDimensionShopPageAttribution,
        shopProductUiModel: ShopProductUiModel,
        productPositionStart: Int,
        shopId: String
    ): HashMap<String, Any> {
        val shopProductUiModelArrayList = ArrayList<ShopProductUiModel>()
        shopProductUiModelArrayList.add(shopProductUiModel)
        val eventMap = createMap(event, category, action, label, customDimensionShopPage)
        eventMap[ShopPageTrackingConstant.ECOMMERCE] = DataLayer.mapOf(
            ShopPageTrackingConstant.CLICK,
            DataLayer.mapOf(
                ShopPageTrackingConstant.ACTION_FIELD,
                DataLayer.mapOf(ShopPageTrackingConstant.LIST, joinDash(SHOPPAGE, shopId, ShopPageTrackingConstant.SEARCH_NO_RESULT_SUGGESTION, loginNonLoginString)),
                ShopPageTrackingConstant.PRODUCTS,
                createProductListMapEmptyState(
                    shopProductUiModelArrayList,
                    productPositionStart,
                    customDimensionShopPage.shopType,
                    loginNonLoginString,
                    shopId,
                    customDimensionShopPage.shopRef
                )
            )
        )
        return eventMap
    }

    fun impressionProductList(
        isLogin: Boolean,
        selectedEtalaseChipName: String?,
        etalaseSection: String,
        customDimensionShopPage: CustomDimensionShopPageAttribution,
        shopProductUiModel: ShopProductUiModel,
        productPosStart: Int,
        shopId: String,
        isSelectedEtalaseCampaign: Boolean,
        isEtalaseSectionCampaign: Boolean,
        isUpcoming: Boolean,
        sortAndFilterValue: String = "",
        userId: String = "",
        selectedTabName: String = ""
    ) {
        val loginNonLoginString = if (isLogin) ShopPageTrackingConstant.LOGIN else ShopPageTrackingConstant.NON_LOGIN
        val etalaseNameTrackerString = if (isSelectedEtalaseCampaign) String.format(ShopPageTrackingConstant.LABEL_ETALASE_CAMPAIGN, selectedEtalaseChipName) else selectedEtalaseChipName!!
        val etalaseSectionTrackerString = getEtalaseNameTrackerString(isEtalaseSectionCampaign, isUpcoming, etalaseSection, ShopPageConstant.DEFAULT_VALUE_ETALASE_TYPE)
        val etalaseChip = String.format(
            ShopPageTrackingConstant.SELECTED_ETALASE_CHIP,
            ShopPageTrackingConstant.ALL_PRODUCT
        )
        val listEventValue = joinDash(
            SHOPPAGE,
            customDimensionShopPage.shopId,
            etalaseChip,
            loginNonLoginString,
            ShopPageTrackingConstant.NOT_SEARCH_RESULT
        )
        val eventAction = joinDash(
            ALL_PRODUCT_IMPRESSION,
            etalaseChip,
            loginNonLoginString,
            ShopPageTrackingConstant.NOT_SEARCH_RESULT
        )
        val event = createProductImpressionMap(
            ShopPageTrackingConstant.PRODUCT_VIEW,
            SHOP_PAGE_BUYER,
            loginNonLoginString,
            eventAction,
            "",
            customDimensionShopPage,
            shopProductUiModel,
            etalaseNameTrackerString, etalaseSectionTrackerString,
            productPosStart,
            shopId,
            sortAndFilterValue,
            listEventValue,
            selectedTabName
        ).toMutableMap()
        event[TRACKER_ID] = ShopPageTrackingConstant.TrackerId.TRACKER_ID_ALL_PRODUCT_IMPRESSION
        event[BUSINESS_UNIT] = PHYSICAL_GOODS
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[ShopPageTrackingConstant.ITEM_LIST] = listEventValue
        event[SHOP_ID] = customDimensionShopPage.shopId.orEmpty()
        event[USER_ID] = userId
        sendDataLayerEvent(event)
    }

    fun impressionProductListSearchResult(
        isLogin: Boolean,
        selectedEtalaseChipName: String,
        etalaseSection: String,
        customDimensionShopPage: CustomDimensionShopPageAttribution,
        shopProductUiModel: ShopProductUiModel,
        productPosStart: Int,
        shopId: String,
        isEtalaseCampaign: Boolean,
        isUpcoming: Boolean,
        etalaseType: Int,
        shopName: String,
        navSource: String,
        sortAndFilterValue: String = "",
        userId: String,
        selectedTabName: String
    ) {
        val loginNonLoginString = if (isLogin) ShopPageTrackingConstant.LOGIN else ShopPageTrackingConstant.NON_LOGIN
        val etalaseNameTrackerString = getEtalaseNameTrackerString(isEtalaseCampaign, isUpcoming, selectedEtalaseChipName, etalaseType)
        val etalaseChip = String.format(
            ShopPageTrackingConstant.SELECTED_ETALASE_CHIP,
            etalaseNameTrackerString
        )
        val listEventValue = joinDash(
            SHOPPAGE,
            customDimensionShopPage.shopId,
            etalaseChip,
            loginNonLoginString,
            ShopPageTrackingConstant.SEARCH_RESULT
        )
        val eventAction = joinDash(
            ALL_PRODUCT_IMPRESSION,
            etalaseChip,
            loginNonLoginString,
            ShopPageTrackingConstant.SEARCH_RESULT
        )
        val event = createProductImpressionSearchResultMap(
            ShopPageTrackingConstant.PRODUCT_VIEW,
            SHOP_PAGE_BUYER,
            loginNonLoginString,
            eventAction,
            "",
            customDimensionShopPage,
            shopProductUiModel,
            etalaseNameTrackerString,
            etalaseSection, productPosStart,
            shopId,
            shopName,
            navSource,
            sortAndFilterValue,
            listEventValue,
            selectedTabName
        )
        event[TRACKER_ID] = ShopPageTrackingConstant.TrackerId.TRACKER_ID_ALL_PRODUCT_IMPRESSION
        event[BUSINESS_UNIT] = PHYSICAL_GOODS
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[ShopPageTrackingConstant.ITEM_LIST] = listEventValue
        event[SHOP_ID] = customDimensionShopPage.shopId.orEmpty()
        event[USER_ID] = userId
        sendDataLayerEvent(event)
    }

    fun clickWishlist(
        isAdd: Boolean,
        isLogin: Boolean,
        selectedEtalaseName: String?,
        sectionName: String,
        customDimensionShopPage: CustomDimensionShopPageProduct,
        isSelectedEtalaseCampaign: Boolean,
        isEtalaseSectionCampaign: Boolean,
        isUpcoming: Boolean
    ) {
        val loginNonLoginString = if (isLogin) ShopPageTrackingConstant.LOGIN else ShopPageTrackingConstant.NON_LOGIN
        val etalaseNameTrackerString = if (isSelectedEtalaseCampaign) String.format(ShopPageTrackingConstant.LABEL_ETALASE_CAMPAIGN, selectedEtalaseName) else selectedEtalaseName!!
        val etalaseSectionTrackerString = getEtalaseNameTrackerString(isEtalaseSectionCampaign, isUpcoming, sectionName, ShopPageConstant.DEFAULT_VALUE_ETALASE_TYPE)
        val etalaseEvent = joinDash(getProductEtalaseEvent(etalaseNameTrackerString, etalaseSectionTrackerString), loginNonLoginString)
        sendGeneralEvent(
            ShopPageTrackingConstant.CLICK_SHOP_PAGE,
            ShopPageTrackingConstant.SHOP_PAGE_BUYER,
            joinDash(joinSpace(if (isAdd) ShopPageTrackingConstant.ADD else ShopPageTrackingConstant.REMOVE, ShopPageTrackingConstant.WISHLIST), etalaseEvent),
            customDimensionShopPage.productId,
            customDimensionShopPage
        )
    }

    fun clickWishlistProductResultPage(
        isAdd: Boolean,
        isLogin: Boolean,
        selectedEtalaseName: String,
        customDimensionShopPage: CustomDimensionShopPageProduct,
        isEtalaseCampaign: Boolean,
        isUpcoming: Boolean,
        etalaseType: Int
    ) {
        val loginNonLoginString = if (isLogin) ShopPageTrackingConstant.LOGIN else ShopPageTrackingConstant.NON_LOGIN
        val etalaseNameTrackerString = getEtalaseNameTrackerString(isEtalaseCampaign, isUpcoming, selectedEtalaseName, etalaseType)
        val etalaseEvent = joinDash(getProductEtalaseEvent(etalaseNameTrackerString, ""), loginNonLoginString, ShopPageTrackingConstant.SEARCH_RESULT)
        sendGeneralEvent(
            ShopPageTrackingConstant.CLICK_SHOP_PAGE,
            ShopPageTrackingConstant.SHOP_PAGE_BUYER,
            joinDash(joinSpace(if (isAdd) ShopPageTrackingConstant.ADD else ShopPageTrackingConstant.REMOVE, ShopPageTrackingConstant.WISHLIST), etalaseEvent),
            customDimensionShopPage.productId,
            customDimensionShopPage
        )
    }

    fun eventShopSendChat() {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            ShopPageTrackingConstant.CLICK_SHOP_MESSAGE,
            SHOP_PAGE,
            ShopPageTrackingConstant.CLICK_SEND_CHAT,
            ""
        )
    }

    fun clickSearch(isOwner: Boolean, customDimensionShopPage: CustomDimensionShopPage?) {
        sendGeneralEvent(
            ShopPageTrackingConstant.CLICK_SHOP_PAGE,
            getShopPageCategory(isOwner),
            ShopPageTrackingConstant.CLICK_SEARCH,
            "",
            customDimensionShopPage
        )
    }

    fun clickShareButton(customDimensionShopPage: CustomDimensionShopPage?) {
        sendGeneralEvent(
            ShopPageTrackingConstant.CLICK_SHOP_PAGE,
            ShopPageTrackingConstant.SHOP_PROFILE_PAGE_BUYER,
            ShopPageTrackingConstant.CLICK_SHARE,
            "",
            customDimensionShopPage
        )
    }

    fun clickShareButtonSellerView(customDimensionShopPage: CustomDimensionShopPage?) {
        sendGeneralEvent(
            ShopPageTrackingConstant.CLICK_SHOP_PAGE,
            ShopPageTrackingConstant.SETTING_PAGE_SELLER,
            ShopPageTrackingConstant.CLICK_SHARE,
            "",
            customDimensionShopPage
        )
    }

    fun clickCancelShareBottomsheet(customDimensionShopPage: CustomDimensionShopPage?, isMyShop: Boolean) {
        sendGeneralEvent(
            if (isMyShop) ShopPageTrackingConstant.CLICK_SHOP_SETTING else ShopPageTrackingConstant.CLICK_PROFILE,
            if (isMyShop) ShopPageTrackingConstant.SETTING_PAGE_SELLER else ShopPageTrackingConstant.SHOP_PROFILE_PAGE_BUYER,
            ShopPageTrackingConstant.CLICK_SHARE_DETAIL,
            ShopPageTrackingConstant.CLICK_BOTTOMSHEET_DISMISS_BUTTON,
            customDimensionShopPage
        )
    }

    fun clickShareSocialMedia(customDimensionShopPage: CustomDimensionShopPage?, isMyShop: Boolean, socialMediaName: String?) {
        sendGeneralEvent(
            if (isMyShop) ShopPageTrackingConstant.CLICK_SHOP_SETTING else ShopPageTrackingConstant.CLICK_PROFILE,
            if (isMyShop) ShopPageTrackingConstant.SETTING_PAGE_SELLER else ShopPageTrackingConstant.SHOP_PROFILE_PAGE_BUYER,
            ShopPageTrackingConstant.CLICK_SHARE_DETAIL,
            socialMediaName,
            customDimensionShopPage
        )
    }

    fun sendEventMembership(shopId: String, userId: String) {
        val eventMap: MutableMap<String, Any> = mutableMapOf(
            ShopPageTrackingConstant.EVENT to ShopPageTrackingConstant.CLICK_SHOP_PAGE,
            ShopPageTrackingConstant.EVENT_ACTION to ShopPageTrackingConstant.CLICK_MEMBERSHIP_EVENT,
            ShopPageTrackingConstant.EVENT_CATEGORY to ShopPageTrackingConstant.SHOP_PAGE_BUYER,
            ShopPageTrackingConstant.EVENT_LABEL to ShopPageTrackingConstant.LABEL_APPLY_SHOP_MEMBER,
            ShopPageTrackingConstant.BUSINESS_UNIT to ShopPageTrackingConstant.PHYSICAL_GOODS,
            ShopPageTrackingConstant.CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            ShopPageTrackingConstant.SHOP_ID to shopId,
            ShopPageTrackingConstant.USER_ID to userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    fun clickShopProfile(customDimensionShopPage: CustomDimensionShopPage?) {
        sendGeneralEvent(
            ShopPageTrackingConstant.CLICK_SHOP_PAGE,
            ShopPageTrackingConstant.SHOP_PAGE_BUYER,
            ShopPageTrackingConstant.CLICK_SHOP_PROFILE,
            "",
            customDimensionShopPage
        )
    }

    fun clickFollowUnfollow(isShopFavorited: Boolean, customDimensionShopPage: CustomDimensionShopPage?) {
        val action: String
        action = if (!isShopFavorited) {
            ShopPageTrackingConstant.CLICK_FOLLOW
        } else {
            ShopPageTrackingConstant.CLICK_UNFOLLOW
        }
        sendGeneralEvent(
            ShopPageTrackingConstant.CLICK_SHOP_PAGE,
            ShopPageTrackingConstant.SHOP_PAGE_BUYER,
            action,
            "",
            customDimensionShopPage
        )
    }

    fun clickMoreMenuChip(
        isOwner: Boolean,
        selectedEtalaseName: String?,
        customDimensionShopPage: CustomDimensionShopPage?
    ) {
        sendGeneralEvent(
            ShopPageTrackingConstant.CLICK_SHOP_PAGE,
            getShopPageCategory(isOwner),
            ShopPageTrackingConstant.CLICK_SHOWCASE_LIST,
            String.format(ShopPageTrackingConstant.ETALASE_X, selectedEtalaseName),
            customDimensionShopPage
        )
    }

    fun sendMoEngageFavoriteEvent(
        shopName: String?,
        shopID: String?,
        shopDomain: String?,
        shopLocation: String?,
        isShopOfficaial: Boolean?,
        isFollowed: Boolean
    ) {
        val mapData = DataLayer.mapOf(
            ShopPageTrackingConstant.SHOP_NAME, shopName,
            ShopPageTrackingConstant.SHOP_ID, shopID,
            ShopPageTrackingConstant.SHOP_LOCATION, shopLocation,
            ShopPageTrackingConstant.URL_SLUG, shopDomain,
            ShopPageTrackingConstant.IS_OFFICIAL_STORE, isShopOfficaial
        )
        val eventName: String
        eventName = if (isFollowed) ShopPageTrackingConstant.SELLER_ADDED_TO_FAVORITE else ShopPageTrackingConstant.SELLER_REMOVED_FROM_FAVORITE
        TrackApp.getInstance().moEngage.sendTrackEvent(mapData, eventName)
    }

    private fun getProductEtalaseEvent(
        selectedEtalaseChipName: String,
        etalaseSection: String
    ): String {
        val etalaseEventEtalaseSectionEmpty = String.format(
            ShopPageTrackingConstant.SELECTED_ETALASE_CHIP,
            selectedEtalaseChipName
        )
        val etalaseEventEtalaseSectionNotEmpty = joinDash(String.format(ShopPageTrackingConstant.SELECTED_ETALASE_CHIP, selectedEtalaseChipName), String.format(ShopPageTrackingConstant.ETALASE_SECTION, etalaseSection))
        return if (etalaseSection.isEmpty()) etalaseEventEtalaseSectionEmpty else etalaseEventEtalaseSectionNotEmpty
    }

    fun clickClearFilter(isOwner: Boolean, customDimensionShopPage: CustomDimensionShopPage?) {
        sendGeneralEvent(
            ShopPageTrackingConstant.CLICK_SHOP_PAGE,
            getShopPageCategory(isOwner),
            ShopPageTrackingConstant.CLICK_CLOSE_FILTER,
            "",
            customDimensionShopPage
        )
    }

    private fun getEtalaseNameTrackerString(
        isEtalaseSectionCampaign: Boolean,
        isUpcoming: Boolean,
        etalaseSection: String,
        etalaseType: Int // Used for validating type of campaign only
    ): String {
        val etalaseSectionTrackerString: String
        if (isEtalaseSectionCampaign) {
            var valueTypeCampaign = ""
            valueTypeCampaign = if (etalaseType == ShopEtalaseTypeDef.ETALASE_THEMATIC_CAMPAIGN) {
                ShopPageTrackingConstant.VALUE_THEMATIC
            } else {
                if (isUpcoming) ShopPageTrackingConstant.VALUE_UPCOMING else ShopPageTrackingConstant.VALUE_ONGOING
            }
            etalaseSectionTrackerString = String.format(
                ShopPageTrackingConstant.LABEL_ETALASE_UPCOMING_ONGOING_CAMPAIGN,
                valueTypeCampaign,
                etalaseSection
            )
        } else {
            etalaseSectionTrackerString = etalaseSection
        }
        return etalaseSectionTrackerString
    }

    private fun createProductListMapEmptyState(
        shopProductUiModelList: List<ShopProductUiModel>,
        productPosition: Int,
        shopTypeDef: String?,
        loginNonLoginString: String,
        shopId: String,
        shopRef: String
    ): List<Any> {
        val list: MutableList<Any> = ArrayList()
        for (i in shopProductUiModelList.indices) {
            val viewModel = shopProductUiModelList[i]
            val event = HashMap(
                DataLayer.mapOf(
                    ShopPageTrackingConstant.NAME, viewModel.name,
                    ShopPageTrackingConstant.ID, viewModel.id,
                    ShopPageTrackingConstant.PRICE, formatPrice(viewModel.displayedPrice!!),
                    ShopPageTrackingConstant.BRAND, ShopPageTrackingConstant.NONE,
                    ShopPageTrackingConstant.CATEGORY, ShopPageTrackingConstant.NONE,
                    ShopPageTrackingConstant.VARIANT, ShopPageTrackingConstant.NONE,
                    ShopPageTrackingConstant.LIST, joinDash(SHOPPAGE, shopId, ShopPageTrackingConstant.SEARCH_NO_RESULT_SUGGESTION, loginNonLoginString),
                    ShopPageTrackingConstant.POSITION, productPosition,
                    ShopPageTrackingConstant.DIMENSION_81, shopTypeDef,
                    ShopPageTrackingConstant.DIMENSION_79, shopId,
                    ShopPageTrackingConstant.DIMENSION_90, shopRef
                )
            )
            list.add(event)
        }
        return list
    }

    private fun createProductImpressionMapEmptyState(
        event: String,
        category: String,
        action: String,
        label: String,
        loginNonLoginString: String,
        customDimensionShopPage: CustomDimensionShopPageAttribution,
        shopProductUiModel: ShopProductUiModel,
        productPositionStart: Int,
        shopId: String
    ): HashMap<String, Any> {
        val shopProductUiModelArrayList = ArrayList<ShopProductUiModel>()
        shopProductUiModelArrayList.add(shopProductUiModel)
        val eventMap = createMap(event, category, action, label, customDimensionShopPage)
        eventMap[ShopPageTrackingConstant.ECOMMERCE] = DataLayer.mapOf(
            ShopPageTrackingConstant.CURRENCY_CODE,
            ShopPageTrackingConstant.IDR,
            ShopPageTrackingConstant.IMPRESSIONS,
            createProductListMapEmptyState(
                shopProductUiModelArrayList,
                productPositionStart,
                customDimensionShopPage.shopType,
                loginNonLoginString,
                shopId,
                customDimensionShopPage.shopRef
            )
        )
        return eventMap
    }

    fun impressionProductListEmptyState(
        isLogin: Boolean,
        customDimensionShopPage: CustomDimensionShopPageAttribution,
        shopProductUiModel: ShopProductUiModel,
        productPosStart: Int,
        shopId: String
    ) {
        val loginNonLoginString = if (isLogin) ShopPageTrackingConstant.LOGIN else ShopPageTrackingConstant.NON_LOGIN
        val event: Map<String, Any> = createProductImpressionMapEmptyState(
            ShopPageTrackingConstant.PRODUCT_VIEW,
            ShopPageTrackingConstant.SHOP_PAGE_BUYER,
            ShopPageTrackingConstant.IMPRESSION_PRODUCT_SEARCH_SUGGESTION,
            "",
            loginNonLoginString,
            customDimensionShopPage,
            shopProductUiModel,
            productPosStart,
            shopId
        )
        sendDataLayerEvent(event)
    }

    fun clickPrimaryBtnEmptyStateSearch(shopId: String?, shopType: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            DataLayer.mapOf(
                ShopPageTrackingConstant.EVENT, ShopPageTrackingConstant.CLICK_SHOP_PAGE,
                ShopPageTrackingConstant.EVENT_CATEGORY, ShopPageTrackingConstant.SHOP_PAGE_BUYER,
                ShopPageTrackingConstant.EVENT_ACTION, ShopPageTrackingConstant.CLICK_SEARCH_PAGE_NO_RESULT,
                ShopPageTrackingConstant.EVENT_LABEL, ShopPageTrackingConstant.TRY_ANOTHER_WORD,
                ShopPageTrackingConstant.SHOP_ID, shopId,
                ShopPageTrackingConstant.SHOP_TYPE, shopType,
                ShopPageTrackingConstant.PAGE_TYPE, SHOPPAGE
            )
        )
    }

    fun clickSecondaryBtnEmptyStateSearch(shopId: String?, shopType: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            DataLayer.mapOf(
                ShopPageTrackingConstant.EVENT, ShopPageTrackingConstant.CLICK_SHOP_PAGE,
                ShopPageTrackingConstant.EVENT_CATEGORY, ShopPageTrackingConstant.SHOP_PAGE_BUYER,
                ShopPageTrackingConstant.EVENT_ACTION, ShopPageTrackingConstant.CLICK_SEARCH_PAGE_NO_RESULT,
                ShopPageTrackingConstant.EVENT_LABEL, ShopPageTrackingConstant.SEARCH_ON_TOKOPEDIA,
                ShopPageTrackingConstant.SHOP_ID, shopId,
                ShopPageTrackingConstant.SHOP_TYPE, shopType,
                ShopPageTrackingConstant.PAGE_TYPE, SHOPPAGE
            )
        )
    }

    fun clickProductListToggle(
        initialView: ShopProductViewGridType,
        finalView: ShopProductViewGridType,
        shopId: String,
        userId: String
    ) {
        val initialViewString = ShopUtil.getShopGridViewTypeString(initialView)
        val finalViewString = ShopUtil.getShopGridViewTypeString(finalView)
        val eventLabel = String.format(ShopPageTrackingConstant.LABEL_CLICK_PRODUCT_LIST_TOGGLE, initialViewString, finalViewString)
        val eventMap: MutableMap<String, Any> = mutableMapOf(
            ShopPageTrackingConstant.EVENT to ShopPageTrackingConstant.CLICK_SHOP_PAGE,
            ShopPageTrackingConstant.EVENT_ACTION to ShopPageTrackingConstant.ACTION_CLICK_PRODUCT_LIST_TOGGLE,
            ShopPageTrackingConstant.EVENT_CATEGORY to ShopPageTrackingConstant.SHOP_PAGE_BUYER,
            ShopPageTrackingConstant.EVENT_LABEL to eventLabel,
            ShopPageTrackingConstant.BUSINESS_UNIT to ShopPageTrackingConstant.PHYSICAL_GOODS,
            ShopPageTrackingConstant.CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            ShopPageTrackingConstant.SHOP_ID to shopId,
            ShopPageTrackingConstant.USER_ID to userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    fun clickFilterChips(productListName: String?, customDimensionShopPage: CustomDimensionShopPage?) {
        sendGeneralEvent(
            ShopPageTrackingConstant.CLICK_SHOP_PAGE,
            ShopPageTrackingConstant.SHOP_PAGE_BUYER,
            ShopPageTrackingConstant.CLICK_FILTER_CHIP,
            productListName,
            customDimensionShopPage
        )
    }

    fun clickApplyFilter(
        selectedSortName: String,
        selectedFilterMap: Map<String, String>,
        userId: String
    ) {
        var eventLabel = ShopPageTrackingConstant.LABEL_CLICK_APPLY_FILTER_CHIP
        if (selectedSortName.isNotBlank()) {
            eventLabel += " - $selectedSortName"
        }
        if (!selectedFilterMap[PMAX_PARAM_KEY].isNullOrBlank() || !selectedFilterMap[PMIN_PARAM_KEY].isNullOrBlank()) {
            val minPrice = selectedFilterMap[PMIN_PARAM_KEY] ?: "0"
            val maxPrice = selectedFilterMap[PMAX_PARAM_KEY] ?: "0"
            eventLabel += " - $minPrice - $maxPrice"
        }
        if (!selectedFilterMap[RATING_PARAM_KEY].isNullOrBlank()) {
            val rating = selectedFilterMap[RATING_PARAM_KEY] ?: "0"
            eventLabel += " - $rating"
        }
        val eventMap: MutableMap<String, Any> = mutableMapOf(
            ShopPageTrackingConstant.EVENT to ShopPageTrackingConstant.CLICK_SHOP_PAGE,
            ShopPageTrackingConstant.EVENT_ACTION to ShopPageTrackingConstant.CLICK_FILTER_CHIP,
            ShopPageTrackingConstant.EVENT_CATEGORY to ShopPageTrackingConstant.SHOP_PAGE_BUYER,
            ShopPageTrackingConstant.EVENT_LABEL to eventLabel,
            ShopPageTrackingConstant.BUSINESS_UNIT to ShopPageTrackingConstant.PHYSICAL_GOODS,
            ShopPageTrackingConstant.CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            ShopPageTrackingConstant.USER_ID to userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    fun sendShopPageProductSearchResultTracker(
        isOwner: Boolean?,
        keyword: String?,
        isProductResultListEmpty: Boolean,
        customDimensionShopPage: CustomDimensionShopPage?
    ) {
        val actionEvent = if (isProductResultListEmpty) ShopPageTrackingConstant.SEARCH_NO_RESULT else ShopPageTrackingConstant.SEARCH
        sendGeneralEvent(
            ShopPageTrackingConstant.CLICK_SHOP_PAGE,
            getShopPageCategory(isOwner!!),
            actionEvent,
            keyword,
            customDimensionShopPage
        )
    }

    fun sendShopPageProductSearchClickEtalaseProductResultTracker(
        isMyShop: Boolean,
        keyword: String?,
        isProductResultListEmpty: Boolean,
        customDimensionShopPage: CustomDimensionShopPage?
    ) {
        val eventActionFormat = if (isProductResultListEmpty) ShopPageTrackingConstant.SHOP_SEARCH_PRODUCT_CLICK_ETALASE_AUTOCOMPLETE_EMPTY else ShopPageTrackingConstant.SHOP_SEARCH_PRODUCT_CLICK_ETALASE_AUTOCOMPLETE
        sendGeneralEvent(
            ShopPageTrackingConstant.CLICK_SHOP_PAGE,
            getShopPageCategory(isMyShop),
            String.format(eventActionFormat, keyword),
            keyword,
            customDimensionShopPage
        )
    }

    fun clickShareButtonNewBottomSheet(
        customDimensionShopPage: CustomDimensionShopPage,
        userId: String,
        isAffiliateShareIcon: Boolean
    ) {
        val shareType = if (isAffiliateShareIcon) CLICK_SHARE_AFFILIATE_ICON else CLICK_SHARE_REGULER
        val eventMap: MutableMap<String, Any> = mutableMapOf(
            ShopPageTrackingConstant.EVENT to ShopPageTrackingConstant.CLICK_COMMUNICATION,
            ShopPageTrackingConstant.EVENT_ACTION to ShopPageTrackingConstant.CLICK_SHARE_BUTTON,
            ShopPageTrackingConstant.EVENT_CATEGORY to ShopPageTrackingConstant.SHOP_PAGE,
            ShopPageTrackingConstant.EVENT_LABEL to "${customDimensionShopPage.shopId.orEmpty()} - $shareType",
            ShopPageTrackingConstant.TRACKER_ID to ShopPageTrackingConstant.TRACKER_ID_CLICK_SHARE_BUTTON,
            ShopPageTrackingConstant.BUSINESS_UNIT to ShopPageTrackingConstant.SHARING_EXPERIENCE,
            ShopPageTrackingConstant.CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            ShopPageTrackingConstant.SHOP_ID to customDimensionShopPage.shopId.orEmpty(),
            ShopPageTrackingConstant.USER_ID to userId.ifEmpty { "0" }
        )
        sendDataLayerEvent(eventMap)
    }

    fun clickGlobalHeaderShareButton(
        customDimensionShopPage: CustomDimensionShopPage,
        userId: String
    ) {
        val eventMap: MutableMap<String, Any> = mutableMapOf(
            ShopPageTrackingConstant.EVENT to ShopPageTrackingConstant.CLICK_SHOP_PAGE,
            ShopPageTrackingConstant.EVENT_ACTION to ShopPageTrackingConstant.CLICK_GLOBAL_HEADER,
            ShopPageTrackingConstant.EVENT_CATEGORY to ShopPageTrackingConstant.SHOP_PAGE_BUYER,
            ShopPageTrackingConstant.EVENT_LABEL to ShopPageTrackingConstant.LABEL_CLICK_GLOBAL_HEADER_SHARE_BUTTON,
            ShopPageTrackingConstant.BUSINESS_UNIT to ShopPageTrackingConstant.PHYSICAL_GOODS,
            ShopPageTrackingConstant.CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            ShopPageTrackingConstant.SHOP_ID to customDimensionShopPage.shopId.orEmpty(),
            ShopPageTrackingConstant.USER_ID to userId.ifEmpty { "0" }
        )
        sendDataLayerEvent(eventMap)
    }

    fun clickCloseNewShareBottomSheet(
        customDimensionShopPage: CustomDimensionShopPage,
        userId: String,
        userType: String
    ) {
        val eventMap: MutableMap<String, Any> = mutableMapOf(
            ShopPageTrackingConstant.EVENT to ShopPageTrackingConstant.CLICK_COMMUNICATION,
            ShopPageTrackingConstant.EVENT_ACTION to ShopPageTrackingConstant.CLICK_CLOSE_SHARE_BOTTOM_SHEET,
            ShopPageTrackingConstant.EVENT_CATEGORY to ShopPageTrackingConstant.SHOP_PAGE,
            ShopPageTrackingConstant.EVENT_LABEL to "${customDimensionShopPage.shopId.orEmpty()} - $userType",
            ShopPageTrackingConstant.TRACKER_ID to ShopPageTrackingConstant.TRACKER_ID_CLOSE_SHARE_BOTTOM_SHEET,
            ShopPageTrackingConstant.BUSINESS_UNIT to ShopPageTrackingConstant.SHARING_EXPERIENCE,
            ShopPageTrackingConstant.CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            ShopPageTrackingConstant.SHOP_ID to customDimensionShopPage.shopId.orEmpty(),
            ShopPageTrackingConstant.USER_ID to userId.ifEmpty { "0" }
        )
        sendDataLayerEvent(eventMap)
    }

    fun clickShareBottomSheetOption(
        socialMediaName: String,
        customDimensionShopPage: CustomDimensionShopPage,
        userId: String,
        imageType: String,
        userType: String
    ) {
        val eventMap: MutableMap<String, Any> = mutableMapOf(
            ShopPageTrackingConstant.EVENT to ShopPageTrackingConstant.CLICK_COMMUNICATION,
            ShopPageTrackingConstant.EVENT_ACTION to ShopPageTrackingConstant.CLICK_SHARE_BOTTOM_SHEET_OPTION,
            ShopPageTrackingConstant.EVENT_CATEGORY to ShopPageTrackingConstant.SHOP_PAGE,
            ShopPageTrackingConstant.EVENT_LABEL to "$socialMediaName - ${customDimensionShopPage.shopId.orEmpty()} - $userType - $imageType",
            ShopPageTrackingConstant.TRACKER_ID to ShopPageTrackingConstant.TRACKER_ID_CLICK_SHARING_CHANNEL,
            ShopPageTrackingConstant.BUSINESS_UNIT to ShopPageTrackingConstant.SHARING_EXPERIENCE,
            ShopPageTrackingConstant.CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            ShopPageTrackingConstant.SHOP_ID to customDimensionShopPage.shopId.orEmpty(),
            ShopPageTrackingConstant.USER_ID to userId.ifEmpty { "0" }
        )
        sendDataLayerEvent(eventMap)
    }

    fun clickGlobalHeaderShareBottomSheetOption(socialMediaName: String, customDimensionShopPage: CustomDimensionShopPage, userId: String) {
        val eventLabel = String.format(ShopPageTrackingConstant.LABEL_CLICK_GLOBAL_HEADER_CHOOSE_SHARE_BUTTON, socialMediaName)
        val eventMap: MutableMap<String, Any> = mutableMapOf(
            ShopPageTrackingConstant.EVENT to ShopPageTrackingConstant.CLICK_SHOP_PAGE,
            ShopPageTrackingConstant.EVENT_ACTION to ShopPageTrackingConstant.CLICK_GLOBAL_HEADER,
            ShopPageTrackingConstant.EVENT_CATEGORY to ShopPageTrackingConstant.SHOP_PAGE_BUYER,
            ShopPageTrackingConstant.EVENT_LABEL to eventLabel,
            ShopPageTrackingConstant.BUSINESS_UNIT to ShopPageTrackingConstant.PHYSICAL_GOODS,
            ShopPageTrackingConstant.CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            ShopPageTrackingConstant.SHOP_ID to customDimensionShopPage.shopId.orEmpty(),
            ShopPageTrackingConstant.USER_ID to userId.ifEmpty { "0" }
        )
        sendDataLayerEvent(eventMap)
    }

    fun onImpressionShareBottomSheet(customDimensionShopPage: CustomDimensionShopPage, userId: String, userType: String) {
        val eventMap: MutableMap<String, Any> = mutableMapOf(
            ShopPageTrackingConstant.EVENT to ShopPageTrackingConstant.VIEW_COMMUNICATION_IRIS,
            ShopPageTrackingConstant.EVENT_ACTION to ShopPageTrackingConstant.VIEW_SHARE_BOTTOM_SHEET,
            ShopPageTrackingConstant.EVENT_CATEGORY to ShopPageTrackingConstant.SHOP_PAGE,
            ShopPageTrackingConstant.EVENT_LABEL to "${customDimensionShopPage.shopId.orEmpty()} - $userType",
            ShopPageTrackingConstant.TRACKER_ID to ShopPageTrackingConstant.TRACKER_ID_VIEW_ON_SHARING_CHANNEL,
            ShopPageTrackingConstant.BUSINESS_UNIT to ShopPageTrackingConstant.SHARING_EXPERIENCE,
            ShopPageTrackingConstant.CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            ShopPageTrackingConstant.SHOP_ID to customDimensionShopPage.shopId.orEmpty(),
            ShopPageTrackingConstant.USER_ID to userId.ifEmpty { "0" }
        )
        sendDataLayerEvent(eventMap)
    }

    fun onImpressionScreenshotShareBottomSheet(customDimensionShopPage: CustomDimensionShopPage, userId: String, userShareType: String) {
        val eventMap: MutableMap<String, Any> = mutableMapOf(
            ShopPageTrackingConstant.EVENT to ShopPageTrackingConstant.VIEW_COMMUNICATION_IRIS,
            ShopPageTrackingConstant.EVENT_ACTION to ShopPageTrackingConstant.VIEW_SCREENSHOT_SHARE_BOTTOM_SHEET,
            ShopPageTrackingConstant.EVENT_CATEGORY to ShopPageTrackingConstant.SHOP_PAGE,
            ShopPageTrackingConstant.EVENT_LABEL to "${customDimensionShopPage.shopId.orEmpty()} - $userShareType",
            ShopPageTrackingConstant.TRACKER_ID to ShopPageTrackingConstant.TRACKER_ID_VIEW_SCREEN_SHOT_BOTTOM_SHEET,
            ShopPageTrackingConstant.BUSINESS_UNIT to ShopPageTrackingConstant.SHARING_EXPERIENCE,
            ShopPageTrackingConstant.CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            ShopPageTrackingConstant.SHOP_ID to customDimensionShopPage.shopId.orEmpty(),
            ShopPageTrackingConstant.USER_ID to userId.ifEmpty { "0" }
        )
        sendDataLayerEvent(eventMap)
    }

    fun clickCloseNewScreenshotShareBottomSheet(customDimensionShopPage: CustomDimensionShopPage, userId: String, userShareType: String) {
        val eventMap: MutableMap<String, Any> = mutableMapOf(
            ShopPageTrackingConstant.EVENT to ShopPageTrackingConstant.CLICK_COMMUNICATION,
            ShopPageTrackingConstant.EVENT_ACTION to ShopPageTrackingConstant.CLICK_CLOSE_SCREENSHOT_SHARE_BOTTOM_SHEET,
            ShopPageTrackingConstant.EVENT_CATEGORY to ShopPageTrackingConstant.SHOP_PAGE,
            ShopPageTrackingConstant.EVENT_LABEL to "${customDimensionShopPage.shopId.orEmpty()} - $userShareType",
            ShopPageTrackingConstant.TRACKER_ID to ShopPageTrackingConstant.TRACKER_ID_CLOSE_SCREEN_SHOT_SHARE_BOTTOM_SHEET,
            ShopPageTrackingConstant.BUSINESS_UNIT to ShopPageTrackingConstant.SHARING_EXPERIENCE,
            ShopPageTrackingConstant.CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            ShopPageTrackingConstant.SHOP_ID to customDimensionShopPage.shopId.orEmpty(),
            ShopPageTrackingConstant.USER_ID to userId.ifEmpty { "0" }
        )
        sendDataLayerEvent(eventMap)
    }

    fun clickScreenshotShareBottomSheetOption(socialMediaName: String, customDimensionShopPage: CustomDimensionShopPage, userId: String, userShareType: String, imageType: String) {
        val eventMap: MutableMap<String, Any> = mutableMapOf(
            ShopPageTrackingConstant.EVENT to ShopPageTrackingConstant.CLICK_COMMUNICATION,
            ShopPageTrackingConstant.EVENT_ACTION to ShopPageTrackingConstant.CLICK_SCREENSHOT_SHARE_BOTTOM_SHEET_OPTION,
            ShopPageTrackingConstant.EVENT_CATEGORY to ShopPageTrackingConstant.SHOP_PAGE,
            ShopPageTrackingConstant.EVENT_LABEL to "$socialMediaName - ${customDimensionShopPage.shopId.orEmpty()} - $userShareType - $imageType",
            ShopPageTrackingConstant.TRACKER_ID to ShopPageTrackingConstant.TRACKER_ID_SCREEN_SHOT_CLICK_SHARING_CHANNEL,
            ShopPageTrackingConstant.BUSINESS_UNIT to ShopPageTrackingConstant.SHARING_EXPERIENCE,
            ShopPageTrackingConstant.CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            ShopPageTrackingConstant.SHOP_ID to customDimensionShopPage.shopId.orEmpty(),
            ShopPageTrackingConstant.USER_ID to userId.ifEmpty { "0" }
        )
        sendDataLayerEvent(eventMap)
    }

    fun sendShopPageAutoCompleteSearchResultTracker(
        keyword: String,
        treatmentType: String,
        responseCode: String,
        navSource: String,
        shopId: String,
        totalData: Int,
        shopName: String
    ) {
        val eventLabel = "$keyword|$treatmentType|$responseCode|${ShopPageTrackingConstant.PHYSICAL_GOODS}|$navSource|$shopId|$totalData"
        val pageSource = "$shopName.$navSource.${ShopPageTrackingConstant.LOCAL_SEARCH}.$shopId"
        val relatedKeyword = "$keyword - none"
        val eventMap: MutableMap<String, Any> = mutableMapOf(
            ShopPageTrackingConstant.EVENT to ShopPageTrackingConstant.CLICK_TOP_NAV,
            ShopPageTrackingConstant.EVENT_ACTION to ShopPageTrackingConstant.ACTION_GENERAL_SEARCH,
            ShopPageTrackingConstant.EVENT_CATEGORY to ShopPageTrackingConstant.CATEGORY_TOP_NAV,
            ShopPageTrackingConstant.EVENT_LABEL to eventLabel,
            ShopPageTrackingConstant.BUSINESS_UNIT to ShopPageTrackingConstant.PHYSICAL_GOODS,
            ShopPageTrackingConstant.COMPONENT to "",
            ShopPageTrackingConstant.CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            ShopPageTrackingConstant.PAGE_SOURCE to pageSource,
            ShopPageTrackingConstant.RELATED_KEYWORD to relatedKeyword
        )
        sendDataLayerEvent(eventMap)
    }

    fun clickUniversalSharingPermission(action: String, label: String, shopId: String, userId: String) {
        val eventMap: MutableMap<String, Any> = mutableMapOf(
            ShopPageTrackingConstant.EVENT to ShopPageTrackingConstant.CLICK_COMMUNICATION,
            ShopPageTrackingConstant.EVENT_ACTION to action,
            ShopPageTrackingConstant.EVENT_CATEGORY to ShopPageTrackingConstant.SHOP_PAGE,
            ShopPageTrackingConstant.EVENT_LABEL to "$label - $shopId",
            ShopPageTrackingConstant.TRACKER_ID to ShopPageTrackingConstant.TRACKER_ID_ACCESS_MEDIA_FILES,
            ShopPageTrackingConstant.BUSINESS_UNIT to ShopPageTrackingConstant.SHARING_EXPERIENCE,
            ShopPageTrackingConstant.CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            ShopPageTrackingConstant.SHOP_ID to shopId,
            ShopPageTrackingConstant.USER_ID to userId.ifEmpty { "0" }
        )
        sendDataLayerEvent(eventMap)
    }

    fun clickScrollToTop(shopId: String, userId: String) {
        val eventMap: MutableMap<String, Any> = mutableMapOf(
            ShopPageTrackingConstant.EVENT to ShopPageTrackingConstant.CLICK_SHOP_PAGE,
            ShopPageTrackingConstant.EVENT_ACTION to ShopPageTrackingConstant.ACTION_CLICK_BACK_TO_TOP,
            ShopPageTrackingConstant.EVENT_CATEGORY to ShopPageTrackingConstant.SHOP_PAGE_BUYER,
            ShopPageTrackingConstant.EVENT_LABEL to "",
            ShopPageTrackingConstant.BUSINESS_UNIT to ShopPageTrackingConstant.PHYSICAL_GOODS,
            ShopPageTrackingConstant.CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            ShopPageTrackingConstant.SHOP_ID to shopId,
            ShopPageTrackingConstant.USER_ID to userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    fun onImpressionProductAtcDirectPurchaseButton(
        shopProductUiModel: ShopProductUiModel,
        widgetName: String,
        position: Int,
        shopId: String,
        userId: String
    ) {
        val eventBundle = Bundle().apply {
            putString(EVENT, VIEW_ITEM)
            putString(EVENT_ACTION, IMPRESSION_PRODUCT_ATC)
            putString(EVENT_CATEGORY, SHOP_PAGE_BUYER_DIRECT_PURCHASE)
            putString(EVENT_LABEL, "")
            putString(BUSINESS_UNIT, PHYSICAL_GOODS)
            putString(CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
            putString(PRODUCT_ID, shopProductUiModel.id.orEmpty())
            putString(SHOP_ID, shopId)
            putString(USER_ID, userId)
            putParcelableArrayList(
                PROMOTIONS,
                arrayListOf(
                    createProductAtcDirectPurchaseButtonPromotions(
                        widgetName,
                        position,
                        shopProductUiModel
                    )
                )
            )
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(VIEW_ITEM, eventBundle)
    }

    private fun createProductAtcDirectPurchaseButtonPromotions(
        widgetName: String,
        position: Int,
        shopProductUiModel: ShopProductUiModel
    ): Bundle {
        return Bundle().apply {
            putString(CREATIVE_NAME, widgetName)
            putInt(CREATIVE_SLOT, position)
            putString(ITEM_ID, shopProductUiModel.id.orEmpty())
            putString(ITEM_NAME, shopProductUiModel.name.orEmpty())
        }
    }

    fun onClickProductAtcDirectPurchaseButton(
        atcTrackerModel: ShopPageAtcTracker,
        shopId: String,
        shopType: String,
        shopName: String,
        userId: String
    ) {
        val eventBundle = Bundle().apply {
            putString(EVENT, DIRECT_PURCHASE_ADD_TO_CART)
            putString(EVENT_ACTION, CLICK_PRODUCT_ATC)
            putString(EVENT_CATEGORY, SHOP_PAGE_BUYER_DIRECT_PURCHASE)
            putString(EVENT_LABEL, atcTrackerModel.componentName)
            putString(TRACKER_ID, TRACKER_ID_ATC_CLICK)
            putString(BUSINESS_UNIT, PHYSICAL_GOODS)
            putString(CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
            putParcelableArrayList(
                ITEMS,
                arrayListOf(
                    createClickProductAtcDirectPurchaseButtonItems(
                        atcTrackerModel,
                        shopId,
                        shopName,
                        shopType
                    )
                )
            )
            putString(PRODUCT_ID, atcTrackerModel.productId)
            putString(SHOP_ID, shopId)
            putString(USER_ID, userId)
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(DIRECT_PURCHASE_ADD_TO_CART, eventBundle)
    }

    private fun createClickProductAtcDirectPurchaseButtonItems(
        atcTrackerModel: ShopPageAtcTracker,
        shopId: String,
        shopName: String,
        shopType: String
    ): Bundle {
        return Bundle().apply {
            putString(CATEGORY_ID, NONE)
            putString(DIMENSION_45, atcTrackerModel.cartId)
            putString(ITEM_BRAND, NONE)
            putString(ITEM_CATEGORY, NONE)
            putString(ITEM_ID, atcTrackerModel.productId)
            putString(ITEM_NAME, atcTrackerModel.productName)
            putString(ITEM_VARIANT, atcTrackerModel.isVariant.toString())
            putLong(PRICE, atcTrackerModel.productPrice.digitsOnly().orZero())
            putInt(QUANTITY, atcTrackerModel.quantity)
            putString(ITEMS_SHOP_ID, shopId)
            putString(SHOP_NAME, shopName)
            putString(ITEMS_SHOP_TYPE, shopType)
        }
    }

    fun onClickProductAtcQuantityButton(
        atcTrackerModel: ShopPageAtcTracker,
        shopId: String,
        userId: String
    ) {
        val quantityType = when (atcTrackerModel.atcType) {
            ShopPageAtcTracker.AtcType.UPDATE_ADD -> {
                SHOP_PRODUCT_ATC_QUANTITY_INCREASE
            }
            else -> {
                SHOP_PRODUCT_ATC_QUANTITY_DECREASE
            }
        }
        val eventLabel = "${atcTrackerModel.componentName} - $quantityType"
        val eventMap = mapOf(
            EVENT to CLICK_PG,
            EVENT_ACTION to CLICK_PRODUCT_ATC_QUANTITY,
            EVENT_CATEGORY to SHOP_PAGE_BUYER_DIRECT_PURCHASE,
            EVENT_LABEL to eventLabel,
            TRACKER_ID to TRACKER_ID_ATC_CLICK_QUANTITY,
            BUSINESS_UNIT to PHYSICAL_GOODS,
            CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            PRODUCT_ID to atcTrackerModel.productId,
            SHOP_ID to shopId,
            USER_ID to userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    fun onClickProductAtcTrashButton(
        atcTrackerModel: ShopPageAtcTracker,
        shopId: String,
        userId: String
    ) {
        val eventMap = mapOf(
            EVENT to CLICK_PG,
            EVENT_ACTION to CLICK_PRODUCT_ATC_RESET,
            EVENT_CATEGORY to SHOP_PAGE_BUYER_DIRECT_PURCHASE,
            EVENT_LABEL to atcTrackerModel.componentName,
            TRACKER_ID to TRACKER_ID_ATC_CLICK_DELETE,
            BUSINESS_UNIT to PHYSICAL_GOODS,
            CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            PRODUCT_ID to atcTrackerModel.productId,
            SHOP_ID to shopId,
            USER_ID to userId,
            DIMENSION_45 to atcTrackerModel.cartId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    fun sendOpenScreenShopCampaignTab(shopId: String, userId: String, isLogin: Boolean) {
        val loginNonLoginString = if (isLogin) ShopPageTrackingConstant.LOGIN else ShopPageTrackingConstant.NON_LOGIN
        val eventMap = mapOf(
            EVENT to OPEN_SCREEN,
            TRACKER_ID to TRACKER_ID_OPEN_SCREEN_CAMPAIGN_TAB,
            BUSINESS_UNIT to PHYSICAL_GOODS,
            CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            IS_LOGGED_IN_STATUS to loginNonLoginString,
            SHOP_ID to shopId,
            USER_ID to userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    fun clickChipsInUniversalSharingBottomSheet(
        chipsValue: String,
        shopId: String,
        userId: String
    ) {
        val eventMap: MutableMap<String, Any> = mutableMapOf(
            ShopPageTrackingConstant.EVENT to ShopPageTrackingConstant.CLICK_COMMUNICATION,
            ShopPageTrackingConstant.EVENT_ACTION to ShopPageTrackingConstant.CLICK_SHARE_CHIPS,
            ShopPageTrackingConstant.EVENT_CATEGORY to ShopPageTrackingConstant.SHOP_PAGE,
            ShopPageTrackingConstant.EVENT_LABEL to "$shopId - $chipsValue",
            ShopPageTrackingConstant.TRACKER_ID to ShopPageTrackingConstant.TRACKER_ID_CLICK_CHIPS_TAB_SHOP_PAGE,
            ShopPageTrackingConstant.BUSINESS_UNIT to ShopPageTrackingConstant.SHARING_EXPERIENCE,
            ShopPageTrackingConstant.CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            ShopPageTrackingConstant.SHOP_ID to shopId,
            ShopPageTrackingConstant.USER_ID to userId.ifEmpty { "0" }
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }
}
