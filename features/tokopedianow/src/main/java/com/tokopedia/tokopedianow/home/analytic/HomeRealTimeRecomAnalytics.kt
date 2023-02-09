package com.tokopedia.tokopedianow.home.analytic

import android.os.Bundle
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.tokopedianow.common.analytics.RealTimeRecommendationAnalytics
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.CATEGORY.EVENT_CATEGORY_TOKONOW_HOMEPAGE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_ADD_TO_CART
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_NAME_ADD_TO_CART
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_PRODUCT_CLICK
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_PRODUCT_VIEW
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_SELECT_CONTENT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_IRIS
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_ITEM_LIST
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
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_TRACKER_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_USER_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_WAREHOUSE_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalytics.getTracker
import com.tokopedia.productcard_compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.tokopedianow.home.analytic.HomeRealTimeRecomAnalytics.ACTION.ACTION_ADD_TO_CART_PRODUCT
import com.tokopedia.tokopedianow.home.analytic.HomeRealTimeRecomAnalytics.ACTION.ACTION_CLICK_CLOSE_BUTTON
import com.tokopedia.tokopedianow.home.analytic.HomeRealTimeRecomAnalytics.ACTION.ACTION_CLICK_RTR_PRODUCT
import com.tokopedia.tokopedianow.home.analytic.HomeRealTimeRecomAnalytics.ACTION.ACTION_CLICK_RTR_REFRESH_BUTTON
import com.tokopedia.tokopedianow.home.analytic.HomeRealTimeRecomAnalytics.ACTION.ACTION_IMPRESSION_RTR_PRODUCT
import com.tokopedia.tokopedianow.home.analytic.HomeRealTimeRecomAnalytics.ACTION.ACTION_IMPRESSION_RTR_REFRESH_BUTTON
import com.tokopedia.tokopedianow.home.analytic.HomeRealTimeRecomAnalytics.ACTION.ACTION_IMPRESSION_RTR_WIDGET
import com.tokopedia.tokopedianow.home.analytic.HomeRealTimeRecomAnalytics.ID.TRACKER_ID_CLOSE_BTN_CLICK
import com.tokopedia.tokopedianow.home.analytic.HomeRealTimeRecomAnalytics.ID.TRACKER_ID_PRODUCT_ATC
import com.tokopedia.tokopedianow.home.analytic.HomeRealTimeRecomAnalytics.ID.TRACKER_ID_PRODUCT_CLICK
import com.tokopedia.tokopedianow.home.analytic.HomeRealTimeRecomAnalytics.ID.TRACKER_ID_PRODUCT_IMPRESSION
import com.tokopedia.tokopedianow.home.analytic.HomeRealTimeRecomAnalytics.ID.TRACKER_ID_REFRESH_BTN_CLICK
import com.tokopedia.tokopedianow.home.analytic.HomeRealTimeRecomAnalytics.ID.TRACKER_ID_REFRESH_BTN_IMPRESSION
import com.tokopedia.tokopedianow.home.analytic.HomeRealTimeRecomAnalytics.ID.TRACKER_ID_WIDGET_IMPRESSION
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.user.session.UserSessionInterface

/**
 * Home Real Time Recom Tracker
 * https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/3552
 */
