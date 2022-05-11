package com.tokopedia.shop_widget.mvc_locked_to_product.analytic

import android.os.Bundle
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTrackingConstant.Key.KEY_BUSINESS_UNIT
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTrackingConstant.Key.KEY_CATEGORY_ID
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTrackingConstant.Key.KEY_CURRENT_SITE
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTrackingConstant.Key.KEY_DIMENSION_40
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTrackingConstant.Key.KEY_DIMENSION_45
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTrackingConstant.Key.KEY_DIMENSION_87
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTrackingConstant.Key.KEY_EVENT
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTrackingConstant.Key.KEY_EVENT_ACTION
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTrackingConstant.Key.KEY_EVENT_CATEGORY
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTrackingConstant.Key.KEY_EVENT_LABEL
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTrackingConstant.Key.KEY_INDEX
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTrackingConstant.Key.KEY_IS_LOGGED_IN_STATUS
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTrackingConstant.Key.KEY_ITEMS
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTrackingConstant.Key.KEY_ITEM_BRAND
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTrackingConstant.Key.KEY_ITEM_CATEGORY
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTrackingConstant.Key.KEY_ITEM_ID
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTrackingConstant.Key.KEY_ITEM_LIST
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTrackingConstant.Key.KEY_ITEM_NAME
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTrackingConstant.Key.KEY_ITEM_VARIANT
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTrackingConstant.Key.KEY_PAGE_SOURCE
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTrackingConstant.Key.KEY_PAGE_TYPE
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTrackingConstant.Key.KEY_PRICE
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTrackingConstant.Key.KEY_PRODUCT_ID
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTrackingConstant.Key.KEY_QUANTITY
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTrackingConstant.Key.KEY_SCREEN_NAME
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTrackingConstant.Key.KEY_SHOP_ID
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTrackingConstant.Key.KEY_SHOP_UNDERSCORE_ID
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTrackingConstant.Key.KEY_USER_ID
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTrackingConstant.Value.ADD_TO_CART
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTrackingConstant.Value.ATC_REMOVE_PRODUCT
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTrackingConstant.Value.ATC_UPDATE_ADD_PRODUCT_QUANTITY
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTrackingConstant.Value.ATC_UPDATE_REMOVE_PRODUCT_QUANTITY
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTrackingConstant.Value.CLICK_ATC_ON_MVC_PRODUCT_CARD
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTrackingConstant.Value.CLICK_MVC_PRODUCT_CARD
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTrackingConstant.Value.CLICK_MVC_SORT_OPTION
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTrackingConstant.Value.CLICK_PG
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTrackingConstant.Value.CLICK_PRODUCT_QUANTITY
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTrackingConstant.Value.CLICK_TRASH_BUTTON
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTrackingConstant.Value.EVENT_OPEN_SCREEN
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTrackingConstant.Value.LOGIN
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTrackingConstant.Value.MVC_LOCKED_TO_PRODUCT_LIST_GENERAL_MODULE
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTrackingConstant.Value.MVC_LOCKED_TO_PRODUCT_LIST_NAME
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTrackingConstant.Value.MVC_LOCKED_TO_PRODUCT_PAGE_SOURCE
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTrackingConstant.Value.MVC_LOCKED_TO_PRODUCT_PAGE_TYPE
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTrackingConstant.Value.MVC_LOCKED_TO_PRODUCT_SCREEN_NAME
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTrackingConstant.Value.MVC_LOCKED_TO_PRODUCT_VBS_IMPRESSION_ACTION
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTrackingConstant.Value.MVC_LOCKED_TO_PRODUCT_VIEW_PG_IRIS
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTrackingConstant.Value.MVC_PRODUCT_ITEM_LIST
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTrackingConstant.Value.NON_LOGIN
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTrackingConstant.Value.PHYSICAL_GOODS
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTrackingConstant.Value.SELECT_CONTENT
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTrackingConstant.Value.SHOP_PAGE_BUYER
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTrackingConstant.Value.SHOP_PAGE_SELLER
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.MvcLockedToProductTrackingConstant.Value.TOKOPEDIA_MARKETPLACE
import com.tokopedia.shop_widget.mvc_locked_to_product.analytic.model.MvcLockedToProductAddToCartTracker
import com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel.MvcLockedToProductGridProductUiModel
import com.tokopedia.track.TrackApp
import javax.inject.Inject

/*
Data Layer Docs:
https://mynakama.tokopedia.com/datatracker/requestdetail/2549
 */

class MvcLockedToProductTracking @Inject constructor() {

