package com.tokopedia.tokopedianow.repurchase.analytic

import android.os.Bundle
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_ADD_TO_CART
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_TOKONOW
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_SELECT_CONTENT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_TOKONOW_IRIS
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_BUSINESS_UNIT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CATEGORY_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CURRENT_SITE
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
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_USER_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalytics.getDataLayer
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalytics.getTracker
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalytics.hitCommonTracker
import com.tokopedia.tokopedianow.repurchase.analytic.RepurchaseAnalytics.ACTION.EVENT_ACTION_CLICK_ADD_TO_CART
import com.tokopedia.tokopedianow.repurchase.analytic.RepurchaseAnalytics.ACTION.EVENT_ACTION_CLICK_APPLY_CATEGORY_FILTER
import com.tokopedia.tokopedianow.repurchase.analytic.RepurchaseAnalytics.ACTION.EVENT_ACTION_CLICK_APPLY_DATE_FILTER
import com.tokopedia.tokopedianow.repurchase.analytic.RepurchaseAnalytics.ACTION.EVENT_ACTION_CLICK_APPLY_MOST_PURCHASE_FILTER
import com.tokopedia.tokopedianow.repurchase.analytic.RepurchaseAnalytics.ACTION.EVENT_ACTION_CLICK_CART_NAV
import com.tokopedia.tokopedianow.repurchase.analytic.RepurchaseAnalytics.ACTION.EVENT_ACTION_CLICK_CATEGORY_FILTER
import com.tokopedia.tokopedianow.repurchase.analytic.RepurchaseAnalytics.ACTION.EVENT_ACTION_CLICK_CHANGE_ADDRESS
import com.tokopedia.tokopedianow.repurchase.analytic.RepurchaseAnalytics.ACTION.EVENT_ACTION_CLICK_DATE_FILTER
import com.tokopedia.tokopedianow.repurchase.analytic.RepurchaseAnalytics.ACTION.EVENT_ACTION_CLICK_MOST_PURCHASE_FILTER
import com.tokopedia.tokopedianow.repurchase.analytic.RepurchaseAnalytics.ACTION.EVENT_ACTION_CLICK_PRODUCT
import com.tokopedia.tokopedianow.repurchase.analytic.RepurchaseAnalytics.ACTION.EVENT_ACTION_CLICK_SIMILAR_PRODUCT
import com.tokopedia.tokopedianow.repurchase.analytic.RepurchaseAnalytics.ACTION.EVENT_ACTION_CLICK_SUBMIT_SEARCH
import com.tokopedia.tokopedianow.repurchase.analytic.RepurchaseAnalytics.ACTION.EVENT_ACTION_IMPRESSION_NO_RESULT_REPURCHASE
import com.tokopedia.tokopedianow.repurchase.analytic.RepurchaseAnalytics.CATEGORY.EVENT_CATEGORY_REPURCHASE_PAGE_TOKONOW
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseProductUiModel
import com.tokopedia.track.TrackAppUtils.*

class RepurchaseAnalytics {

    object CATEGORY{
        const val EVENT_CATEGORY_REPURCHASE_PAGE_TOKONOW = "repurchase page tokonow"
    }

    object ACTION {
        const val EVENT_ACTION_CLICK_ADD_TO_CART = "click add to cart"
        const val EVENT_ACTION_CLICK_PRODUCT = "click product"
        const val EVENT_ACTION_CLICK_SIMILAR_PRODUCT = "click produk serupa"
        const val EVENT_ACTION_IMPRESSION_NO_RESULT_REPURCHASE = "view no result repurchase"
        const val EVENT_ACTION_CLICK_MOST_PURCHASE_FILTER = "click most purchase filter"
        const val EVENT_ACTION_CLICK_APPLY_MOST_PURCHASE_FILTER = "click terapkan on most purchase filter"
        const val EVENT_ACTION_CLICK_CATEGORY_FILTER = "click category filter"
        const val EVENT_ACTION_CLICK_APPLY_CATEGORY_FILTER = "click terapkan on category filter"
        const val EVENT_ACTION_CLICK_DATE_FILTER = "click date filter"
        const val EVENT_ACTION_CLICK_APPLY_DATE_FILTER = "click terapkan on date filter"
        const val EVENT_ACTION_CLICK_SUBMIT_SEARCH = "submit search from cari product"
        const val EVENT_ACTION_CLICK_CART_NAV = "click cart nav"
        const val EVENT_ACTION_CLICK_CHANGE_ADDRESS = "click change address"
    }

