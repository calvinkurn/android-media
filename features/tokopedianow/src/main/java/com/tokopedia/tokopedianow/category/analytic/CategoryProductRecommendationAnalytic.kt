package com.tokopedia.tokopedianow.category.analytic

import android.os.Bundle
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.tokopedianow.category.analytic.CategoryMainAnalytic.ACTION.EVENT_ACTION_CLICK_ATC_ON_PRODUCT_RECOM_WIDGET
import com.tokopedia.tokopedianow.category.analytic.CategoryMainAnalytic.ACTION.EVENT_ACTION_CLICK_PRODUCT_CAROUSEL
import com.tokopedia.tokopedianow.category.analytic.CategoryMainAnalytic.ACTION.EVENT_ACTION_IMPRESS_PRODUCT_CAROUSEL
import com.tokopedia.tokopedianow.category.analytic.CategoryMainAnalytic.CATEGORY.EVENT_CATEGORY_PAGE_L1
import com.tokopedia.tokopedianow.category.analytic.CategoryMainAnalytic.TRACKER_ID.ID_CLICK_ATC_ON_PRODUCT_RECOM_WIDGET
import com.tokopedia.tokopedianow.category.analytic.CategoryMainAnalytic.TRACKER_ID.ID_CLICK_PRODUCT_CAROUSEL
import com.tokopedia.tokopedianow.category.analytic.CategoryMainAnalytic.TRACKER_ID.ID_IMPRESS_PRODUCT_CAROUSEL
import com.tokopedia.tokopedianow.category.analytic.CategoryMainAnalytic.VALUE.ITEM_LIST_CATEGORY_L1
import com.tokopedia.tokopedianow.category.analytic.CategoryMainAnalytic.VALUE.ITEM_LIST_RECOMPRODUCT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_ADD_TO_CART
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_SELECT_CONTENT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_ITEM_LIST
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_BUSINESS_UNIT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CATEGORY_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CURRENT_SITE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_DIMENSION_40
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_DIMENSION_98
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
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_USER_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_GROCERIES
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.ITEM_LIST_SLASH_TOKONOW
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalytics.getTracker
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalytics.joinDash
import com.tokopedia.track.TrackAppUtils.EVENT
import com.tokopedia.track.TrackAppUtils.EVENT_ACTION
import com.tokopedia.track.TrackAppUtils.EVENT_CATEGORY
import com.tokopedia.track.TrackAppUtils.EVENT_LABEL