    fun sendOpenScreenMvcLockedToProduct(
        voucherId: String,
        shopId: String,
        userId: String,
        previousPage: String,
        isLogin: Boolean
    ) {
        val pageSource = String.format(MVC_LOCKED_TO_PRODUCT_PAGE_SOURCE, voucherId, previousPage)
        val screenName = String.format(MVC_LOCKED_TO_PRODUCT_SCREEN_NAME, shopId)
        val loggedInStatus = getLoggedInStatus(isLogin)
        val eventMap = mapOf(
            KEY_EVENT to EVENT_OPEN_SCREEN,
            KEY_BUSINESS_UNIT to PHYSICAL_GOODS,
            KEY_CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            KEY_IS_LOGGED_IN_STATUS to loggedInStatus,
            KEY_PAGE_SOURCE to pageSource,
            KEY_PAGE_TYPE to MVC_LOCKED_TO_PRODUCT_PAGE_TYPE,
            KEY_SCREEN_NAME to screenName,
            KEY_SHOP_ID to shopId,
            KEY_USER_ID to userId
        )
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName, eventMap)
    }

    fun clickProductCard(
        position: Int,
        productId: String,
        productName: String,
        productPrice: String,
        voucherId: String,
        shopId: String,
        userId: String,
        voucherName: String,
        isSeller: Boolean
    ) {
        val eventLabel = "$productId - $voucherId - $voucherName"
        val itemList = String.format(MVC_PRODUCT_ITEM_LIST, voucherName)
        val eventBundle = Bundle().apply {
            putString(KEY_EVENT, SELECT_CONTENT)
            putString(KEY_EVENT_ACTION, CLICK_MVC_PRODUCT_CARD)
            putString(KEY_EVENT_CATEGORY, getShopPageEventCategory(isSeller))
            putString(KEY_EVENT_LABEL, eventLabel)
            putString(KEY_BUSINESS_UNIT, PHYSICAL_GOODS)
            putString(KEY_CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
            putString(KEY_ITEM_LIST, itemList)
            putString(KEY_PRODUCT_ID, productId)
            putString(KEY_SHOP_ID, shopId)
            putString(KEY_USER_ID, userId)
            putParcelableArrayList(
                KEY_ITEMS,
                arrayListOf(
                    getProductItemTracking(
                        position,
                        productId,
                        productName,
                        productPrice,
                        voucherId
                    )
                )
            )
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(SELECT_CONTENT, eventBundle)
    }

    fun clickSortOption(
        selectedSortName: String,
        shopId: String,
        userId: String,
        isSeller: Boolean
    ) {
        val eventMap = mapOf(
            KEY_EVENT to CLICK_PG,
            KEY_EVENT_ACTION to CLICK_MVC_SORT_OPTION,
            KEY_EVENT_CATEGORY to getShopPageEventCategory(isSeller),
            KEY_EVENT_LABEL to selectedSortName,
            KEY_BUSINESS_UNIT to PHYSICAL_GOODS,
            KEY_CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            KEY_SHOP_ID to shopId,
            KEY_USER_ID to userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    private fun getShopPageEventCategory(isSeller: Boolean): String {
        return if (isSeller) {
            SHOP_PAGE_SELLER
        } else {
            SHOP_PAGE_BUYER
        }
    }

    private fun getLoggedInStatus(isLogin: Boolean): String {
        return if (isLogin)
            LOGIN
        else
            NON_LOGIN
    }

    private fun getProductItemTracking(
        position: Int,
        productId: String,
        productName: String,
        productPrice: String,
        voucherId: String
    ): Bundle {
        return Bundle().apply {
            putString(KEY_DIMENSION_87, MVC_LOCKED_TO_PRODUCT_LIST_GENERAL_MODULE)
            putInt(KEY_INDEX, position)
            putString(KEY_ITEM_BRAND, "")
            putString(KEY_ITEM_CATEGORY, "")
            putString(KEY_ITEM_ID, productId)
            putString(KEY_ITEM_NAME, productName)
            putString(KEY_ITEM_VARIANT, voucherId)
            putLong(KEY_PRICE, productPrice.toLongOrZero())
        }
    }

    fun sendClickAddToCartTracker(
        cartId: String,
        productUiModel: MvcLockedToProductGridProductUiModel,
        voucherId: String,
        quantity: Int,
        shopId: String,
        userId: String,
        isSellerView: Boolean
    ) {
        val eventBundle = Bundle().apply {
            putString(KEY_EVENT, ADD_TO_CART)
            putString(KEY_EVENT_ACTION, CLICK_ATC_ON_MVC_PRODUCT_CARD)
            putString(KEY_EVENT_CATEGORY, getShopPageEventCategory(isSellerView))
            putString(KEY_EVENT_LABEL, "")
            putString(KEY_BUSINESS_UNIT, PHYSICAL_GOODS)
            putString(KEY_CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
            putString(KEY_PRODUCT_ID, productUiModel.productID)
            putString(KEY_SHOP_ID, shopId)
            putString(KEY_USER_ID, userId)
            putParcelableArrayList(
                KEY_ITEMS,
                arrayListOf(
                    getAtcProductItemTracking(
                        cartId,
                        shopId,
                        quantity,
                        productUiModel.productID,
                        productUiModel.productCardModel.productName,
                        productUiModel.finalPrice,
                        voucherId
                    )
                )
            )
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(ADD_TO_CART, eventBundle)
    }

    private fun getAtcProductItemTracking(
        cartId: String,
        shopId: String,
        quantity: Int,
        productId: String,
        productName: String,
        productPrice: String,
        voucherId: String
    ): Bundle {
        return Bundle().apply {
            putString(KEY_CATEGORY_ID, "")
            putString(KEY_DIMENSION_45, cartId)
            putString(KEY_DIMENSION_40, MVC_LOCKED_TO_PRODUCT_LIST_NAME)
            putString(KEY_DIMENSION_87, MVC_LOCKED_TO_PRODUCT_LIST_GENERAL_MODULE)
            putString(KEY_ITEM_BRAND, "")
            putString(KEY_ITEM_CATEGORY, "")
            putString(KEY_ITEM_ID, productId)
            putString(KEY_ITEM_NAME, productName)
            putString(KEY_ITEM_VARIANT, voucherId)
            putLong(KEY_PRICE, productPrice.toLongOrZero())
            putInt(KEY_QUANTITY, quantity)
            putString(KEY_SHOP_UNDERSCORE_ID, shopId)
        }
    }

    fun sendClickUpdateProductQuantityTracker(
        productId: String,
        quantity: Int,
        atcType: MvcLockedToProductAddToCartTracker.AtcType,
        shopId: String,
        userId: String,
        isSellerView: Boolean
    ) {
        val eventLabel = when(atcType){
            MvcLockedToProductAddToCartTracker.AtcType.UPDATE_ADD -> {
                String.format(ATC_UPDATE_ADD_PRODUCT_QUANTITY,quantity)
            }
            else -> {
                String.format(ATC_UPDATE_REMOVE_PRODUCT_QUANTITY,quantity)
            }
        }
        val eventMap = mapOf(
            KEY_EVENT to CLICK_PG,
            KEY_EVENT_ACTION to CLICK_PRODUCT_QUANTITY,
            KEY_EVENT_CATEGORY to getShopPageEventCategory(isSellerView),
            KEY_EVENT_LABEL to eventLabel,
            KEY_BUSINESS_UNIT to PHYSICAL_GOODS,
            KEY_CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            KEY_PRODUCT_ID to productId,
            KEY_SHOP_ID to shopId,
            KEY_USER_ID to userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }


    fun sendClickAtcTrashButton(
        productId: String,
        quantity: Int,
        atcType: MvcLockedToProductAddToCartTracker.AtcType,
        shopId: String,
        userId: String,
        voucherId: String,
        isSellerView: Boolean
    ) {
        val eventLabel = String.format(ATC_REMOVE_PRODUCT, productId, voucherId)
        val eventMap = mapOf(
            KEY_EVENT to CLICK_PG,
            KEY_EVENT_ACTION to CLICK_TRASH_BUTTON,
            KEY_EVENT_CATEGORY to getShopPageEventCategory(isSellerView),
            KEY_EVENT_LABEL to eventLabel,
            KEY_BUSINESS_UNIT to PHYSICAL_GOODS,
            KEY_CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            KEY_PRODUCT_ID to productId,
            KEY_SHOP_ID to shopId,
            KEY_USER_ID to userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    fun sendVbsImpressionTracker(shopId: String, userId: String, isSellerView: Boolean) {
        val eventMap = mapOf(
            KEY_EVENT to MVC_LOCKED_TO_PRODUCT_VIEW_PG_IRIS,
            KEY_EVENT_ACTION to MVC_LOCKED_TO_PRODUCT_VBS_IMPRESSION_ACTION,
            KEY_EVENT_CATEGORY to getShopPageEventCategory(isSellerView),
            KEY_EVENT_LABEL to "",
            KEY_BUSINESS_UNIT to PHYSICAL_GOODS,
            KEY_CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            KEY_SHOP_ID to shopId,
            KEY_USER_ID to userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

}