    object VALUE {
        const val REPURCHASE_TOKONOW = "repurchase page tokonow"
        const val SHOP_NAME = "Tokopedia NOW!"
        const val SHOP_TYPE = "tokonow"
    }

    fun onClickChangeAddress(userId: String) {
        val dataLayer = getDataLayer(
            event = EVENT_CLICK_TOKONOW,
            action = EVENT_ACTION_CLICK_CHANGE_ADDRESS,
            category = EVENT_CATEGORY_REPURCHASE_PAGE_TOKONOW
        )
        dataLayer[KEY_USER_ID] = userId
        hitCommonTracker(
            dataLayer
        )
    }

    fun onClickCartNav(userId: String) {
        val dataLayer = getDataLayer(
            event = EVENT_CLICK_TOKONOW,
            action = EVENT_ACTION_CLICK_CART_NAV,
            category = EVENT_CATEGORY_REPURCHASE_PAGE_TOKONOW
        )
        dataLayer[KEY_USER_ID] = userId
        hitCommonTracker(
            dataLayer
        )
    }

    fun onClickSimilarProduct(userId: String) {
        val dataLayer = getDataLayer(
            event = EVENT_CLICK_TOKONOW,
            action = EVENT_ACTION_CLICK_SIMILAR_PRODUCT,
            category = EVENT_CATEGORY_REPURCHASE_PAGE_TOKONOW
        )
        dataLayer[KEY_USER_ID] = userId
        hitCommonTracker(
            dataLayer
        )
    }

    fun onImpressNoHistoryRepurchase(userId: String) {
        val dataLayer = getDataLayer(
            event = EVENT_VIEW_TOKONOW_IRIS,
            action = EVENT_ACTION_IMPRESSION_NO_RESULT_REPURCHASE,
            category = EVENT_CATEGORY_REPURCHASE_PAGE_TOKONOW
        )
        dataLayer[KEY_USER_ID] = userId
        hitCommonTracker(
            dataLayer
        )
    }

    fun onClickMostPurchaseFilter(userId: String) {
        val dataLayer = getDataLayer(
            event = EVENT_CLICK_TOKONOW,
            action = EVENT_ACTION_CLICK_MOST_PURCHASE_FILTER,
            category = EVENT_CATEGORY_REPURCHASE_PAGE_TOKONOW
        )
        dataLayer[KEY_USER_ID] = userId
        hitCommonTracker(
            dataLayer
        )
    }

    fun onClickApplyMostPurchaseFilter(userId: String) {
        val dataLayer = getDataLayer(
            event = EVENT_CLICK_TOKONOW,
            action = EVENT_ACTION_CLICK_APPLY_MOST_PURCHASE_FILTER,
            category = EVENT_CATEGORY_REPURCHASE_PAGE_TOKONOW
        )
        dataLayer[KEY_USER_ID] = userId
        hitCommonTracker(
            dataLayer
        )
    }

    fun onClickCategoryFilter(userId: String) {
        val dataLayer = getDataLayer(
            event = EVENT_CLICK_TOKONOW,
            action = EVENT_ACTION_CLICK_CATEGORY_FILTER,
            category = EVENT_CATEGORY_REPURCHASE_PAGE_TOKONOW
        )
        dataLayer[KEY_USER_ID] = userId
        hitCommonTracker(
            dataLayer
        )
    }

    fun onClickApplyCategoryFilter(userId: String) {
        val dataLayer = getDataLayer(
            event = EVENT_CLICK_TOKONOW,
            action = EVENT_ACTION_CLICK_APPLY_CATEGORY_FILTER,
            category = EVENT_CATEGORY_REPURCHASE_PAGE_TOKONOW
        )
        dataLayer[KEY_USER_ID] = userId
        hitCommonTracker(
            dataLayer
        )
    }

    fun onClickDateFilter(userId: String) {
        val dataLayer = getDataLayer(
            event = EVENT_CLICK_TOKONOW,
            action = EVENT_ACTION_CLICK_DATE_FILTER,
            category = EVENT_CATEGORY_REPURCHASE_PAGE_TOKONOW
        )
        dataLayer[KEY_USER_ID] = userId
        hitCommonTracker(
            dataLayer
        )
    }

