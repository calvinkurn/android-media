package com.tokopedia.tokofood.feature.merchant.analytics

import android.os.Bundle
import com.tokopedia.atc_common.domain.analytics.AddToCartExternalAnalytics
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalytics
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants
import com.tokopedia.tokofood.common.constants.ShareComponentConstants
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodProduct
import com.tokopedia.tokofood.feature.merchant.presentation.model.ProductListItem
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * https://mynakama.tokopedia.com/datatracker/requestdetail/view/2988
 */

class MerchantPageAnalyticsOld @Inject constructor(private val userSession: UserSessionInterface) {

    private val tracker by lazy { TrackApp.getInstance().gtm }

    fun openMerchantPage(merchantId: String, isShopClosed: Boolean) {
        val shopStatus = if (isShopClosed) {
            TokoFoodAnalyticsConstants.CLOSED
        } else {
            TokoFoodAnalyticsConstants.OPEN
        }
        val mapData = mapOf(
            TrackAppUtils.EVENT to TokoFoodAnalyticsConstants.OPEN_SCREEN,
            TrackAppUtils.EVENT_CATEGORY to TokoFoodAnalyticsConstants.TOKOFOOD_MERCHANT_PAGE,
            TrackAppUtils.EVENT_ACTION to TokoFoodAnalyticsConstants.VIEW_MERCHANT_PAGE,
            TrackAppUtils.EVENT_LABEL to shopStatus,
            TokoFoodAnalyticsConstants.BUSSINESS_UNIT to TokoFoodAnalyticsConstants.PHYSICAL_GOODS,
            TokoFoodAnalyticsConstants.CURRENT_SITE to TokoFoodAnalyticsConstants.TOKOPEDIA_MARKETPLACE,
            TokoFoodAnalyticsConstants.IS_LOGGED_IN_STATUS to userSession.isLoggedIn.toString(),
            TokoFoodAnalyticsConstants.MERCHANT to TokoFoodAnalyticsConstants.TOKOFOOD_MERCHANT_PAGE,
            TokoFoodAnalyticsConstants.SHOP_ID to merchantId,
            TokoFoodAnalyticsConstants.USER_ID to userSession.userId.orEmpty()
        )
        tracker.sendScreenAuthenticated(TokoFoodAnalyticsConstants.TOKOFOOD_MERCHANT_PAGE, mapData)
    }

