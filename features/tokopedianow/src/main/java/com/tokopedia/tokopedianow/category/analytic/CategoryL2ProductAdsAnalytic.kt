package com.tokopedia.tokopedianow.category.analytic

import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
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
import com.tokopedia.user.session.UserSessionInterface

class CategoryL2ProductAdsAnalytic(private val userSession: UserSessionInterface) {

    companion object {
        private const val EVENT_ACTION_IMPRESSION_ADS_SLOT = "impression product - ads slot"
        private const val EVENT_ACTION_CLICK_ADS_SLOT = "click product - ads slot"
        private const val EVENT_ACTION_ATC_ADS_SLOT = "click atc - ads slot"

        //Tracker Id
        private const val TRACKER_ID_IMPRESSION_ADS_SLOT = "43881"
        private const val TRACKER_ID_CLICK_ADS_SLOT = "43883"
        private const val TRACKER_ID_ATC_ADS_SLOT = "43884"
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3979
    // Tracker ID: 43881
    fun trackProductAdsImpression(
        index: Int,
        title: String,
        categoryIdL1: String,
        product: ProductCardCompactCarouselItemUiModel
    ) {
        val position = index + 1
        val eventLabel = "$categoryIdL1 - $title - $position - ${product.getProductId()}"
        val isAvailable = product.productCardModel.availableStock.isMoreThanZero()

        val productItemsDataLayer = arrayListOf(
            TokoNowCommonAnalytics.productItemDataLayer(
                position = position,
                itemCategory = product.categoryBreadcrumbs,
                itemId = product.getProductId(),
                itemName = product.getProductName(),
                price = product.getProductPrice(),
                dimension40 = ""
            ).apply {
                putString(KEY_DIMENSION_56, "")
                putBoolean(KEY_DIMENSION_98, isAvailable)
            }
        )

        val dataLayer = TokoNowCommonAnalytics.getDataLayer(
            event = EVENT_VIEW_ITEM_LIST,
            action = EVENT_ACTION_IMPRESSION_ADS_SLOT,
            category = EVENT_CATEGORY_PAGE_L2,
            label = eventLabel,
            trackerId = TRACKER_ID_IMPRESSION_ADS_SLOT,
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
    // Tracker ID: 43883
    fun trackProductAdsClick(
        index: Int,
        title: String,
        categoryIdL1: String,
        product: ProductCardCompactCarouselItemUiModel
    ) {
        val position = index + 1
        val eventLabel = "$categoryIdL1 - $title - $position - ${product.getProductId()}"
        val isAvailable = product.productCardModel.availableStock.isMoreThanZero()

        val productItemsDataLayer = arrayListOf(
            TokoNowCommonAnalytics.productItemDataLayer(
                position = position,
                itemCategory = product.categoryBreadcrumbs,
                itemId = product.getProductId(),
                itemName = product.getProductName(),
                price = product.getProductPrice(),
                dimension40 = ""
            ).apply {
                putString(KEY_DIMENSION_56, "")
                putBoolean(KEY_DIMENSION_98, isAvailable)
            }
        )

        val dataLayer = TokoNowCommonAnalytics.getDataLayer(
            event = EVENT_SELECT_CONTENT,
            action = EVENT_ACTION_CLICK_ADS_SLOT,
            category = EVENT_CATEGORY_PAGE_L2,
            label = eventLabel,
            trackerId = TRACKER_ID_CLICK_ADS_SLOT,
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
    // Tracker ID: 43884
    fun trackProductAdsAddToCart(
        title: String,
        data: CategoryAtcTrackerModel
    ) {
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
        val eventLabel = "$categoryIdL1 - $title - $position - $productId"
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
                putString(KEY_DIMENSION_56, "")
                putBoolean(KEY_DIMENSION_98, isAvailable)
            }
        )

        val dataLayer = TokoNowCommonAnalytics.getDataLayer(
            event = EVENT_ADD_TO_CART,
            action = EVENT_ACTION_ATC_ADS_SLOT,
            category = EVENT_CATEGORY_PAGE_L2,
            label = eventLabel,
            trackerId = TRACKER_ID_ATC_ADS_SLOT,
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