class CategoryProductRecommendationAnalytic(
    private val userId: String
) {
    private fun getItems(
        isOos: Boolean,
        index: Int,
        id: String,
        name: String,
        price: Long
    ): Bundle {
        val items = Bundle()
        items.putString(KEY_DIMENSION_40, String.EMPTY)
        items.putString(KEY_DIMENSION_98, (!isOos).toString())
        items.putInt(KEY_INDEX, index)
        items.putString(KEY_ITEM_BRAND, String.EMPTY)
        items.putString(KEY_ITEM_CATEGORY, String.EMPTY)
        items.putString(KEY_ITEM_ID, id)
        items.putString(KEY_ITEM_NAME, name)
        items.putString(KEY_ITEM_VARIANT, String.EMPTY)
        items.putDouble(KEY_PRICE, price.toDouble())
        return items
    }

    private fun getAtcItems(
        categoryId: String,
        isOos: Boolean,
        id: String,
        name: String,
        price: Long,
        quantity: String
    ): Bundle {
        val items = Bundle()
        items.putString(KEY_CATEGORY_ID, categoryId)
        items.putString(KEY_DIMENSION_98, (!isOos).toString())
        items.putString(KEY_ITEM_BRAND, String.EMPTY)
        items.putString(KEY_ITEM_CATEGORY, String.EMPTY)
        items.putString(KEY_ITEM_ID, id)
        items.putString(KEY_ITEM_NAME, name)
        items.putString(KEY_ITEM_VARIANT, String.EMPTY)
        items.putDouble(KEY_PRICE, price.toDouble())
        items.putString(KEY_QUANTITY, quantity)
        items.putString(KEY_SHOP_ID, String.EMPTY)
        items.putString(KEY_SHOP_NAME, String.EMPTY)
        items.putString(KEY_SHOP_TYPE, String.EMPTY)
        return items
    }

    // Tracker ID: 43851
    fun sendImpressionProductCarouselEvent (
        categoryIdL1: String,
        headerName: String,
        index: Int,
        productId: String,
        warehouseId: String,
        isOos: Boolean,
        name: String,
        price: Long
    ) {
        val bundle = Bundle().apply {
            putString(EVENT, EVENT_VIEW_ITEM_LIST)
            putString(EVENT_ACTION, EVENT_ACTION_IMPRESS_PRODUCT_CAROUSEL)
            putString(EVENT_CATEGORY, EVENT_CATEGORY_PAGE_L1)
            putString(EVENT_LABEL, joinDash(categoryIdL1, headerName, index.toString(), productId, warehouseId))
            putString(KEY_TRACKER_ID, ID_IMPRESS_PRODUCT_CAROUSEL)
            putString(KEY_BUSINESS_UNIT, BUSINESS_UNIT_GROCERIES)
            putString(KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            putString(KEY_ITEM_LIST, joinDash(ITEM_LIST_SLASH_TOKONOW, ITEM_LIST_CATEGORY_L1, ITEM_LIST_RECOMPRODUCT, headerName))
            putBundle(KEY_ITEMS, getItems(
                    isOos = isOos,
                    index = index,
                    id = productId,
                    name = name,
                    price = price
                )
            )
            putString(KEY_USER_ID, userId)
        }
        getTracker().sendEnhanceEcommerceEvent(EVENT_VIEW_ITEM_LIST, bundle)
    }

    // Tracker ID: 43852
    fun sendClickProductCarouselEvent (
        categoryIdL1: String,
        headerName: String,
        index: Int,
        productId: String,
        warehouseId: String,
        isOos: Boolean,
        name: String,
        price: Long
    ) {
        val bundle = Bundle().apply {
            putString(EVENT, EVENT_SELECT_CONTENT)
            putString(EVENT_ACTION, EVENT_ACTION_CLICK_PRODUCT_CAROUSEL)
            putString(EVENT_CATEGORY, EVENT_CATEGORY_PAGE_L1)
            putString(EVENT_LABEL, joinDash(categoryIdL1, headerName, index.toString(), productId, warehouseId))
            putString(KEY_TRACKER_ID, ID_CLICK_PRODUCT_CAROUSEL)
            putString(KEY_BUSINESS_UNIT, BUSINESS_UNIT_GROCERIES)
            putString(KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            putString(KEY_ITEM_LIST, joinDash(ITEM_LIST_SLASH_TOKONOW, ITEM_LIST_CATEGORY_L1, ITEM_LIST_RECOMPRODUCT, headerName))
            putBundle(KEY_ITEMS, getItems(
                    isOos = isOos,
                    index = index,
                    id = productId,
                    name = name,
                    price = price
                )
            )
            putString(KEY_USER_ID, userId)
        }
        getTracker().sendEnhanceEcommerceEvent(EVENT_SELECT_CONTENT, bundle)
    }

    // Tracker ID: 43862
    fun sendClickAtcCarouselWidgetEvent (
        categoryIdL1: String,
        index: Int,
        productId: String,
        warehouseId: String,
        isOos: Boolean,
        name: String,
        price: Long,
        headerName: String,
        quantity: String
    ) {
        val bundle = Bundle().apply {
            putString(EVENT, EVENT_ADD_TO_CART)
            putString(EVENT_ACTION, EVENT_ACTION_CLICK_ATC_ON_PRODUCT_RECOM_WIDGET)
            putString(EVENT_CATEGORY, EVENT_CATEGORY_PAGE_L1)
            putString(EVENT_LABEL, joinDash(categoryIdL1, index.toString(), productId, warehouseId))
            putString(KEY_TRACKER_ID, ID_CLICK_ATC_ON_PRODUCT_RECOM_WIDGET)
            putString(KEY_BUSINESS_UNIT, BUSINESS_UNIT_GROCERIES)
            putString(KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            putString(KEY_ITEM_LIST, joinDash(ITEM_LIST_SLASH_TOKONOW, ITEM_LIST_CATEGORY_L1, ITEM_LIST_RECOMPRODUCT, headerName))
            putBundle(KEY_ITEMS, getAtcItems(
                    categoryId = categoryIdL1,
                    isOos = isOos,
                    id = productId,
                    name = name,
                    price = price,
                    quantity = quantity
                )
            )
            putString(KEY_USER_ID, userId)
        }
        getTracker().sendEnhanceEcommerceEvent(EVENT_SELECT_CONTENT, bundle)
    }
}