    fun onClickApplyDateFilter(userId: String) {
        val dataLayer = getDataLayer(
            event = EVENT_CLICK_TOKONOW,
            action = EVENT_ACTION_CLICK_APPLY_DATE_FILTER,
            category = EVENT_CATEGORY_REPURCHASE_PAGE_TOKONOW
        )
        dataLayer[KEY_USER_ID] = userId
        hitCommonTracker(
            dataLayer
        )
    }

    // the feature is not ready yet
    fun onClickSubmitSearch(userId: String) {
        val dataLayer = getDataLayer(
            event = EVENT_CLICK_TOKONOW,
            action = EVENT_ACTION_CLICK_SUBMIT_SEARCH,
            category = EVENT_CATEGORY_REPURCHASE_PAGE_TOKONOW
        )
        dataLayer[KEY_USER_ID] = userId
        hitCommonTracker(
            dataLayer
        )
    }

    fun onClickAddToCart(userId: String, quantity: Int, model: RepurchaseProductUiModel) {
        val item = ecommerceDataLayerAddToCartClicked(model, quantity)

        val dataLayer = getEcommerceDataLayer(
            event = EVENT_ADD_TO_CART,
            action = EVENT_ACTION_CLICK_ADD_TO_CART,
            category = EVENT_CATEGORY_REPURCHASE_PAGE_TOKONOW,
            userId = userId,
            items = arrayListOf(item)
        )
        getTracker().sendEnhanceEcommerceEvent(EVENT_ADD_TO_CART, dataLayer)
    }

    fun onClickProduct(userId: String, model: RepurchaseProductUiModel, position: Int) {
        val item = ecommerceDataLayerProductClicked(model, position)

        val dataLayer = getEcommerceDataLayer(
            event = EVENT_SELECT_CONTENT,
            action = EVENT_ACTION_CLICK_PRODUCT,
            category = EVENT_CATEGORY_REPURCHASE_PAGE_TOKONOW,
            userId = userId,
            items = arrayListOf(item)
        )
        dataLayer.putString(KEY_ITEM_LIST, "")
        getTracker().sendEnhanceEcommerceEvent(EVENT_SELECT_CONTENT, dataLayer)
    }

    private fun getEcommerceDataLayer(event: String, action: String, category: String, userId: String, items: ArrayList<Bundle>, label: String = ""): Bundle {
        return Bundle().apply {
            putString(EVENT, event)
            putString(EVENT_ACTION, action)
            putString(EVENT_CATEGORY, category)
            putString(EVENT_LABEL, label)
            putString(KEY_BUSINESS_UNIT, BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE)
            putString(KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            putString(KEY_USER_ID, userId)
            putParcelableArrayList(KEY_ITEMS, items)
        }
    }

    private fun ecommerceDataLayerAddToCartClicked(model: RepurchaseProductUiModel, quantity: Int): Bundle {
        return Bundle().apply {
            putString(KEY_CATEGORY_ID, model.categoryId)
            putString(KEY_ITEM_BRAND, "")
            putString(KEY_ITEM_CATEGORY, model.category)
            putString(KEY_ITEM_ID, model.id)
            putString(KEY_ITEM_NAME, model.productCard.productName)
            putString(KEY_ITEM_VARIANT, model.productCard.labelGroupVariantList.joinToString { it.title })
            putString(KEY_PRICE, model.productCard.formattedPrice)
            putString(KEY_QUANTITY, quantity.toString())
            putString(KEY_SHOP_ID, model.shopId)
            putString(KEY_SHOP_NAME, VALUE.SHOP_NAME)
            putString(KEY_SHOP_TYPE, VALUE.SHOP_TYPE)
        }
    }

    private fun ecommerceDataLayerProductClicked(model: RepurchaseProductUiModel, position: Int): Bundle {
        return Bundle().apply {
            putString(KEY_INDEX, position.toString())
            putString(KEY_ITEM_BRAND, "")
            putString(KEY_ITEM_CATEGORY, model.category)
            putString(KEY_ITEM_ID, model.id)
            putString(KEY_ITEM_NAME, model.productCard.productName)
            putString(KEY_ITEM_VARIANT, model.productCard.labelGroupVariantList.joinToString { it.title })
            putString(KEY_PRICE, model.productCard.formattedPrice)
        }
    }
}