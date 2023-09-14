package com.tokopedia.tokopedianow.category.analytic

import com.tokopedia.kotlin.extensions.view.getDigits
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.productcard.compact.similarproduct.presentation.uimodel.ProductCardCompactSimilarProductUiModel
import com.tokopedia.tokopedianow.category.analytic.CategoryL2Analytic.Companion.EVENT_CATEGORY_PAGE_L2
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_ADD_TO_CART
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_GROCERIES
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_ITEM_LIST
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_DIMENSION_56
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_DIMENSION_98
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEMS
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_LIST
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_TRACKER_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_WAREHOUSE_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_GROCERIES
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalytics
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface

class CategoryL2SimilarProductAnalytic(private val userSession: UserSessionInterface) {

    companion object {

        // Event Action
        private const val EVENT_ACTION_CLICK_DROP_DOWN = "click produk serupa dropdown"
        private const val EVENT_ACTION_IMPRESSION_SIMILAR_PRODUCT =
            "impression produk serupa bottom sheet"
        private const val EVENT_ACTION_ATC_SIMILAR_PRODUCT = "click atc on produk serupa"
        private const val EVENT_ACTION_CLICK_CLOSE_SIMILAR_PRODUCT = "click close on produk serupa"

        // TrackerId
        private const val TRACKER_ID_CLICK_DROP_DOWN = "45269"
        private const val TRACKER_ID_IMPRESSION_SIMILAR_PRODUCT = "45270"
        private const val TRACKER_ID_ATC_SIMILAR_PRODUCT = "45271"
        private const val TRACKER_ID_CLICK_CLOSE_SIMILAR_PRODUCT = "45272"
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3979
    // Tracker ID: 45269
    fun sendClickSimilarProductDropdownEvent(
        index: Int,
        productId: String,
        categoryIdL2: String
    ) {
        val position = index + 1
        val eventLabel = "$categoryIdL2 - $position - $productId"

        Tracker.Builder()
            .setEvent(EVENT_CLICK_GROCERIES)
            .setEventAction(EVENT_ACTION_CLICK_DROP_DOWN)
            .setEventCategory(EVENT_CATEGORY_PAGE_L2)
            .setEventLabel(eventLabel)
            .setCustomProperty(KEY_TRACKER_ID, TRACKER_ID_CLICK_DROP_DOWN)
            .setBusinessUnit(BUSINESS_UNIT_GROCERIES)
            .setCurrentSite("")
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3979
    // Tracker ID: 45270
    fun sendProductImpressionEvent(
        categoryIdL2: String,
        product: ProductCardCompactSimilarProductUiModel
    ) {
        val position = product.position
        val productId = product.id
        val eventLabel = "$categoryIdL2 - $position - $productId"
        val isAvailable = product.stock.isMoreThanZero()

        val productItemsDataLayer = arrayListOf(
            TokoNowCommonAnalytics.productItemDataLayer(
                position = position,
                itemCategory = product.categoryName,
                itemId = productId,
                itemName = product.name,
                price = product.priceFmt.getDigits()?.toFloat().toString(),
                dimension40 = ""
            ).apply {
                putString(KEY_DIMENSION_56, product.warehouseIds)
                putBoolean(KEY_DIMENSION_98, isAvailable)
            }
        )

        val dataLayer = TokoNowCommonAnalytics.getDataLayer(
            event = EVENT_VIEW_ITEM_LIST,
            action = EVENT_ACTION_IMPRESSION_SIMILAR_PRODUCT,
            category = EVENT_CATEGORY_PAGE_L2,
            label = eventLabel,
            trackerId = TRACKER_ID_IMPRESSION_SIMILAR_PRODUCT,
            businessUnit = BUSINESS_UNIT_GROCERIES,
            currentSite = "",
            userId = userSession.userId
        ).apply {
            putString(KEY_ITEM_LIST, "/nowrecom - product card - similar product recom")
            putParcelableArrayList(KEY_ITEMS, productItemsDataLayer)
        }

        TokoNowCommonAnalytics.getTracker()
            .sendEnhanceEcommerceEvent(EVENT_VIEW_ITEM_LIST, dataLayer)
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3979
    // Tracker ID: 45271
    fun sendProductAddToCartEvent(
        categoryIdL2: String,
        quantity: Int,
        product: ProductCardCompactSimilarProductUiModel
    ) {
        val position = product.position
        val productId = product.id
        val categoryName = product.categoryName
        val shopId = product.shopId
        val shopName = product.shopName

        val eventLabel = "$categoryIdL2 - $position - $productId"
        val isAvailable = product.stock.isMoreThanZero()

        val productItemsDataLayer = arrayListOf(
            TokoNowCommonAnalytics.productItemDataLayer(
                categoryId = categoryIdL2,
                itemCategory = categoryName,
                itemId = productId,
                itemName = product.name,
                price = product.priceFmt.getDigits()?.toFloat().toString(),
                quantity = quantity,
                shopId = shopId,
                shopName = shopName
            ).apply {
                putString(KEY_DIMENSION_56, product.warehouseIds)
                putBoolean(KEY_DIMENSION_98, isAvailable)
            }
        )

        val dataLayer = TokoNowCommonAnalytics.getDataLayer(
            event = EVENT_ADD_TO_CART,
            action = EVENT_ACTION_ATC_SIMILAR_PRODUCT,
            category = EVENT_CATEGORY_PAGE_L2,
            label = eventLabel,
            trackerId = TRACKER_ID_ATC_SIMILAR_PRODUCT,
            businessUnit = BUSINESS_UNIT_GROCERIES,
            currentSite = "",
            userId = userSession.userId
        ).apply {
            putParcelableArrayList(KEY_ITEMS, productItemsDataLayer)
        }

        TokoNowCommonAnalytics.getTracker()
            .sendEnhanceEcommerceEvent(EVENT_ADD_TO_CART, dataLayer)
    }


    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3979
    // Tracker ID: 45272
    fun sendClickCloseOnSimilarProductEvent(
        categoryIdL2: String,
        warehouseIds: String
    ) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_GROCERIES)
            .setEventAction(EVENT_ACTION_CLICK_CLOSE_SIMILAR_PRODUCT)
            .setEventCategory(EVENT_CATEGORY_PAGE_L2)
            .setEventLabel(categoryIdL2)
            .setCustomProperty(KEY_TRACKER_ID, TRACKER_ID_CLICK_CLOSE_SIMILAR_PRODUCT)
            .setBusinessUnit(BUSINESS_UNIT_GROCERIES)
            .setCurrentSite("")
            .setCustomProperty(KEY_WAREHOUSE_ID, warehouseIds)
            .build()
            .send()
    }
}