    fun impressionOnProductCard(
        productListItems: List<ProductListItem>,
        productListItem: ProductListItem,
        position: Int,
        merchantId: String
    ) {
        val foodItem = productListItem.productUiModel
        val oosText = if (foodItem.isOutOfStock) {
            TokoFoodAnalyticsConstants.OOS
        } else {
            TokoFoodAnalyticsConstants.NOT
        }

        val productListItemsFmt =
            productListItems.joinToString(prefix = ",") { it.productUiModel.name }

        val itemBundle = getItemsBundle(productListItem, position)

        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT, TokoFoodAnalyticsConstants.VIEW_ITEM_LIST)
            putString(
                TrackAppUtils.EVENT_ACTION,
                TokoFoodAnalyticsConstants.IMPRESSION_ON_PRODUCT_CARD
            )
            putString(
                TrackAppUtils.EVENT_CATEGORY,
                TokoFoodAnalyticsConstants.TOKOFOOD_MERCHANT_PAGE
            )
            putString(
                TrackAppUtils.EVENT_LABEL,
                "$oosText - ${foodItem.name} - ${foodItem.price.toInt()} - $position"
            )
            putString(
                TokoFoodAnalyticsConstants.BUSSINESS_UNIT,
                TokoFoodAnalyticsConstants.PHYSICAL_GOODS
            )
            putString(
                TokoFoodAnalyticsConstants.CURRENT_SITE,
                TokoFoodAnalyticsConstants.TOKOPEDIA_MARKETPLACE
            )
            putString(
                TokoFoodAnalyticsConstants.ITEM_LIST, productListItemsFmt
            )
            putParcelableArrayList(
                AddToCartExternalAnalytics.EE_VALUE_ITEMS, arrayListOf(itemBundle)
            )
            putString(TokoFoodAnalyticsConstants.SHOP_ID, merchantId)
            putString(TokoFoodAnalyticsConstants.USER_ID, userSession.userId)
        }

        tracker.sendEnhanceEcommerceEvent(TokoFoodAnalyticsConstants.VIEW_ITEM_LIST, eventDataLayer)
    }

    fun clickProductCard(
        productListItems: List<ProductListItem>,
        productListItem: ProductListItem,
        position: Int,
        merchantId: String
    ) {
        val foodItem = productListItem.productUiModel
        val oosText = if (foodItem.isOutOfStock)
            TokoFoodAnalyticsConstants.OOS else TokoFoodAnalyticsConstants.NOT

        val itemBundle = getItemsBundle(productListItem, position)

        val productListItemsFmt =
            productListItems.joinToString(prefix = ",") { it.productUiModel.name }

        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT, TokoFoodAnalyticsConstants.SELECT_CONTENT)
            putString(
                TrackAppUtils.EVENT_ACTION,
                TokoFoodAnalyticsConstants.CLICK_ON_PRODUCT_CARD
            )
            putString(
                TrackAppUtils.EVENT_CATEGORY,
                TokoFoodAnalyticsConstants.TOKOFOOD_MERCHANT_PAGE
            )
            putString(
                TrackAppUtils.EVENT_LABEL,
                "$oosText - ${foodItem.name} - ${foodItem.price.toInt()} - $position"
            )
            putString(
                TokoFoodAnalyticsConstants.BUSSINESS_UNIT,
                TokoFoodAnalyticsConstants.PHYSICAL_GOODS
            )
            putString(
                TokoFoodAnalyticsConstants.CURRENT_SITE,
                TokoFoodAnalyticsConstants.TOKOPEDIA_MARKETPLACE
            )
            putString(
                TokoFoodAnalyticsConstants.ITEM_LIST, productListItemsFmt
            )
            putParcelableArrayList(
                AddToCartExternalAnalytics.EE_VALUE_ITEMS, arrayListOf(itemBundle)
            )
            putString(TokoFoodAnalyticsConstants.SHOP_ID, merchantId)
            putString(TokoFoodAnalyticsConstants.USER_ID, userSession.userId)
        }

        tracker.sendEnhanceEcommerceEvent(TokoFoodAnalyticsConstants.SELECT_CONTENT, eventDataLayer)
    }

    fun clickOnAtcButton(
        productListItem: ProductListItem,
        merchantId: String,
        merchantName: String,
        position: Int
    ) {
        val foodItem = productListItem.productUiModel
        val oosText = if (foodItem.isOutOfStock)
            TokoFoodAnalyticsConstants.OOS else TokoFoodAnalyticsConstants.NOT

        val itemBundle = getCartItemsBundle(
            productListItem, merchantId, merchantName,
            foodItem.orderQty.toString()
        )

        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT, TokoFoodAnalyticsConstants.ADD_TO_CART)
            putString(
                TrackAppUtils.EVENT_ACTION,
                TokoFoodAnalyticsConstants.CLICK_ON_PESAN_PRODUCT_CARD
            )
            putString(
                TrackAppUtils.EVENT_CATEGORY,
                TokoFoodAnalyticsConstants.TOKOFOOD_MERCHANT_PAGE
            )
            putString(
                TrackAppUtils.EVENT_LABEL,
                "$oosText - ${foodItem.name} - ${foodItem.price.toInt()} - $position"
            )
            putString(
                TokoFoodAnalyticsConstants.BUSSINESS_UNIT,
                TokoFoodAnalyticsConstants.PHYSICAL_GOODS
            )
            putString(
                TokoFoodAnalyticsConstants.CURRENT_SITE,
                TokoFoodAnalyticsConstants.TOKOPEDIA_MARKETPLACE
            )
            putParcelableArrayList(
                TokoFoodAnalyticsConstants.ITEM_LIST, arrayListOf()
            )
            putParcelableArrayList(
                AddToCartExternalAnalytics.EE_VALUE_ITEMS, arrayListOf(itemBundle)
            )
            putString(TokoFoodAnalyticsConstants.SHOP_ID, merchantId)
            putString(TokoFoodAnalyticsConstants.USER_ID, userSession.userId)
        }

        tracker.sendEnhanceEcommerceEvent(TokoFoodAnalyticsConstants.ADD_TO_CART, eventDataLayer)
    }

    fun clickOnOrderProductBottomSheet(
        productListItem: ProductListItem,
        merchantId: String,
        merchantName: String,
        position: Int
    ) {
        val foodItem = productListItem.productUiModel

        val itemBundle = getCartItemsBundle(
            productListItem,
            merchantId,
            merchantName,
            foodItem.orderQty.toString()
        )

        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT, TokoFoodAnalyticsConstants.ADD_TO_CART)
            putString(
                TrackAppUtils.EVENT_ACTION,
                TokoFoodAnalyticsConstants.CLICK_ON_PESAN_PRODUCT_BOTTOM_SHEET
            )
            putString(
                TrackAppUtils.EVENT_CATEGORY,
                TokoFoodAnalyticsConstants.TOKOFOOD_MERCHANT_PAGE
            )
            putString(
                TrackAppUtils.EVENT_LABEL,
                "$merchantId - ${foodItem.name} - ${foodItem.price.toInt()} - $position"
            )
            putString(
                TokoFoodAnalyticsConstants.BUSSINESS_UNIT,
                TokoFoodAnalyticsConstants.PHYSICAL_GOODS
            )
            putString(
                TokoFoodAnalyticsConstants.CURRENT_SITE,
                TokoFoodAnalyticsConstants.TOKOPEDIA_MARKETPLACE
            )
            putParcelableArrayList(
                TokoFoodAnalyticsConstants.ITEM_LIST, arrayListOf()
            )
            putParcelableArrayList(
                AddToCartExternalAnalytics.EE_VALUE_ITEMS, arrayListOf(itemBundle)
            )
            putString(TokoFoodAnalyticsConstants.PRODUCT_ID, foodItem.id)
            putString(TokoFoodAnalyticsConstants.SHOP_ID, merchantId)
            putString(TokoFoodAnalyticsConstants.USER_ID, userSession.userId)
        }

        tracker.sendEnhanceEcommerceEvent(TokoFoodAnalyticsConstants.ADD_TO_CART, eventDataLayer)
    }

    fun clickOnOrderVariantPage(
        productListItem: ProductListItem?,
        merchantId: String,
        merchantName: String,
        position: Int
    ) {
        val foodItem = productListItem?.productUiModel

        val itemBundle = productListItem?.let {
            getCartItemsBundle(
                it,
                merchantId,
                merchantName,
                foodItem?.orderQty.toString()
            )
        }

        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT, TokoFoodAnalyticsConstants.ADD_TO_CART)
            putString(
                TrackAppUtils.EVENT_ACTION,
                TokoFoodAnalyticsConstants.CLICK_ON_PESAN_VARIANT_PAGE
            )
            putString(
                TrackAppUtils.EVENT_CATEGORY,
                TokoFoodAnalyticsConstants.TOKOFOOD_MERCHANT_PAGE
            )
            putString(
                TrackAppUtils.EVENT_LABEL,
                "$merchantId - ${foodItem?.name.orEmpty()} - ${foodItem?.price?.toInt().orZero()} - $position"
            )
            putString(
                TokoFoodAnalyticsConstants.BUSSINESS_UNIT,
                TokoFoodAnalyticsConstants.PHYSICAL_GOODS
            )
            putString(
                TokoFoodAnalyticsConstants.CURRENT_SITE,
                TokoFoodAnalyticsConstants.TOKOPEDIA_MARKETPLACE
            )
            putParcelableArrayList(
                TokoFoodAnalyticsConstants.ITEM_LIST, arrayListOf()
            )
            putParcelableArrayList(
                AddToCartExternalAnalytics.EE_VALUE_ITEMS, arrayListOf(itemBundle)
            )
            putString(TokoFoodAnalyticsConstants.PRODUCT_ID, foodItem?.id.orEmpty())
            putString(TokoFoodAnalyticsConstants.SHOP_ID, merchantId)
            putString(TokoFoodAnalyticsConstants.USER_ID, userSession.userId)
        }

        tracker.sendEnhanceEcommerceEvent(TokoFoodAnalyticsConstants.ADD_TO_CART, eventDataLayer)
    }

    fun clickCheckoutOnMiniCart(productList: List<CheckoutTokoFoodProduct>,
                                purchaseAmount: String,
                                merchantId: String,
                                merchantName: String) {
        val productBundleList =
            ArrayList(
                productList.map { getMiniCartItemsBundle(it, merchantId, merchantName) }
            )

        val cartIds = productList.joinToString(separator = COMMA_SEPARATOR) { it.cartId }
        val productIds = productList.joinToString(separator = COMMA_SEPARATOR) { it.productId }
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT, TokoFoodAnalytics.EVENT_BEGIN_CHECKOUT)
            putString(TrackAppUtils.EVENT_ACTION, TokoFoodAnalyticsConstants.CLICK_ON_CHECKOUT_MINICART)
            putString(TrackAppUtils.EVENT_CATEGORY, TokoFoodAnalyticsConstants.TOKOFOOD_MERCHANT_PAGE)
            putString(TrackAppUtils.EVENT_LABEL, "{$purchaseAmount} - {$cartIds}")
            putString(TokoFoodAnalytics.KEY_TRACKER_ID, MINICART_TRACKER_ID)
            putString(TokoFoodAnalyticsConstants.BUSSINESS_UNIT, TokoFoodAnalyticsConstants.PHYSICAL_GOODS)
            putString(TokoFoodAnalyticsConstants.CURRENT_SITE, TokoFoodAnalyticsConstants.TOKOPEDIA_MARKETPLACE)
            putString(TokoFoodAnalyticsConstants.PRODUCT_ID, productIds)
            putString(TokoFoodAnalyticsConstants.SHOP_ID, merchantId)
            putString(TokoFoodAnalyticsConstants.USER_ID, userSession.userId)
            putString(
                TokoFoodAnalytics.KEY_CHECKOUT_OPTION,
                TokoFoodAnalyticsConstants.CLICK_ON_CHECKOUT_BUTTON_ON_MINICART
            )
            putString(TokoFoodAnalytics.KEY_CHECKOUT_STEP, TokoFoodAnalytics.CHECKOUT_STEP_1)
            putParcelableArrayList(AddToCartExternalAnalytics.EE_VALUE_ITEMS, productBundleList)
        }

        tracker.sendEnhanceEcommerceEvent(TokoFoodAnalytics.EVENT_BEGIN_CHECKOUT, eventDataLayer)
    }

    fun impressShareBottomSheet(itemId: String, userId: String) {
        val mapData = mapOf(
            TrackAppUtils.EVENT to TokoFoodAnalyticsConstants.VIEW_COMMUNICATION_IRIS,
            TrackAppUtils.EVENT_ACTION to TokoFoodAnalyticsConstants.VIEW_ON_SHARING_CHANNEL,
            TrackAppUtils.EVENT_CATEGORY to ShareComponentConstants.TOKOFOOD,
            TrackAppUtils.EVENT_LABEL to "${TokoFoodAnalyticsConstants.MERCHANT} \n $itemId",
            TokoFoodAnalyticsConstants.BUSSINESS_UNIT to TokoFoodAnalyticsConstants.PHYSICAL_GOODS,
            TokoFoodAnalyticsConstants.CURRENT_SITE to TokoFoodAnalyticsConstants.TOKOPEDIA_MARKETPLACE,
            TokoFoodAnalyticsConstants.USER_ID to String.format(
                TokoFoodAnalyticsConstants.USER_ID_FORMAT,
                userId
            )
        )
        tracker.sendGeneralEvent(mapData)
    }

    fun closeShareBottomSheet(itemId: String, userId: String) {
        val mapData = mapOf(
            TrackAppUtils.EVENT to TokoFoodAnalyticsConstants.CLICK_COMMUNICATION,
            TrackAppUtils.EVENT_ACTION to TokoFoodAnalyticsConstants.CLICK_CLOSE_SHARE_BOTTOM_SHEET,
            TrackAppUtils.EVENT_CATEGORY to ShareComponentConstants.TOKOFOOD,
            TrackAppUtils.EVENT_LABEL to "${TokoFoodAnalyticsConstants.MERCHANT} \n $itemId",
            TokoFoodAnalyticsConstants.BUSSINESS_UNIT to TokoFoodAnalyticsConstants.PHYSICAL_GOODS,
            TokoFoodAnalyticsConstants.CURRENT_SITE to TokoFoodAnalyticsConstants.TOKOPEDIA_MARKETPLACE,
            TokoFoodAnalyticsConstants.USER_ID to String.format(
                TokoFoodAnalyticsConstants.USER_ID_FORMAT,
                userId
            )
        )
        tracker.sendGeneralEvent(mapData)
    }

    fun clickShareBottomSheet(itemId: String, userId: String) {
        val mapData = mapOf(
            TrackAppUtils.EVENT to TokoFoodAnalyticsConstants.CLICK_COMMUNICATION,
            TrackAppUtils.EVENT_ACTION to TokoFoodAnalyticsConstants.CLICK_SHARE_BOTTOM_SHEET,
            TrackAppUtils.EVENT_CATEGORY to ShareComponentConstants.TOKOFOOD,
            TrackAppUtils.EVENT_LABEL to "${TokoFoodAnalyticsConstants.MERCHANT} \n $itemId",
            TokoFoodAnalyticsConstants.BUSSINESS_UNIT to TokoFoodAnalyticsConstants.PHYSICAL_GOODS,
            TokoFoodAnalyticsConstants.CURRENT_SITE to TokoFoodAnalyticsConstants.TOKOPEDIA_MARKETPLACE,
            TokoFoodAnalyticsConstants.USER_ID to String.format(
                TokoFoodAnalyticsConstants.USER_ID_FORMAT,
                userId
            )
        )
        tracker.sendGeneralEvent(mapData)
    }

    fun impressPromoMvc(promoName: String, merchantId: String) {
        val promotionBundleList = arrayListOf(getMvcPromotionBundle(promoName))

        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT, TokoFoodAnalytics.EVENT_PROMO_VIEW)
            putString(TrackAppUtils.EVENT_ACTION, TokoFoodAnalyticsConstants.IMPRESSION_ON_PROMO_BUTTON)
            putString(TrackAppUtils.EVENT_CATEGORY, TokoFoodAnalyticsConstants.TOKOFOOD_MERCHANT_PAGE)
            putString(TrackAppUtils.EVENT_LABEL, String.EMPTY)
            putString(TokoFoodAnalytics.KEY_TRACKER_ID, TokoFoodAnalyticsConstants.TRACKER_ID_37402)
            putString(TokoFoodAnalyticsConstants.BUSSINESS_UNIT, TokoFoodAnalyticsConstants.PHYSICAL_GOODS)
            putString(TokoFoodAnalyticsConstants.CURRENT_SITE, TokoFoodAnalyticsConstants.TOKOPEDIA_MARKETPLACE)
            putString(TokoFoodAnalyticsConstants.SHOP_ID, merchantId)
            putString(TokoFoodAnalyticsConstants.PROMO_ID, String.EMPTY)
            putString(TokoFoodAnalyticsConstants.USER_ID, userSession.userId)
            putParcelableArrayList(TokoFoodAnalyticsConstants.KEY_EE_PROMOTIONS, promotionBundleList)
        }

        tracker.sendEnhanceEcommerceEvent(TokoFoodAnalytics.EVENT_PROMO_VIEW, eventDataLayer)
    }

    fun clickPromoMvc(promoName: String, merchantId: String) {
        val promotionBundleList = arrayListOf(getMvcPromotionBundle(promoName))

        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT, TokoFoodAnalytics.EVENT_PROMO_CLICK)
            putString(TrackAppUtils.EVENT_ACTION, TokoFoodAnalyticsConstants.CLICK_ON_PROMO_BUTTON)
            putString(TrackAppUtils.EVENT_CATEGORY, TokoFoodAnalyticsConstants.TOKOFOOD_MERCHANT_PAGE)
            putString(TrackAppUtils.EVENT_LABEL, String.EMPTY)
            putString(TokoFoodAnalytics.KEY_TRACKER_ID, TokoFoodAnalyticsConstants.TRACKER_ID_37403)
            putString(TokoFoodAnalyticsConstants.BUSSINESS_UNIT, TokoFoodAnalyticsConstants.PHYSICAL_GOODS)
            putString(TokoFoodAnalyticsConstants.CURRENT_SITE, TokoFoodAnalyticsConstants.TOKOPEDIA_MARKETPLACE)
            putString(TokoFoodAnalyticsConstants.SHOP_ID, merchantId)
            putString(TokoFoodAnalyticsConstants.PROMO_ID, String.EMPTY)
            putString(TokoFoodAnalyticsConstants.USER_ID, userSession.userId)
            putParcelableArrayList(TokoFoodAnalyticsConstants.KEY_EE_PROMOTIONS, promotionBundleList)
        }

        tracker.sendEnhanceEcommerceEvent(TokoFoodAnalytics.EVENT_PROMO_CLICK, eventDataLayer)
    }

    private fun getItemsBundle(
        productListItem: ProductListItem,
        position: Int
    ): Bundle {
        val foodItem = productListItem.productUiModel
        val categoryItem = productListItem.productCategory
        return Bundle().apply {
            putString(
                TokoFoodAnalyticsConstants.INDEX,
                position.toString()
            )
            putString(
                AddToCartExternalAnalytics.EE_PARAM_ITEM_BRAND,
                String.EMPTY
            )
            putString(
                AddToCartExternalAnalytics.EE_PARAM_ITEM_CATEGORY,
                categoryItem.title
            )
            putString(
                AddToCartExternalAnalytics.EE_PARAM_ITEM_ID,
                foodItem.id
            )
            putString(
                AddToCartExternalAnalytics.EE_PARAM_ITEM_NAME,
                foodItem.name
            )
            putString(
                AddToCartExternalAnalytics.EE_PARAM_ITEM_VARIANT,
                foodItem.customListItems.joinToString(separator = COMMA_SEPARATOR) { it.addOnUiModel?.name.orEmpty() }
            )
            putInt(AddToCartExternalAnalytics.EE_PARAM_PRICE, foodItem.price.toInt())
        }
    }

    private fun getCartItemsBundle(
        productListItem: ProductListItem,
        merchantId: String,
        merchantName: String,
        quantity: String
    ): Bundle {
        val foodItem = productListItem.productUiModel
        val categoryItem = productListItem.productCategory
        return Bundle().apply {
            putString(
                AddToCartExternalAnalytics.EE_PARAM_CATEGORY_ID,
                categoryItem.id
            )
            putString(
                AddToCartExternalAnalytics.EE_PARAM_ITEM_BRAND,
                String.EMPTY
            )
            putString(
                AddToCartExternalAnalytics.EE_PARAM_ITEM_CATEGORY,
                categoryItem.title
            )
            putString(
                AddToCartExternalAnalytics.EE_PARAM_ITEM_ID,
                foodItem.id
            )
            putString(
                AddToCartExternalAnalytics.EE_PARAM_ITEM_NAME,
                foodItem.name
            )
            putString(
                AddToCartExternalAnalytics.EE_PARAM_ITEM_VARIANT,
                foodItem.customListItems.joinToString(separator = COMMA_SEPARATOR) { it.addOnUiModel?.name.orEmpty() }
            )
            putString(AddToCartExternalAnalytics.EE_PARAM_PRICE, foodItem.price.toString())
            putString(AddToCartExternalAnalytics.EE_PARAM_QUANTITY, quantity)
            putString(AddToCartExternalAnalytics.EE_PARAM_SHOP_ID, merchantId)
            putString(AddToCartExternalAnalytics.EE_PARAM_SHOP_NAME, merchantName)
            putString(
                AddToCartExternalAnalytics.EE_PARAM_SHOP_TYPE, ""
            )
        }
    }

    private fun getMiniCartItemsBundle(
        product: CheckoutTokoFoodProduct,
        merchantId: String,
        merchantName: String
    ): Bundle {
        val selectedOptions =
            product.variants.flatMap { it.options }.filter { it.isSelected }.map { it.optionId }
        return Bundle().apply {
            putString(TokoFoodAnalytics.KEY_DIMENSION_45, product.cartId)
            putString(AddToCartExternalAnalytics.EE_PARAM_ITEM_BRAND, String.EMPTY)
            putString(AddToCartExternalAnalytics.EE_PARAM_ITEM_CATEGORY, product.categoryId)
            putString(AddToCartExternalAnalytics.EE_PARAM_ITEM_ID, product.productId)
            putString(AddToCartExternalAnalytics.EE_PARAM_ITEM_NAME, product.productName)
            putString(
                AddToCartExternalAnalytics.EE_PARAM_ITEM_VARIANT,
                selectedOptions.joinToString(separator = COMMA_SEPARATOR)
            )
            putString(AddToCartExternalAnalytics.EE_PARAM_PRICE, product.price.toString())
            putString(AddToCartExternalAnalytics.EE_PARAM_QUANTITY, product.quantity.toString())
            putString(AddToCartExternalAnalytics.EE_PARAM_SHOP_ID, merchantId)
            putString(AddToCartExternalAnalytics.EE_PARAM_SHOP_NAME, merchantName)
            putString(AddToCartExternalAnalytics.EE_PARAM_SHOP_TYPE, String.EMPTY)
        }
    }

    private fun getMvcPromotionBundle(promoName: String): Bundle {
        return Bundle().apply {
            putString(
                TokoFoodAnalyticsConstants.KEY_EE_CREATIVE_NAME,
                PROMO_TYPE_TICKER
            )
            putInt(TokoFoodAnalyticsConstants.KEY_EE_CREATIVE_SLOT, Int.ZERO)
            putString(TokoFoodAnalyticsConstants.KEY_EE_ITEM_ID, String.EMPTY)
            putString(TokoFoodAnalyticsConstants.KEY_EE_ITEM_NAME, promoName)
        }
    }

    companion object {
        private const val MINICART_TRACKER_ID = "31320"

        private const val COMMA_SEPARATOR = ","

        private const val PROMO_TYPE_TICKER = "ticker"
    }

}
