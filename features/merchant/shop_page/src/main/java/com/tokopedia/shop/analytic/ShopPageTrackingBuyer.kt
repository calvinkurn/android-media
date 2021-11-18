package com.tokopedia.shop.analytic

import android.text.TextUtils
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.shop.analytic.model.CustomDimensionShopPageAttribution
import com.tokopedia.shop.analytic.model.CustomDimensionShopPageProduct
import com.tokopedia.shop.common.constant.*
import com.tokopedia.shop.common.util.ShopProductViewGridType
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.product.view.datamodel.ShopProductUiModel
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue
import java.util.*
import kotlin.collections.HashMap

class ShopPageTrackingBuyer(
        trackingQueue: TrackingQueue?) : ShopPageTracking(trackingQueue!!) {
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
            isFreeOngkirActive: Boolean?
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
            val event = HashMap(DataLayer.mapOf(
                    ShopPageTrackingConstant.NAME, viewModel.name,
                    ShopPageTrackingConstant.ID, viewModel.id,
                    ShopPageTrackingConstant.PRICE, formatPrice(viewModel.displayedPrice!!),
                    ShopPageTrackingConstant.BRAND, ShopPageTrackingConstant.NONE,
                    ShopPageTrackingConstant.CATEGORY, ShopPageTrackingConstant.NONE,
                    ShopPageTrackingConstant.VARIANT, ShopPageTrackingConstant.NONE,
                    ShopPageTrackingConstant.LIST, joinDash(SHOPPAGE, shopId, getProductEtalaseEvent(selectedEtalaseName, etalaseName), loginNonLoginString),
                    ShopPageTrackingConstant.POSITION, productPosition,
                    ShopPageTrackingConstant.DIMENSION_81, shopTypeDef,
                    ShopPageTrackingConstant.DIMENSION_79, shopId,
                    ShopPageTrackingConstant.DIMENSION_90, shopRef,
                    ShopPageTrackingConstant.DIMENSION_83, boe
            ))
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
            dimension90: String
    ): List<Any> {
        val list: MutableList<Any> = ArrayList()
        for (i in shopProductUiModelList.indices) {
            val viewModel = shopProductUiModelList[i]
            val event = HashMap(DataLayer.mapOf(
                    ShopPageTrackingConstant.NAME, viewModel.name,
                    ShopPageTrackingConstant.ID, viewModel.id,
                    ShopPageTrackingConstant.PRICE, formatPrice(viewModel.displayedPrice!!),
                    ShopPageTrackingConstant.BRAND, ShopPageTrackingConstant.NONE,
                    ShopPageTrackingConstant.CATEGORY, ShopPageTrackingConstant.NONE,
                    ShopPageTrackingConstant.VARIANT, ShopPageTrackingConstant.NONE,
                    ShopPageTrackingConstant.LIST, joinDash(SHOPPAGE, shopId, getProductEtalaseEvent(selectedEtalaseName, etalaseName), loginNonLoginString, ShopPageTrackingConstant.SEARCH_RESULT),
                    ShopPageTrackingConstant.POSITION, productPosition,
                    ShopPageTrackingConstant.DIMENSION_81, shopTypeDef,
                    ShopPageTrackingConstant.DIMENSION_79, shopId,
                    ShopPageTrackingConstant.DIMENSION_90, dimension90
            ))
            list.add(event)
        }
        return list
    }

    private fun createProductImpressionMap(
            event: String, isOwner: Boolean, category: String, loginNonLoginString: String, action: String, label: String,
            customDimensionShopPage: CustomDimensionShopPageAttribution,
            shopProductUiModel: ShopProductUiModel,
            selectedEtalaseChipName: String, etalaseName: String,
            productPositionStart: Int,
            shopId: String
    ): HashMap<String, Any> {
        val shopProductUiModelArrayList = ArrayList<ShopProductUiModel>()
        shopProductUiModelArrayList.add(shopProductUiModel)
        val eventMap = createMap(event, category, action, label, customDimensionShopPage)
        eventMap[ShopPageTrackingConstant.ECOMMERCE] = DataLayer.mapOf(
                ShopPageTrackingConstant.CURRENCY_CODE, ShopPageTrackingConstant.IDR,
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
                        customDimensionShopPage.isFreeOngkirActive
                ))
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
            etalaseName: String, productPositionStart: Int,
            shopId: String,
            shopName: String,
            navSource: String
    ): HashMap<String, Any> {
        val shopProductUiModelArrayList = ArrayList<ShopProductUiModel>()
        shopProductUiModelArrayList.add(shopProductUiModel)
        val eventMap = createMap(event, category, action, label, customDimensionShopPage)
        val dimension90Value = "$shopName.$navSource.local_search.$shopId"
        eventMap[ShopPageTrackingConstant.ECOMMERCE] = DataLayer.mapOf(
                ShopPageTrackingConstant.CURRENCY_CODE, ShopPageTrackingConstant.IDR,
                ShopPageTrackingConstant.IMPRESSIONS,
                createProductListSearchResultMap(
                        shopProductUiModelArrayList,
                        selectedEtalaseChipName,
                        etalaseName,
                        productPositionStart,
                        customDimensionShopPage.shopType,
                        loginNonLoginString,
                        shopId,
                        dimension90Value
                ))
        return eventMap
    }

    private fun createProductClickMap(event: String, isOwner: Boolean, category: String, loginNonLoginString: String, action: String, label: String?,
                                      customDimensionShopPage: CustomDimensionShopPageAttribution,
                                      shopProductUiModel: ShopProductUiModel,
                                      selectedEtalaseChipName: String, etalaseName: String,
                                      productPositionStart: Int,
                                      shopId: String): HashMap<String, Any> {
        val shopProductUiModelArrayList = ArrayList<ShopProductUiModel>()
        shopProductUiModelArrayList.add(shopProductUiModel)
        val eventMap = createMap(event, category, action, label, customDimensionShopPage)
        eventMap[ShopPageTrackingConstant.ECOMMERCE] = DataLayer.mapOf(
                ShopPageTrackingConstant.CLICK,
                DataLayer.mapOf(
                        ShopPageTrackingConstant.ACTION_FIELD, DataLayer.mapOf(ShopPageTrackingConstant.LIST, joinDash(SHOPPAGE, shopId, getProductEtalaseEvent(selectedEtalaseChipName, etalaseName), loginNonLoginString)),
                        ShopPageTrackingConstant.PRODUCTS, createProductListMap(
                        shopProductUiModelArrayList,
                        selectedEtalaseChipName,
                        etalaseName,
                        productPositionStart,
                        customDimensionShopPage.shopType,
                        loginNonLoginString,
                        shopId,
                        customDimensionShopPage.shopRef,
                        customDimensionShopPage.isFulfillmentExist,
                        customDimensionShopPage.isFreeOngkirActive
                ))
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
            etalaseName: String, productPositionStart: Int,
            shopId: String,
            shopName: String,
            navSource: String
    ): HashMap<String, Any> {
        val shopProductUiModelArrayList = ArrayList<ShopProductUiModel>()
        shopProductUiModelArrayList.add(shopProductUiModel)
        val eventMap = createMap(event, category, action, label, customDimensionShopPage)
        val dimension90Value = "$shopName.$navSource.local_search.$shopId"
        eventMap[ShopPageTrackingConstant.ECOMMERCE] = DataLayer.mapOf(
                ShopPageTrackingConstant.CLICK,
                DataLayer.mapOf(
                        ShopPageTrackingConstant.ACTION_FIELD, DataLayer.mapOf(
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
                        ShopPageTrackingConstant.PRODUCTS, createProductListSearchResultMap(
                        shopProductUiModelArrayList,
                        selectedEtalaseChipName,
                        etalaseName,
                        productPositionStart,
                        customDimensionShopPage.shopType,
                        loginNonLoginString,
                        shopId,
                        dimension90Value
                ))
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

    fun impressionCoachMarkDissapearFollowUnfollowShop(
            shopId: String,
            userId: String?
    ) {
        followUnfollowShop(
                ShopPageTrackingConstant.VIEW_SHOP_PAGE_IRIS,
                ShopPageTrackingConstant.VIEW_COACHMARK_FOLLOW,
                ShopPageTrackingConstant.SHOP_PAGE_LABEL + shopId + ShopPageTrackingConstant.COACHMARK_DISAPPEAR,
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
            customDimensionShopPage: CustomDimensionShopPage?) {
        val followUnfollow = if (isFollow) ShopPageTrackingConstant.FOLLOW else ShopPageTrackingConstant.UNFOLLOW
        sendGeneralEvent(ShopPageTrackingConstant.CLICK_SHOP_PAGE,
                ShopPageTrackingConstant.SHOP_PAGE_BUYER,
                joinSpace(ShopPageTrackingConstant.CLICK, followUnfollow),
                "",
                customDimensionShopPage)
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
                ShopPageTrackingConstant.SHOP_PAGE_BUYER, String.format(ShopPageTrackingConstant.CLICK_SHOWCASE_FOLLOW_NPL, ShopPageTrackingConstant.FOLLOW),
                shopId,
                ShopPageTrackingConstant.PHYSICAL_GOODS,
                ShopPageTrackingConstant.TOKOPEDIA_MARKETPLACE,
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
                ShopPageTrackingConstant.TOKOPEDIA_MARKETPLACE,
                userId,
                customDimensionShopPage
        )
    }

    fun clickMessageSeller(customDimensionShopPage: CustomDimensionShopPage?) {
        sendGeneralEvent(ShopPageTrackingConstant.CLICK_SHOP_PAGE,
                ShopPageTrackingConstant.SHOP_PAGE_BUYER,
                ShopPageTrackingConstant.CLICK_CHAT_SELLER,
                "",
                customDimensionShopPage)
    }

    fun clickProduct(
            isOwner: Boolean,
            isLogin: Boolean,
            selectedEtalaseChipName: String?,
            etalaseSection: String,
            customDimensionShopPage: CustomDimensionShopPageAttribution,
            shopProductUiModel: ShopProductUiModel,
            productPosStart: Int,
            shopId: String,
            isSelectedEtalaseCampaign: Boolean,
            isEtalaseSectionCampaign: Boolean,
            isUpcoming: Boolean
    ) {
        val loginNonLoginString = if (isLogin) ShopPageTrackingConstant.LOGIN else ShopPageTrackingConstant.NON_LOGIN
        val etalaseNameTrackerString = if (isSelectedEtalaseCampaign) String.format(ShopPageTrackingConstant.LABEL_ETALASE_CAMPAIGN, selectedEtalaseChipName) else selectedEtalaseChipName!!
        val etalaseSectionTrackerString = getEtalaseNameTrackerString(isEtalaseSectionCampaign, isUpcoming, etalaseSection, ShopPageConstant.DEFAULT_VALUE_ETALASE_TYPE)
        val event: Map<String, Any> = createProductClickMap(
                ShopPageTrackingConstant.PRODUCT_CLICK,
                isOwner,
                getShopPageCategory(isOwner),
                loginNonLoginString,
                joinDash(ShopPageTrackingConstant.CLICK_PRODUCT, getProductEtalaseEvent(etalaseNameTrackerString, etalaseSectionTrackerString), loginNonLoginString),
                shopProductUiModel.id,
                customDimensionShopPage,
                shopProductUiModel,
                etalaseNameTrackerString, etalaseSectionTrackerString,
                productPosStart,
                shopId
        )
        sendDataLayerEvent(event)
    }

    fun clickProductSearchResult(
            isOwner: Boolean,
            isLogin: Boolean,
            selectedEtalaseChipName: String,
            etalaseSection: String,
            customDimensionShopPage: CustomDimensionShopPageAttribution,
            shopProductUiModel: ShopProductUiModel,
            productPosStart: Int,
            shopId: String,
            isEtalaseCampaign: Boolean,
            isUpcoming: Boolean,
            keyword: String,
            etalaseType: Int,
            shopName: String,
            navSource: String
    ) {
        val loginNonLoginString = if (isLogin) ShopPageTrackingConstant.LOGIN else ShopPageTrackingConstant.NON_LOGIN
        val etalaseNameTrackerString = getEtalaseNameTrackerString(isEtalaseCampaign, isUpcoming, selectedEtalaseChipName, etalaseType)
        val event: Map<String, Any> = createProductClickSearchResultMap(
                ShopPageTrackingConstant.PRODUCT_CLICK,
                getShopPageCategory(isOwner),
                loginNonLoginString,
                joinDash(
                        ShopPageTrackingConstant.CLICK_PRODUCT,
                        getProductEtalaseEvent(etalaseNameTrackerString, etalaseSection),
                        loginNonLoginString,
                        ShopPageTrackingConstant.SEARCH_RESULT),
                keyword,
                customDimensionShopPage,
                shopProductUiModel,
                etalaseNameTrackerString,
                etalaseSection,
                productPosStart,
                shopId,
                shopName,
                navSource
        )
        sendDataLayerEvent(event)
    }

    fun clickProductListEmptyState(isLogin: Boolean,
                                   customDimensionShopPage: CustomDimensionShopPageAttribution,
                                   shopProductUiModel: ShopProductUiModel,
                                   productPosStart: Int,
                                   shopId: String) {
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

    private fun createProductClickEmptyStateMap(event: String, category: String, action: String, label: String,
                                                loginNonLoginString: String,
                                                customDimensionShopPage: CustomDimensionShopPageAttribution,
                                                shopProductUiModel: ShopProductUiModel,
                                                productPositionStart: Int,
                                                shopId: String): HashMap<String, Any> {
        val shopProductUiModelArrayList = ArrayList<ShopProductUiModel>()
        shopProductUiModelArrayList.add(shopProductUiModel)
        val eventMap = createMap(event, category, action, label, customDimensionShopPage)
        eventMap[ShopPageTrackingConstant.ECOMMERCE] = DataLayer.mapOf(
                ShopPageTrackingConstant.CLICK,
                DataLayer.mapOf(
                        ShopPageTrackingConstant.ACTION_FIELD, DataLayer.mapOf(ShopPageTrackingConstant.LIST, joinDash(SHOPPAGE, shopId, ShopPageTrackingConstant.SEARCH_NO_RESULT_SUGGESTION, loginNonLoginString)),
                        ShopPageTrackingConstant.PRODUCTS, createProductListMapEmptyState(
                        shopProductUiModelArrayList,
                        productPositionStart,
                        customDimensionShopPage.shopType,
                        loginNonLoginString,
                        shopId,
                        customDimensionShopPage.shopRef
                ))
        )
        return eventMap
    }

    fun impressionProductList(
            isOwner: Boolean,
            isLogin: Boolean,
            selectedEtalaseChipName: String?,
            etalaseSection: String,
            customDimensionShopPage: CustomDimensionShopPageAttribution,
            shopProductUiModel: ShopProductUiModel,
            productPosStart: Int,
            shopId: String,
            isSelectedEtalaseCampaign: Boolean,
            isEtalaseSectionCampaign: Boolean,
            isUpcoming: Boolean
    ) {
        val loginNonLoginString = if (isLogin) ShopPageTrackingConstant.LOGIN else ShopPageTrackingConstant.NON_LOGIN
        val etalaseNameTrackerString = if (isSelectedEtalaseCampaign) String.format(ShopPageTrackingConstant.LABEL_ETALASE_CAMPAIGN, selectedEtalaseChipName) else selectedEtalaseChipName!!
        val etalaseSectionTrackerString = getEtalaseNameTrackerString(isEtalaseSectionCampaign, isUpcoming, etalaseSection, ShopPageConstant.DEFAULT_VALUE_ETALASE_TYPE)
        val event: Map<String, Any> = createProductImpressionMap(
                ShopPageTrackingConstant.PRODUCT_VIEW,
                isOwner,
                getShopPageCategory(isOwner),
                loginNonLoginString,
                joinDash(ShopPageTrackingConstant.PRODUCT_LIST_IMPRESSION, getProductEtalaseEvent(etalaseNameTrackerString, etalaseSectionTrackerString), loginNonLoginString),
                "",
                customDimensionShopPage,
                shopProductUiModel,
                etalaseNameTrackerString, etalaseSectionTrackerString,
                productPosStart,
                shopId
        )
        sendDataLayerEvent(event)
    }

    fun impressionProductListSearchResult(
            isOwner: Boolean,
            isLogin: Boolean,
            selectedEtalaseChipName: String,
            etalaseSection: String,
            customDimensionShopPage: CustomDimensionShopPageAttribution,
            shopProductUiModel: ShopProductUiModel,
            productPosStart: Int,
            shopId: String,
            isEtalaseCampaign: Boolean,
            isUpcoming: Boolean,
            keyword: String,
            etalaseType: Int,
            shopName: String,
            navSource: String
    ) {
        val loginNonLoginString = if (isLogin) ShopPageTrackingConstant.LOGIN else ShopPageTrackingConstant.NON_LOGIN
        val etalaseNameTrackerString = getEtalaseNameTrackerString(isEtalaseCampaign, isUpcoming, selectedEtalaseChipName, etalaseType)
        val event: Map<String, Any> = createProductImpressionSearchResultMap(
                ShopPageTrackingConstant.PRODUCT_VIEW,
                getShopPageCategory(isOwner),
                loginNonLoginString,
                joinDash(ShopPageTrackingConstant.PRODUCT_LIST_IMPRESSION, getProductEtalaseEvent(etalaseNameTrackerString, etalaseSection), loginNonLoginString, ShopPageTrackingConstant.SEARCH_RESULT),
                keyword,
                customDimensionShopPage,
                shopProductUiModel,
                etalaseNameTrackerString,
                etalaseSection, productPosStart,
                shopId,
                shopName,
                navSource
        )
        sendDataLayerEvent(event)
    }

    fun clickWishlist(isAdd: Boolean,
                      isLogin: Boolean,
                      selectedEtalaseName: String?,
                      sectionName: String,
                      customDimensionShopPage: CustomDimensionShopPageProduct,
                      isSelectedEtalaseCampaign: Boolean,
                      isEtalaseSectionCampaign: Boolean,
                      isUpcoming: Boolean) {
        val loginNonLoginString = if (isLogin) ShopPageTrackingConstant.LOGIN else ShopPageTrackingConstant.NON_LOGIN
        val etalaseNameTrackerString = if (isSelectedEtalaseCampaign) String.format(ShopPageTrackingConstant.LABEL_ETALASE_CAMPAIGN, selectedEtalaseName) else selectedEtalaseName!!
        val etalaseSectionTrackerString = getEtalaseNameTrackerString(isEtalaseSectionCampaign, isUpcoming, sectionName, ShopPageConstant.DEFAULT_VALUE_ETALASE_TYPE)
        val etalaseEvent = joinDash(getProductEtalaseEvent(etalaseNameTrackerString, etalaseSectionTrackerString), loginNonLoginString)
        sendGeneralEvent(ShopPageTrackingConstant.CLICK_SHOP_PAGE,
                ShopPageTrackingConstant.SHOP_PAGE_BUYER,
                joinDash(joinSpace(if (isAdd) ShopPageTrackingConstant.ADD else ShopPageTrackingConstant.REMOVE, ShopPageTrackingConstant.WISHLIST), etalaseEvent),
                customDimensionShopPage.productId,
                customDimensionShopPage)
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
        sendGeneralEvent(ShopPageTrackingConstant.CLICK_SHOP_PAGE,
                ShopPageTrackingConstant.SHOP_PAGE_BUYER,
                joinDash(joinSpace(if (isAdd) ShopPageTrackingConstant.ADD else ShopPageTrackingConstant.REMOVE, ShopPageTrackingConstant.WISHLIST), etalaseEvent),
                customDimensionShopPage.productId,
                customDimensionShopPage)
    }

    fun eventShopSendChat() {
        TrackApp.getInstance().gtm.sendGeneralEvent(ShopPageTrackingConstant.CLICK_SHOP_MESSAGE,
                SHOP_PAGE, ShopPageTrackingConstant.CLICK_SEND_CHAT, "")
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
        sendGeneralEvent(ShopPageTrackingConstant.CLICK_SHOP_PAGE,
                ShopPageTrackingConstant.SHOP_PROFILE_PAGE_BUYER,
                ShopPageTrackingConstant.CLICK_SHARE,
                "",
                customDimensionShopPage)
    }

    fun clickShareButtonSellerView(customDimensionShopPage: CustomDimensionShopPage?) {
        sendGeneralEvent(ShopPageTrackingConstant.CLICK_SHOP_PAGE,
                ShopPageTrackingConstant.SETTING_PAGE_SELLER,
                ShopPageTrackingConstant.CLICK_SHARE,
                "",
                customDimensionShopPage)
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
                ShopPageTrackingConstant.CURRENT_SITE to ShopPageTrackingConstant.TOKOPEDIA_MARKETPLACE,
                ShopPageTrackingConstant.SHOP_ID to shopId,
                ShopPageTrackingConstant.USER_ID to userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    fun clickShopProfile(customDimensionShopPage: CustomDimensionShopPage?) {
        sendGeneralEvent(ShopPageTrackingConstant.CLICK_SHOP_PAGE,
                ShopPageTrackingConstant.SHOP_PAGE_BUYER,
                ShopPageTrackingConstant.CLICK_SHOP_PROFILE,
                "",
                customDimensionShopPage)
    }

    fun clickFollowUnfollow(isShopFavorited: Boolean, customDimensionShopPage: CustomDimensionShopPage?) {
        val action: String
        action = if (!isShopFavorited) {
            ShopPageTrackingConstant.CLICK_FOLLOW
        } else {
            ShopPageTrackingConstant.CLICK_UNFOLLOW
        }
        sendGeneralEvent(ShopPageTrackingConstant.CLICK_SHOP_PAGE,
                ShopPageTrackingConstant.SHOP_PAGE_BUYER,
                action,
                "",
                customDimensionShopPage)
    }

    fun clickMoreMenuChip(isOwner: Boolean,
                          selectedEtalaseName: String?,
                          customDimensionShopPage: CustomDimensionShopPage?) {
        sendGeneralEvent(ShopPageTrackingConstant.CLICK_SHOP_PAGE,
                getShopPageCategory(isOwner),
                ShopPageTrackingConstant.CLICK_SHOWCASE_LIST, String.format(ShopPageTrackingConstant.ETALASE_X, selectedEtalaseName),
                customDimensionShopPage)
    }

    fun sendMoEngageFavoriteEvent(shopName: String?, shopID: String?, shopDomain: String?, shopLocation: String?,
                                  isShopOfficaial: Boolean?, isFollowed: Boolean) {
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

    fun searchProduct(
            keyword: String?,
            isProductSearchResultEmpty: Boolean,
            isOwner: Boolean,
            customDimensionShopPage: CustomDimensionShopPage?
    ) {
        val productResultLabel = if (isProductSearchResultEmpty) ShopPageTrackingConstant.SEARCH_PRODUCT_NO_RESULT else ShopPageTrackingConstant.SEARCH_PRODUCT_RESULT
        sendGeneralEvent(
                ShopPageTrackingConstant.CLICK_SHOP_PAGE,
                getShopPageCategory(isOwner),
                ShopPageTrackingConstant.SEARCH_PRODUCT,
                joinSpace(ShopPageTrackingConstant.SEARCH, joinDash(keyword, productResultLabel)),
                customDimensionShopPage
        )
    }

    fun clickAddEtalase(customDimensionShopPage: CustomDimensionShopPage?) {
        sendGeneralEvent(
                ShopPageTrackingConstant.CLICK_SHOP_PAGE,
                ShopPageTrackingConstant.SHOP_PAGE_SELLER,
                ShopPageTrackingConstant.CLICK_ADD_ETALASE_BUTTON,
                "",
                customDimensionShopPage
        )
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
        sendGeneralEvent(ShopPageTrackingConstant.CLICK_SHOP_PAGE,
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
            val event = HashMap(DataLayer.mapOf(
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
            ))
            list.add(event)
        }
        return list
    }

    private fun createProductImpressionMapEmptyState(
            event: String, category: String, action: String, label: String,
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
                ShopPageTrackingConstant.CURRENCY_CODE, ShopPageTrackingConstant.IDR,
                ShopPageTrackingConstant.IMPRESSIONS,
                createProductListMapEmptyState(
                        shopProductUiModelArrayList,
                        productPositionStart,
                        customDimensionShopPage.shopType,
                        loginNonLoginString,
                        shopId,
                        customDimensionShopPage.shopRef
                ))
        return eventMap
    }

    fun impressionProductListEmptyState(isLogin: Boolean,
                                        customDimensionShopPage: CustomDimensionShopPageAttribution,
                                        shopProductUiModel: ShopProductUiModel,
                                        productPosStart: Int,
                                        shopId: String) {
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
        TrackApp.getInstance().gtm.sendGeneralEvent(DataLayer.mapOf(
                ShopPageTrackingConstant.EVENT, ShopPageTrackingConstant.CLICK_SHOP_PAGE,
                ShopPageTrackingConstant.EVENT_CATEGORY, ShopPageTrackingConstant.SHOP_PAGE_BUYER,
                ShopPageTrackingConstant.EVENT_ACTION, ShopPageTrackingConstant.CLICK_SEARCH_PAGE_NO_RESULT,
                ShopPageTrackingConstant.EVENT_LABEL, ShopPageTrackingConstant.TRY_ANOTHER_WORD,
                ShopPageTrackingConstant.SHOP_ID, shopId,
                ShopPageTrackingConstant.SHOP_TYPE, shopType,
                ShopPageTrackingConstant.PAGE_TYPE, SHOPPAGE
        ))
    }

    fun clickSecondaryBtnEmptyStateSearch(shopId: String?, shopType: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(DataLayer.mapOf(
                ShopPageTrackingConstant.EVENT, ShopPageTrackingConstant.CLICK_SHOP_PAGE,
                ShopPageTrackingConstant.EVENT_CATEGORY, ShopPageTrackingConstant.SHOP_PAGE_BUYER,
                ShopPageTrackingConstant.EVENT_ACTION, ShopPageTrackingConstant.CLICK_SEARCH_PAGE_NO_RESULT,
                ShopPageTrackingConstant.EVENT_LABEL, ShopPageTrackingConstant.SEARCH_ON_TOKOPEDIA,
                ShopPageTrackingConstant.SHOP_ID, shopId,
                ShopPageTrackingConstant.SHOP_TYPE, shopType,
                ShopPageTrackingConstant.PAGE_TYPE, SHOPPAGE
        ))
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
                ShopPageTrackingConstant.CURRENT_SITE to ShopPageTrackingConstant.TOKOPEDIA_MARKETPLACE,
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
            shopId: String,
            userId: String
    ) {
        var eventLabel = ShopPageTrackingConstant.LABEL_CLICK_APPLY_FILTER_CHIP
        if(selectedSortName.isNotBlank()){
            eventLabel+= " - $selectedSortName"
        }
        if (!selectedFilterMap[PMAX_PARAM_KEY].isNullOrBlank() || !selectedFilterMap[PMIN_PARAM_KEY].isNullOrBlank()) {
            val minPrice = selectedFilterMap[PMIN_PARAM_KEY] ?: "0"
            val maxPrice = selectedFilterMap[PMAX_PARAM_KEY] ?: "0"
            eventLabel+= " - $minPrice - $maxPrice"
        }
        if (!selectedFilterMap[RATING_PARAM_KEY].isNullOrBlank()) {
            val rating = selectedFilterMap[RATING_PARAM_KEY] ?: "0"
            eventLabel+= " - $rating"
        }
        val eventMap: MutableMap<String, Any> = mutableMapOf(
                ShopPageTrackingConstant.EVENT to ShopPageTrackingConstant.CLICK_SHOP_PAGE,
                ShopPageTrackingConstant.EVENT_ACTION to ShopPageTrackingConstant.CLICK_FILTER_CHIP,
                ShopPageTrackingConstant.EVENT_CATEGORY to ShopPageTrackingConstant.SHOP_PAGE_BUYER,
                ShopPageTrackingConstant.EVENT_LABEL to eventLabel,
                ShopPageTrackingConstant.BUSINESS_UNIT to ShopPageTrackingConstant.PHYSICAL_GOODS,
                ShopPageTrackingConstant.CURRENT_SITE to ShopPageTrackingConstant.TOKOPEDIA_MARKETPLACE,
                ShopPageTrackingConstant.SHOP_ID to shopId,
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
                getShopPageCategory(isMyShop), String.format(eventActionFormat, keyword),
                keyword,
                customDimensionShopPage
        )
    }

    fun clickShareButtonNewBottomSheet(
            customDimensionShopPage: CustomDimensionShopPage,
            userId: String
    ) {
        val eventMap: MutableMap<String, Any> = mutableMapOf(
                ShopPageTrackingConstant.EVENT to ShopPageTrackingConstant.CLICK_SHOP_PAGE,
                ShopPageTrackingConstant.EVENT_ACTION to ShopPageTrackingConstant.CLICK_SHARE_BUTTON,
                ShopPageTrackingConstant.EVENT_CATEGORY to ShopPageTrackingConstant.SHOP_PAGE,
                ShopPageTrackingConstant.EVENT_LABEL to "",
                ShopPageTrackingConstant.BUSINESS_UNIT to ShopPageTrackingConstant.SHARING_EXPERIENCE,
                ShopPageTrackingConstant.CURRENT_SITE to ShopPageTrackingConstant.TOKOPEDIA_MARKETPLACE,
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
                ShopPageTrackingConstant.CURRENT_SITE to ShopPageTrackingConstant.TOKOPEDIA_MARKETPLACE,
                ShopPageTrackingConstant.SHOP_ID to customDimensionShopPage.shopId.orEmpty(),
                ShopPageTrackingConstant.USER_ID to userId.ifEmpty { "0" }
        )
        sendDataLayerEvent(eventMap)
    }

    fun clickCloseNewShareBottomSheet(customDimensionShopPage: CustomDimensionShopPage, userId: String) {
        val eventMap: MutableMap<String, Any> = mutableMapOf(
                ShopPageTrackingConstant.EVENT to ShopPageTrackingConstant.CLICK_SHOP_PAGE,
                ShopPageTrackingConstant.EVENT_ACTION to ShopPageTrackingConstant.CLICK_CLOSE_SHARE_BOTTOM_SHEET,
                ShopPageTrackingConstant.EVENT_CATEGORY to ShopPageTrackingConstant.SHOP_PAGE,
                ShopPageTrackingConstant.EVENT_LABEL to "",
                ShopPageTrackingConstant.BUSINESS_UNIT to ShopPageTrackingConstant.SHARING_EXPERIENCE,
                ShopPageTrackingConstant.CURRENT_SITE to ShopPageTrackingConstant.TOKOPEDIA_MARKETPLACE,
                ShopPageTrackingConstant.SHOP_ID to customDimensionShopPage.shopId.orEmpty(),
                ShopPageTrackingConstant.USER_ID to userId.ifEmpty { "0" }
        )
        sendDataLayerEvent(eventMap)
    }

    fun clickShareBottomSheetOption(socialMediaName: String, customDimensionShopPage: CustomDimensionShopPage, userId: String) {
        val eventMap: MutableMap<String, Any> = mutableMapOf(
                ShopPageTrackingConstant.EVENT to ShopPageTrackingConstant.CLICK_SHOP_PAGE,
                ShopPageTrackingConstant.EVENT_ACTION to ShopPageTrackingConstant.CLICK_SHARE_BOTTOM_SHEET_OPTION,
                ShopPageTrackingConstant.EVENT_CATEGORY to ShopPageTrackingConstant.SHOP_PAGE,
                ShopPageTrackingConstant.EVENT_LABEL to socialMediaName,
                ShopPageTrackingConstant.BUSINESS_UNIT to ShopPageTrackingConstant.SHARING_EXPERIENCE,
                ShopPageTrackingConstant.CURRENT_SITE to ShopPageTrackingConstant.TOKOPEDIA_MARKETPLACE,
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
                ShopPageTrackingConstant.CURRENT_SITE to ShopPageTrackingConstant.TOKOPEDIA_MARKETPLACE,
                ShopPageTrackingConstant.SHOP_ID to customDimensionShopPage.shopId.orEmpty(),
                ShopPageTrackingConstant.USER_ID to userId.ifEmpty { "0" }
        )
        sendDataLayerEvent(eventMap)
    }

    fun onImpressionShareBottomSheet(customDimensionShopPage: CustomDimensionShopPage, userId: String) {
        val eventMap: MutableMap<String, Any> = mutableMapOf(
                ShopPageTrackingConstant.EVENT to ShopPageTrackingConstant.VIEW_SHOP_PAGE_IRIS,
                ShopPageTrackingConstant.EVENT_ACTION to ShopPageTrackingConstant.VIEW_SHARE_BOTTOM_SHEET,
                ShopPageTrackingConstant.EVENT_CATEGORY to ShopPageTrackingConstant.SHOP_PAGE,
                ShopPageTrackingConstant.EVENT_LABEL to "",
                ShopPageTrackingConstant.BUSINESS_UNIT to ShopPageTrackingConstant.SHARING_EXPERIENCE,
                ShopPageTrackingConstant.CURRENT_SITE to ShopPageTrackingConstant.TOKOPEDIA_MARKETPLACE,
                ShopPageTrackingConstant.SHOP_ID to customDimensionShopPage.shopId.orEmpty(),
                ShopPageTrackingConstant.USER_ID to userId.ifEmpty { "0" }
        )
        sendDataLayerEvent(eventMap)
    }

    fun onImpressionScreenshotShareBottomSheet(customDimensionShopPage: CustomDimensionShopPage, userId: String) {
        val eventMap: MutableMap<String, Any> = mutableMapOf(
                ShopPageTrackingConstant.EVENT to ShopPageTrackingConstant.VIEW_SHOP_PAGE_IRIS,
                ShopPageTrackingConstant.EVENT_ACTION to ShopPageTrackingConstant.VIEW_SCREENSHOT_SHARE_BOTTOM_SHEET,
                ShopPageTrackingConstant.EVENT_CATEGORY to ShopPageTrackingConstant.SHOP_PAGE,
                ShopPageTrackingConstant.EVENT_LABEL to "",
                ShopPageTrackingConstant.BUSINESS_UNIT to ShopPageTrackingConstant.SHARING_EXPERIENCE,
                ShopPageTrackingConstant.CURRENT_SITE to ShopPageTrackingConstant.TOKOPEDIA_MARKETPLACE,
                ShopPageTrackingConstant.SHOP_ID to customDimensionShopPage.shopId.orEmpty(),
                ShopPageTrackingConstant.USER_ID to userId.ifEmpty { "0" }
        )
        sendDataLayerEvent(eventMap)
    }

    fun clickCloseNewScreenshotShareBottomSheet(customDimensionShopPage: CustomDimensionShopPage, userId: String) {
        val eventMap: MutableMap<String, Any> = mutableMapOf(
                ShopPageTrackingConstant.EVENT to ShopPageTrackingConstant.CLICK_SHOP_PAGE,
                ShopPageTrackingConstant.EVENT_ACTION to ShopPageTrackingConstant.CLICK_CLOSE_SCREENSHOT_SHARE_BOTTOM_SHEET,
                ShopPageTrackingConstant.EVENT_CATEGORY to ShopPageTrackingConstant.SHOP_PAGE,
                ShopPageTrackingConstant.EVENT_LABEL to "",
                ShopPageTrackingConstant.BUSINESS_UNIT to ShopPageTrackingConstant.SHARING_EXPERIENCE,
                ShopPageTrackingConstant.CURRENT_SITE to ShopPageTrackingConstant.TOKOPEDIA_MARKETPLACE,
                ShopPageTrackingConstant.SHOP_ID to customDimensionShopPage.shopId.orEmpty(),
                ShopPageTrackingConstant.USER_ID to userId.ifEmpty { "0" }
        )
        sendDataLayerEvent(eventMap)
    }

    fun clickScreenshotShareBottomSheetOption(socialMediaName: String, customDimensionShopPage: CustomDimensionShopPage, userId: String) {
        val eventMap: MutableMap<String, Any> = mutableMapOf(
                ShopPageTrackingConstant.EVENT to ShopPageTrackingConstant.CLICK_SHOP_PAGE,
                ShopPageTrackingConstant.EVENT_ACTION to ShopPageTrackingConstant.CLICK_SCREENSHOT_SHARE_BOTTOM_SHEET_OPTION,
                ShopPageTrackingConstant.EVENT_CATEGORY to ShopPageTrackingConstant.SHOP_PAGE,
                ShopPageTrackingConstant.EVENT_LABEL to socialMediaName,
                ShopPageTrackingConstant.BUSINESS_UNIT to ShopPageTrackingConstant.SHARING_EXPERIENCE,
                ShopPageTrackingConstant.CURRENT_SITE to ShopPageTrackingConstant.TOKOPEDIA_MARKETPLACE,
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
                ShopPageTrackingConstant.CURRENT_SITE to ShopPageTrackingConstant.TOKOPEDIA_MARKETPLACE,
                ShopPageTrackingConstant.PAGE_SOURCE to pageSource,
                ShopPageTrackingConstant.RELATED_KEYWORD to relatedKeyword
        )
        sendDataLayerEvent(eventMap)
    }

    fun clickUniversalSharingPermission(action: String, label: String, shopId: String, userId: String) {
        val eventMap: MutableMap<String, Any> = mutableMapOf(
                ShopPageTrackingConstant.EVENT to ShopPageTrackingConstant.CLICK_SHOP_PAGE,
                ShopPageTrackingConstant.EVENT_ACTION to action,
                ShopPageTrackingConstant.EVENT_CATEGORY to ShopPageTrackingConstant.SHOP_PAGE,
                ShopPageTrackingConstant.EVENT_LABEL to label,
                ShopPageTrackingConstant.BUSINESS_UNIT to ShopPageTrackingConstant.SHARING_EXPERIENCE,
                ShopPageTrackingConstant.CURRENT_SITE to ShopPageTrackingConstant.TOKOPEDIA_MARKETPLACE,
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
                ShopPageTrackingConstant.CURRENT_SITE to ShopPageTrackingConstant.TOKOPEDIA_MARKETPLACE,
                ShopPageTrackingConstant.SHOP_ID to shopId,
                ShopPageTrackingConstant.USER_ID to userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }
}