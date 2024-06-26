package com.tokopedia.tokopedianow.recipedetail.analytics

import android.os.Bundle
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_ADD_TO_CART
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_PG
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_SELECT_CONTENT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_ITEM_LIST
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_PG_IRIS
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CATEGORY_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_INDEX
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEMS
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_BRAND
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_CATEGORY
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_LIST
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_NAME
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_VARIANT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_PRICE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_QUANTITY
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_SHOP_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_SHOP_NAME
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_SHOP_TYPE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_TRACKER_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.DEFAULT_EMPTY_VALUE
import com.tokopedia.tokopedianow.recipecommon.analytics.RecipeCommonAnalyticsConstant.EVENT_CATEGORY_TOKONOW_RECIPE
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeProductAnalytics.ACTION.EVENT_ACTION_CLICK_ADD_TO_CART
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeProductAnalytics.ACTION.EVENT_ACTION_CLICK_PRODUCT
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeProductAnalytics.ACTION.EVENT_ACTION_CLICK_QTY_DECREMENT
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeProductAnalytics.ACTION.EVENT_ACTION_CLICK_QTY_INCREMENT
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeProductAnalytics.ACTION.EVENT_ACTION_CLICK_REMOVE_PRODUCT
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeProductAnalytics.ACTION.EVENT_ACTION_CLICK_SIMILAR_PRODUCT_BTN
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeProductAnalytics.ACTION.EVENT_ACTION_IMPRESSION_OUT_OF_STOCK_PRODUCT
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeProductAnalytics.ACTION.EVENT_ACTION_IMPRESSION_PRODUCT_CARD
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeProductAnalytics.ACTION.EVENT_ACTION_IMPRESSION_SIMILAR_PRODUCT_BTN
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeProductAnalytics.TRACKER_ID.TRACKER_ID_CLICK_ADD_TO_CART
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeProductAnalytics.TRACKER_ID.TRACKER_ID_CLICK_PRODUCT
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeProductAnalytics.TRACKER_ID.TRACKER_ID_CLICK_QTY_DECREMENT
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeProductAnalytics.TRACKER_ID.TRACKER_ID_CLICK_QTY_INCREMENT
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeProductAnalytics.TRACKER_ID.TRACKER_ID_CLICK_REMOVE_PRODUCT
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeProductAnalytics.TRACKER_ID.TRACKER_ID_CLICK_SIMILAR_PRODUCT_BTN
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeProductAnalytics.TRACKER_ID.TRACKER_ID_IMPRESSION_OUT_OF_STOCK_PRODUCT
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeProductAnalytics.TRACKER_ID.TRACKER_ID_IMPRESSION_PRODUCT_CARD
import com.tokopedia.tokopedianow.recipedetail.analytics.RecipeProductAnalytics.TRACKER_ID.TRACKER_ID_IMPRESSION_SIMILAR_PRODUCT_BTN
import com.tokopedia.tokopedianow.recipedetail.presentation.uimodel.RecipeProductUiModel
import com.tokopedia.track.TrackApp
import com.tokopedia.track.constant.TrackerConstant.BUSINESS_UNIT
import com.tokopedia.track.constant.TrackerConstant.CURRENT_SITE
import com.tokopedia.track.constant.TrackerConstant.EVENT
import com.tokopedia.track.constant.TrackerConstant.EVENT_ACTION
import com.tokopedia.track.constant.TrackerConstant.EVENT_CATEGORY
import com.tokopedia.track.constant.TrackerConstant.EVENT_LABEL
import com.tokopedia.track.constant.TrackerConstant.USERID
import com.tokopedia.user.session.UserSessionInterface

/**
 * Docs:
 * https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/3155
 */
