package com.tokopedia.tokopedianow.recipedetail.analytics

import android.os.Bundle
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_ADD_TO_CART
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_ATC
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_PG
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_PRODUCT_CLICK
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_PRODUCT_VIEW
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_SELECT_CONTENT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_ITEM_LIST
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_PG_IRIS
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_INDEX
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEMS
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_BRAND
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_CATEGORY
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_LIST
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_NAME
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_VARIANT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_PRICE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.DEFAULT_EMPTY_VALUE
import com.tokopedia.tokopedianow.recipecommon.analytics.RecipeCommonAnalyticsConstant.EVENT_CATEGORY_TOKONOW_RECIPE
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

class RecipeSimilarProductAnalytics(
    private val userSession: UserSessionInterface
) : ProductAnalytics {

    companion object {
        private const val EVENT_ACTION_IMPRESSION_BOTTOMSHEET = "impression produk serupa bottomsheet"
        private const val EVENT_ACTION_IMPRESSION_PRODUCT_BOTTOMSHEET = "impression produk serupa bottomsheet"
        private const val EVENT_ACTION_IMPRESSION_SIMILAR_PRODUCT_BTN = "impression produk serupa button"
        private const val EVENT_ACTION_CLICK_SIMILAR_PRODUCT_BTN = "click produk serupa button"
        private const val EVENT_ACTION_CLICK_ADD_TO_CART = "bottomsheet atc product"
        private const val EVENT_ACTION_CLICK_PRODUCT = "product click at bottomsheet"
        private const val EVENT_ACTION_CLICK_REMOVE_PRODUCT = "click bottomsheet trash"
        private const val EVENT_ACTION_CLICK_QTY_DECREMENT = "click bottomsheet qty decrement"
        private const val EVENT_ACTION_CLICK_QTY_INCREMENT = "click bottomsheet qty increment"
        private const val EVENT_ACTION_IMPRESSION_OUT_OF_STOCK_PRODUCT = "impression oos product card"
    }

    fun trackImpressionBottomSheet() {
        val dataLayer = createGeneralDataLayer(
            event = EVENT_VIEW_PG_IRIS,
            action = EVENT_ACTION_IMPRESSION_BOTTOMSHEET
        )

        sendEnhanceEcommerceEvent(
            eventName = EVENT_VIEW_PG_IRIS,
            dataLayer = dataLayer
        )
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
            action = EVENT_ACTION_IMPRESSION_PRODUCT_BOTTOMSHEET
        ).apply {
            putParcelableArrayList(KEY_ITEM_LIST, arrayListOf())
            putParcelableArrayList(KEY_ITEMS, ArrayList(items))
        }

        sendEnhanceEcommerceEvent(
            eventName = EVENT_PRODUCT_VIEW,
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
                index = product.position,
                id = product.id,
                name = product.name,
                price = product.priceFmt,
                category = product.categoryId
            )
        }

        val dataLayer = createGeneralDataLayer(
            event = EVENT_SELECT_CONTENT,
            action = EVENT_ACTION_CLICK_PRODUCT
        ).apply {
            putParcelableArrayList(KEY_ITEM_LIST, ArrayList(itemList))
            putParcelableArrayList(KEY_ITEMS, ArrayList(items))
        }

        sendEnhanceEcommerceEvent(
            eventName = EVENT_PRODUCT_CLICK,
            dataLayer = dataLayer
        )
    }

    override fun trackClickAddToCart(product: RecipeProductUiModel) {
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
            event = EVENT_ADD_TO_CART,
            action = EVENT_ACTION_CLICK_ADD_TO_CART
        ).apply {
            putParcelableArrayList(KEY_ITEMS, ArrayList(items))
        }

        sendEnhanceEcommerceEvent(
            eventName = EVENT_ATC,
            dataLayer = dataLayer
        )
    }

    override fun trackClickRemoveProduct() {
        val dataLayer = createGeneralDataLayer(
            event = EVENT_CLICK_PG,
            action = EVENT_ACTION_CLICK_REMOVE_PRODUCT
        )

        sendEnhanceEcommerceEvent(
            eventName = EVENT_CLICK_PG,
            dataLayer = dataLayer
        )
    }

    override fun trackClickDecreaseQuantity() {
        val dataLayer = createGeneralDataLayer(
            event = EVENT_CLICK_PG,
            action = EVENT_ACTION_CLICK_QTY_DECREMENT
        )

        sendEnhanceEcommerceEvent(
            eventName = EVENT_CLICK_PG,
            dataLayer = dataLayer
        )
    }

    override fun trackClickIncreaseQuantity() {
        val dataLayer = createGeneralDataLayer(
            event = EVENT_CLICK_PG,
            action = EVENT_ACTION_CLICK_QTY_INCREMENT
        )

        sendEnhanceEcommerceEvent(
            eventName = EVENT_CLICK_PG,
            dataLayer = dataLayer
        )
    }

    override fun trackImpressionSimilarProductBtn() {
        val dataLayer = createGeneralDataLayer(
            event = EVENT_VIEW_PG_IRIS,
            action = EVENT_ACTION_IMPRESSION_SIMILAR_PRODUCT_BTN
        )

        sendEnhanceEcommerceEvent(
            eventName = EVENT_VIEW_PG_IRIS,
            dataLayer = dataLayer
        )
    }

    override fun trackClickSimilarProductBtn() {
        val dataLayer = createGeneralDataLayer(
            event = EVENT_CLICK_PG,
            action = EVENT_ACTION_CLICK_SIMILAR_PRODUCT_BTN
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

        val dataLayer = createGeneralDataLayer(
            event = EVENT_VIEW_ITEM_LIST,
            action = EVENT_ACTION_IMPRESSION_OUT_OF_STOCK_PRODUCT
        ).apply {
            putParcelableArrayList(KEY_ITEMS, ArrayList(items))
        }

        sendEnhanceEcommerceEvent(
            eventName = EVENT_PRODUCT_VIEW,
            dataLayer = dataLayer
        )
    }

    private fun createGeneralDataLayer(event: String, action: String): Bundle {
        return Bundle().apply {
            putString(EVENT, event)
            putString(EVENT_ACTION, action)
            putString(EVENT_CATEGORY, EVENT_CATEGORY_TOKONOW_RECIPE)
            putString(EVENT_LABEL, DEFAULT_EMPTY_VALUE)
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

    private fun sendEnhanceEcommerceEvent(eventName: String, dataLayer: Bundle) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(eventName, dataLayer)
    }
}