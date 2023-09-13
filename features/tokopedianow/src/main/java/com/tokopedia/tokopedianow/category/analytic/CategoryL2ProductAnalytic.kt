package com.tokopedia.tokopedianow.category.analytic

import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.tokopedianow.category.analytic.CategoryL2Analytic.Companion.EVENT_CATEGORY_PAGE_L2
import com.tokopedia.tokopedianow.category.presentation.model.CategoryAtcTrackerModel
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_ADD_TO_CART
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_SELECT_CONTENT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_ITEM_LIST
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_DIMENSION_56
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_DIMENSION_98
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEMS
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_LIST
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_GROCERIES
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalytics
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.user.session.UserSessionInterface

class CategoryL2ProductAnalytic(private val userSession: UserSessionInterface) {

    companion object {

        // Event Action
        private const val EVENT_ACTION_ATC_PRODUCT = "click atc on product card"
        private const val EVENT_ACTION_CLICK_PRODUCT = "click product card"
        private const val EVENT_ACTION_IMPRESSION_PRODUCT = "impression product card"

        // TrackerId
        private const val TRACKER_ID_ATC_PRODUCT = "43885"
        private const val TRACKER_ID_CLICK_PRODUCT = "43886"
        private const val TRACKER_ID_IMPRESSION_PRODUCT = "45255"
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3979
    // Tracker ID: 45255
    fun trackProductImpression(
        categoryIdL1: String,
        productItem: ProductItemDataView
    ) {
        val position = productItem.position
        val product = productItem.productCardModel
        val productId = product.productId
        val eventLabel = "$categoryIdL1 - $position - $productId"
        val isAvailable = product.availableStock.isMoreThanZero()

        val productItemsDataLayer = arrayListOf(
            TokoNowCommonAnalytics.productItemDataLayer(
                position = position,
                itemCategory = productItem.categoryBreadcrumbs,
                itemId = productId,
                itemName = product.name,
                price = product.price,
                dimension40 = ""
            ).apply {
                putString(KEY_DIMENSION_56, product.warehouseId)
                putBoolean(KEY_DIMENSION_98, isAvailable)
            }
        )

        val dataLayer = TokoNowCommonAnalytics.getDataLayer(
            event = EVENT_VIEW_ITEM_LIST,
            action = EVENT_ACTION_IMPRESSION_PRODUCT,
            category = EVENT_CATEGORY_PAGE_L2,
            label = eventLabel,
            trackerId = TRACKER_ID_IMPRESSION_PRODUCT,
            businessUnit = BUSINESS_UNIT_GROCERIES,
            currentSite = "",
            userId = userSession.userId
        ).apply {
            putString(KEY_ITEM_LIST, "")
            putParcelableArrayList(KEY_ITEMS, productItemsDataLayer)
        }

        TokoNowCommonAnalytics.getTracker()
            .sendEnhanceEcommerceEvent(EVENT_VIEW_ITEM_LIST, dataLayer)
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3979
    // Tracker ID: 43886
    fun trackProductClick(
        categoryIdL1: String,
        productItem: ProductItemDataView
    ) {
        val position = productItem.position
        val product = productItem.productCardModel
        val productId = product.productId
        val eventLabel = "$categoryIdL1 - $position - $productId"
        val isAvailable = product.availableStock.isMoreThanZero()

        val productItemsDataLayer = arrayListOf(
            TokoNowCommonAnalytics.productItemDataLayer(
                position = position,
                itemCategory = productItem.categoryBreadcrumbs,
                itemId = productId,
                itemName = product.name,
                price = product.price,
                dimension40 = ""
            ).apply {
                putString(KEY_DIMENSION_56, product.warehouseId)
                putBoolean(KEY_DIMENSION_98, isAvailable)
            }
        )

        val dataLayer = TokoNowCommonAnalytics.getDataLayer(
            event = EVENT_SELECT_CONTENT,
            action = EVENT_ACTION_CLICK_PRODUCT,
            category = EVENT_CATEGORY_PAGE_L2,
            label = eventLabel,
            trackerId = TRACKER_ID_CLICK_PRODUCT,
            businessUnit = BUSINESS_UNIT_GROCERIES,
            currentSite = "",
            userId = userSession.userId
        ).apply {
            putString(KEY_ITEM_LIST, "")
            putParcelableArrayList(KEY_ITEMS, productItemsDataLayer)
        }

        TokoNowCommonAnalytics.getTracker()
            .sendEnhanceEcommerceEvent(EVENT_SELECT_CONTENT, dataLayer)
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3979
    // Tracker ID: 43885
    fun trackProductAddToCart(data: CategoryAtcTrackerModel) {
        val position = data.index
        val product = data.product
        val categoryIdL1 = data.categoryIdL1
        val categoryIdL2 = data.categoryIdL2
        val categoryBreadcrumbs = data.categoryBreadcrumbs
        val quantity = data.quantity
        val shopId = data.shopId
        val shopName = data.shopName
        val shopType = data.shopType

        val productId = product.productId
        val eventLabel = "$categoryIdL1 - $position - $productId"
        val isAvailable = product.availableStock.isMoreThanZero()

        val productItemsDataLayer = arrayListOf(
            TokoNowCommonAnalytics.productItemDataLayer(
                categoryId = categoryIdL2,
                itemCategory = categoryBreadcrumbs,
                itemId = productId,
                itemName = product.name,
                price = product.price,
                quantity = quantity,
                shopId = shopId,
                shopName = shopName,
                shopType = shopType
            ).apply {
                putString(KEY_DIMENSION_56, product.warehouseId)
                putBoolean(KEY_DIMENSION_98, isAvailable)
            }
        )

        val dataLayer = TokoNowCommonAnalytics.getDataLayer(
            event = EVENT_ADD_TO_CART,
            action = EVENT_ACTION_ATC_PRODUCT,
            category = EVENT_CATEGORY_PAGE_L2,
            label = eventLabel,
            trackerId = TRACKER_ID_ATC_PRODUCT,
            businessUnit = BUSINESS_UNIT_GROCERIES,
            currentSite = "",
            userId = userSession.userId
        ).apply {
            putParcelableArrayList(KEY_ITEMS, productItemsDataLayer)
        }

        TokoNowCommonAnalytics.getTracker()
            .sendEnhanceEcommerceEvent(EVENT_ADD_TO_CART, dataLayer)
    }
}