class HomeRealTimeRecomAnalytics(
    private val userSession: UserSessionInterface
) : RealTimeRecommendationAnalytics {

    internal object ACTION {
        const val ACTION_IMPRESSION_RTR_WIDGET = "impression rtr widget"
        const val ACTION_IMPRESSION_RTR_REFRESH_BUTTON = "impression rtr tampilkan button"
        const val ACTION_CLICK_RTR_REFRESH_BUTTON = "click rtr tampilkan button"
        const val ACTION_CLICK_CLOSE_BUTTON = "click close rtr widget"
        const val ACTION_IMPRESSION_RTR_PRODUCT = "rtr widget product impression"
        const val ACTION_CLICK_RTR_PRODUCT = "rtr widget product click"
        const val ACTION_ADD_TO_CART_PRODUCT = "rtr widget product atc"
    }

    internal object ID {
        const val TRACKER_ID_WIDGET_IMPRESSION = "38356"
        const val TRACKER_ID_REFRESH_BTN_IMPRESSION = "38357"
        const val TRACKER_ID_REFRESH_BTN_CLICK = "38358"
        const val TRACKER_ID_CLOSE_BTN_CLICK = "38359"
        const val TRACKER_ID_PRODUCT_IMPRESSION = "38360"
        const val TRACKER_ID_PRODUCT_CLICK = "38361"
        const val TRACKER_ID_PRODUCT_ATC = "38362"
    }

    override fun trackWidgetImpression(productId: String, warehouseId: String) {
        val dataLayer = getDataLayer(
            event = EVENT_VIEW_IRIS,
            action = ACTION_IMPRESSION_RTR_WIDGET,
            label = productId,
            trackerId = TRACKER_ID_WIDGET_IMPRESSION
        ).apply {
            putString(KEY_WAREHOUSE_ID, warehouseId)
        }

        getTracker().sendEnhanceEcommerceEvent(EVENT_VIEW_IRIS, dataLayer)
    }

    override fun trackRefreshImpression(productId: String) {
        val dataLayer = getDataLayer(
            event = EVENT_VIEW_IRIS,
            action = ACTION_IMPRESSION_RTR_REFRESH_BUTTON,
            label = productId,
            trackerId = TRACKER_ID_REFRESH_BTN_IMPRESSION
        )

        getTracker().sendEnhanceEcommerceEvent(EVENT_VIEW_IRIS, dataLayer)
    }

    override fun trackClickRefresh(productId: String) {
        val dataLayer = getDataLayer(
            event = EVENT_CLICK,
            action = ACTION_CLICK_RTR_REFRESH_BUTTON,
            label = productId,
            trackerId = TRACKER_ID_REFRESH_BTN_CLICK
        )

        getTracker().sendEnhanceEcommerceEvent(EVENT_CLICK, dataLayer)
    }

    override fun trackClickClose(productId: String) {
        val dataLayer = getDataLayer(
            event = EVENT_CLICK,
            action = ACTION_CLICK_CLOSE_BUTTON,
            label = productId,
            trackerId = TRACKER_ID_CLOSE_BTN_CLICK
        )

        getTracker().sendEnhanceEcommerceEvent(EVENT_CLICK, dataLayer)
    }

    override fun trackProductImpression(
        headerName: String,
        productId: String,
        item: ProductCardCompactCarouselItemUiModel,
        position: Int
    ) {
        val items = arrayListOf(
            productItemDataLayer(
                index = position.toString(),
                productId = item.getProductId(),
                productName = item.getProductName(),
                price = item.getProductPrice().filter { it.isDigit() }.toLongOrZero(),
                productCategory = item.categoryBreadcrumbs
            )
        )

        val itemList = "/ - p$position - rtr widget - carousel - $headerName"

        val dataLayer = getDataLayer(
            event = EVENT_VIEW_ITEM_LIST,
            action = ACTION_IMPRESSION_RTR_PRODUCT,
            label = productId,
            trackerId = TRACKER_ID_PRODUCT_IMPRESSION
        ).apply {
            putString(KEY_ITEM_LIST, itemList)
            putParcelableArrayList(KEY_ITEMS, items)
            putString(KEY_USER_ID, userSession.userId)
        }

        getTracker().sendEnhanceEcommerceEvent(EVENT_PRODUCT_VIEW, dataLayer)
    }

    override fun trackProductClick(
        headerName: String,
        productId: String,
        item: ProductCardCompactCarouselItemUiModel,
        position: Int
    ) {
        val items = arrayListOf(
            productItemDataLayer(
                index = position.toString(),
                productId = item.getProductId(),
                productName = item.getProductName(),
                price = item.getProductPrice().filter { it.isDigit() }.toLongOrZero(),
                productCategory = item.categoryBreadcrumbs
            )
        )

        val itemList = "/ - p$position - rtr widget - carousel - $headerName"

        val dataLayer = getDataLayer(
            event = EVENT_SELECT_CONTENT,
            action = ACTION_CLICK_RTR_PRODUCT,
            label = productId,
            trackerId = TRACKER_ID_PRODUCT_CLICK
        ).apply {
            putString(KEY_ITEM_LIST, itemList)
            putParcelableArrayList(KEY_ITEMS, items)
            putString(KEY_USER_ID, userSession.userId)
        }

        getTracker().sendEnhanceEcommerceEvent(EVENT_PRODUCT_CLICK, dataLayer)
    }

    override fun trackAddToCart(
        productId: String,
        item: ProductCardCompactCarouselItemUiModel,
        quantity: Int
    ) {
        val items = arrayListOf(
            productItemDataLayer(
                item = item,
                quantity = quantity
            )
        )

        val dataLayer = getDataLayer(
            event = EVENT_ADD_TO_CART,
            action = ACTION_ADD_TO_CART_PRODUCT,
            label = productId,
            trackerId = TRACKER_ID_PRODUCT_ATC
        ).apply {
            putParcelableArrayList(KEY_ITEMS, items)
            putString(KEY_USER_ID, userSession.userId)
        }

        getTracker().sendEnhanceEcommerceEvent(EVENT_NAME_ADD_TO_CART, dataLayer)
    }

    private fun productItemDataLayer(
        index: String = "",
        productId: String = "",
        productName: String = "",
        price: Long = 0L,
        productBrand: String = "",
        productCategory: String = "",
        productVariant: String = ""
    ): Bundle {
        return Bundle().apply {
            if (index.isNotBlank()) {
                putString(KEY_INDEX, index)
            }
            putString(KEY_ITEM_BRAND, productBrand)
            putString(KEY_ITEM_CATEGORY, productCategory)
            putString(KEY_ITEM_ID, productId)
            putString(KEY_ITEM_NAME, productName)
            putString(KEY_ITEM_VARIANT, productVariant)
            putLong(KEY_PRICE, price)
        }
    }

    private fun productItemDataLayer(
        categoryId: String = "",
        productBrand: String = "",
        productVariant: String = "",
        quantity: Int = 0,
        item: ProductCardCompactCarouselItemUiModel
    ): Bundle {
        val productCategory = item.categoryBreadcrumbs
        val productId = item.getProductId()
        val productName = item.getProductName()
        val price = item.getProductPrice().filter { it.isDigit() }.toLongOrZero()
        val shopId = item.shopId.toIntOrZero()
        val shopName = item.shopName
        val shopType = item.shopType

        return Bundle().apply {
            putString(KEY_CATEGORY_ID, categoryId)
            putString(KEY_ITEM_BRAND, productBrand)
            putString(KEY_ITEM_CATEGORY, productCategory)
            putString(KEY_ITEM_ID, productId)
            putString(KEY_ITEM_NAME, productName)
            putString(KEY_ITEM_VARIANT, productVariant)
            putLong(KEY_PRICE, price)
            putInt(KEY_QUANTITY, quantity)
            putInt(KEY_SHOP_ID, shopId)
            putString(KEY_SHOP_NAME, shopName)
            putString(KEY_SHOP_TYPE, shopType)
        }
    }

    private fun getDataLayer(
        event: String,
        action: String,
        label: String = "",
        trackerId: String = ""
    ): Bundle {
        return Bundle().apply {
            putString(TrackAppUtils.EVENT, event)
            putString(TrackAppUtils.EVENT_ACTION, action)
            putString(TrackAppUtils.EVENT_CATEGORY, EVENT_CATEGORY_TOKONOW_HOMEPAGE)
            putString(TrackAppUtils.EVENT_LABEL, label)

            if(trackerId.isNotEmpty()) {
                putString(KEY_TRACKER_ID, trackerId)
            }

            putString(KEY_BUSINESS_UNIT, BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE)
            putString(KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
        }
    }
}