class RecipeProductAnalytics(
    private val userSession: UserSessionInterface,
    private val headerName: String = ""
) : ProductAnalytics {

    private object ACTION {
        const val EVENT_ACTION_CLICK_PRODUCT = "click product view pdp view"
        const val EVENT_ACTION_CLICK_SIMILAR_PRODUCT_BTN = "click produk serupa button"
        const val EVENT_ACTION_IMPRESSION_SIMILAR_PRODUCT_BTN = "impression produk serupa button"
        const val EVENT_ACTION_CLICK_ADD_TO_CART = "click atc button"
        const val EVENT_ACTION_CLICK_REMOVE_PRODUCT = "click trash button"
        const val EVENT_ACTION_CLICK_QTY_DECREMENT = "click quantity decrement"
        const val EVENT_ACTION_CLICK_QTY_INCREMENT = "click quantity increment"
        const val EVENT_ACTION_IMPRESSION_OUT_OF_STOCK_PRODUCT = "impression oos product card"
        const val EVENT_ACTION_IMPRESSION_PRODUCT_CARD = "impression product card"
    }

    private object TRACKER_ID {
        const val TRACKER_ID_CLICK_PRODUCT = "33041"
        const val TRACKER_ID_CLICK_SIMILAR_PRODUCT_BTN = "33043"
        const val TRACKER_ID_IMPRESSION_SIMILAR_PRODUCT_BTN = "33042"
        const val TRACKER_ID_CLICK_ADD_TO_CART = "33052"
        const val TRACKER_ID_CLICK_REMOVE_PRODUCT = "33054"
        const val TRACKER_ID_CLICK_QTY_DECREMENT = "33055"
        const val TRACKER_ID_CLICK_QTY_INCREMENT = "33056"
        const val TRACKER_ID_IMPRESSION_OUT_OF_STOCK_PRODUCT = "33057"
        const val TRACKER_ID_IMPRESSION_PRODUCT_CARD = "33040"
    }

    override fun trackImpressionProduct(product: RecipeProductUiModel) {
        val items = listOf(
            createProductItemDataLayer(
                index = product.position,
                id = product.id,
                name = product.name,
                price = product.priceFmt,
                category = product.categoryId
            )
        )

        val dataLayer = createGeneralDataLayer(
            event = EVENT_VIEW_ITEM_LIST,
            action = EVENT_ACTION_IMPRESSION_PRODUCT_CARD,
            trackerId = TRACKER_ID_IMPRESSION_PRODUCT_CARD
        ).apply {
            putString(KEY_ITEM_LIST, "")
            putParcelableArrayList(KEY_ITEMS, ArrayList(items))
        }

        sendEnhanceEcommerceEvent(
            eventName = EVENT_VIEW_ITEM_LIST,
            dataLayer = dataLayer
        )
    }

    override fun trackClickProduct(product: RecipeProductUiModel) {
        val items = listOf(
            createProductItemDataLayer(
                index = product.position,
                id = product.id,
                name = product.name,
                price = product.priceFmt,
                category = product.categoryId
            )
        )

        val itemList = product.similarProducts.map {
            createProductItemDataLayer(
                index = it.position,
                id = it.id,
                name = it.name,
                price = it.priceFmt,
                category = it.categoryId
            )
        }

        val dataLayer = createGeneralDataLayer(
            event = EVENT_SELECT_CONTENT,
            action = EVENT_ACTION_CLICK_PRODUCT,
            trackerId = TRACKER_ID_CLICK_PRODUCT
        ).apply {
            putParcelableArrayList(KEY_ITEM_LIST, ArrayList(itemList))
            putParcelableArrayList(KEY_ITEMS, ArrayList(items))
        }

        sendEnhanceEcommerceEvent(
            eventName = EVENT_SELECT_CONTENT,
            dataLayer = dataLayer
        )
    }

    override fun trackClickAddToCart(product: RecipeProductUiModel) {
        val items = listOf(
            createAtcProductItemDataLayer(
                id = product.id,
                name = product.name,
                price = product.getPrice(),
                categoryName = product.categoryName,
                categoryId = product.categoryId,
                quantity = product.minOrder.toString(),
                shopId = product.shopId
            )
        )

        val dataLayer = createGeneralDataLayer(
            event = EVENT_ADD_TO_CART,
            action = EVENT_ACTION_CLICK_ADD_TO_CART,
            trackerId = TRACKER_ID_CLICK_ADD_TO_CART
        ).apply {
            putParcelableArrayList(KEY_ITEMS, ArrayList(items))
        }

        sendEnhanceEcommerceEvent(
            eventName = EVENT_ADD_TO_CART,
            dataLayer = dataLayer
        )
    }

    override fun trackClickRemoveProduct() {
        val dataLayer = createGeneralDataLayer(
            event = EVENT_CLICK_PG,
            action = EVENT_ACTION_CLICK_REMOVE_PRODUCT,
            trackerId = TRACKER_ID_CLICK_REMOVE_PRODUCT
        )

        sendEnhanceEcommerceEvent(
            eventName = EVENT_CLICK_PG,
            dataLayer = dataLayer
        )
    }

    override fun trackClickDecreaseQuantity() {
        val dataLayer = createGeneralDataLayer(
            event = EVENT_CLICK_PG,
            action = EVENT_ACTION_CLICK_QTY_DECREMENT,
            trackerId = TRACKER_ID_CLICK_QTY_DECREMENT
        )

        sendEnhanceEcommerceEvent(
            eventName = EVENT_CLICK_PG,
            dataLayer = dataLayer
        )
    }

    override fun trackClickIncreaseQuantity() {
        val dataLayer = createGeneralDataLayer(
            event = EVENT_CLICK_PG,
            action = EVENT_ACTION_CLICK_QTY_INCREMENT,
            trackerId = TRACKER_ID_CLICK_QTY_INCREMENT
        )

        sendEnhanceEcommerceEvent(
            eventName = EVENT_CLICK_PG,
            dataLayer = dataLayer
        )
    }

    override fun trackImpressionSimilarProductBtn() {
        val dataLayer = createGeneralDataLayer(
            event = EVENT_VIEW_PG_IRIS,
            action = EVENT_ACTION_IMPRESSION_SIMILAR_PRODUCT_BTN,
            trackerId = TRACKER_ID_IMPRESSION_SIMILAR_PRODUCT_BTN
        )

        sendEnhanceEcommerceEvent(
            eventName = EVENT_VIEW_PG_IRIS,
            dataLayer = dataLayer
        )
    }

    override fun trackClickSimilarProductBtn() {
        val dataLayer = createGeneralDataLayer(
            event = EVENT_CLICK_PG,
            action = EVENT_ACTION_CLICK_SIMILAR_PRODUCT_BTN,
            trackerId = TRACKER_ID_CLICK_SIMILAR_PRODUCT_BTN
        )

        sendEnhanceEcommerceEvent(
            eventName = EVENT_CLICK_PG,
            dataLayer = dataLayer
        )
    }

    override fun trackImpressionOutOfStockProduct(product: RecipeProductUiModel) {
        val items = listOf(
            createProductItemDataLayer(
                index = product.position,
                id = product.id,
                name = product.name,
                price = product.priceFmt,
                category = product.categoryId
            )
        )

        val itemList = product.similarProducts.map {
            createProductItemDataLayer(
                index = it.position,
                id = it.id,
                name = it.name,
                price = it.priceFmt,
                category = it.categoryId
            )
        }

        val dataLayer = createGeneralDataLayer(
            event = EVENT_VIEW_ITEM_LIST,
            action = EVENT_ACTION_IMPRESSION_OUT_OF_STOCK_PRODUCT,
            trackerId = TRACKER_ID_IMPRESSION_OUT_OF_STOCK_PRODUCT
        ).apply {
            putParcelableArrayList(KEY_ITEMS, ArrayList(items))
            putParcelableArrayList(KEY_ITEM_LIST, ArrayList(itemList))
        }

        sendEnhanceEcommerceEvent(
            eventName = EVENT_VIEW_ITEM_LIST,
            dataLayer = dataLayer
        )
    }

    private fun createGeneralDataLayer(event: String, action: String, trackerId: String): Bundle {
        return Bundle().apply {
            putString(EVENT, event)
            putString(EVENT_ACTION, action)
            putString(EVENT_CATEGORY, EVENT_CATEGORY_TOKONOW_RECIPE)
            putString(EVENT_LABEL, headerName)
            putString(KEY_TRACKER_ID, trackerId)
            putString(BUSINESS_UNIT, BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE)
            putString(CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            putString(USERID, userSession.userId)
        }
    }

    private fun createProductItemDataLayer(
        index: Int = 0,
        id: String = "",
        name: String = "",
        price: String = "",
        brand: String = "",
        category: String = "",
        variant: String = ""
    ): Bundle {
        return Bundle().apply {
            putInt(KEY_INDEX, index)
            putString(KEY_ITEM_BRAND, brand)
            putString(KEY_ITEM_CATEGORY, category)
            putString(KEY_ITEM_ID, id)
            putString(KEY_ITEM_NAME, name)
            putString(KEY_ITEM_VARIANT, variant)
            putString(KEY_PRICE, price)
        }
    }
    private fun createAtcProductItemDataLayer(
        id: String = "",
        name: String = "",
        price: String = "",
        brand: String = "",
        categoryName: String = "",
        categoryId: String = "",
        quantity: String = "",
        variant: String = "",
        shopId: String = ""
    ): Bundle {
        return Bundle().apply {
            putString(KEY_ITEM_BRAND, brand)
            putString(KEY_ITEM_CATEGORY, categoryName)
            putString(KEY_ITEM_ID, id)
            putString(KEY_ITEM_NAME, name)
            putString(KEY_ITEM_VARIANT, variant)
            putString(KEY_PRICE, price)
            putString(KEY_CATEGORY_ID, categoryId)
            putString(KEY_QUANTITY, quantity)
            putString(KEY_SHOP_ID, shopId)
            putString(KEY_SHOP_NAME, DEFAULT_EMPTY_VALUE)
            putString(KEY_SHOP_TYPE, DEFAULT_EMPTY_VALUE)
        }
    }

    private fun sendEnhanceEcommerceEvent(eventName: String, dataLayer: Bundle) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(eventName, dataLayer)
    }
}
