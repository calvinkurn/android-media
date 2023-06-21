package com.tokopedia.tokopedianow.category.analytic

import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.tokopedianow.category.analytic.CategoryAnalytic.CATEGORY.EVENT_CATEGORY_PAGE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_ADD_TO_CART
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_SELECT_CONTENT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_ITEM_LIST
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEMS
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_LIST
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_GROCERIES
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalytics
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalytics.getTracker
import com.tokopedia.tokopedianow.common.util.TrackerUtil.getTrackerPosition
import com.tokopedia.user.session.UserSessionInterface

/**
 * Ads Slot Tracker
 * https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/3991
 */

class CategoryProductAdsAnalytic(
    private val userSession: UserSessionInterface
) {

    companion object {
        private const val ACTION_IMPRESSION_ADS_SLOT = "impression product - ads slot"
        private const val ACTION_CLICK_ADS_SLOT = "click product - ads slot"
        private const val ACTION_ADD_TO_CART_ADS_SLOT = "click add to cart - ads slot"

        private const val TRACKER_ID_PRODUCT_IMPRESSION = "44063"
        private const val TRACKER_ID_PRODUCT_CLICK = "44064"
        private const val TRACKER_ID_PRODUCT_ATC = "44065"
    }

    fun trackProductImpression(
        position: Int,
        title: String,
        product: ProductCardCompactCarouselItemUiModel
    ) {
        val trackerPosition = position.getTrackerPosition()
        val eventLabel = "$title - $trackerPosition - ${product.getProductId()}"

        val productItemsDataLayer = arrayListOf(
            TokoNowCommonAnalytics.productItemDataLayer(
                position = trackerPosition,
                itemCategory = product.categoryBreadcrumbs,
                itemId = product.getProductId(),
                itemName = product.getProductName(),
                price = product.getProductPrice(),
                dimension40 = ""
            )
        )

        val dataLayer = TokoNowCommonAnalytics.getDataLayer(
            event = EVENT_VIEW_ITEM_LIST,
            action = ACTION_IMPRESSION_ADS_SLOT,
            category = EVENT_CATEGORY_PAGE,
            label = eventLabel,
            trackerId = TRACKER_ID_PRODUCT_IMPRESSION,
            businessUnit = BUSINESS_UNIT_GROCERIES,
            currentSite = CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
            userId = userSession.userId
        ).apply {
            putString(KEY_ITEM_LIST, "")
            putParcelableArrayList(KEY_ITEMS, productItemsDataLayer)
        }

        getTracker().sendEnhanceEcommerceEvent(EVENT_VIEW_ITEM_LIST, dataLayer)
    }

    fun trackProductClick(
        position: Int,
        title: String,
        product: ProductCardCompactCarouselItemUiModel
    ) {
        val trackerPosition = position.getTrackerPosition()
        val eventLabel = "$title - $trackerPosition - ${product.getProductId()}"

        val productItemsDataLayer = arrayListOf(
            TokoNowCommonAnalytics.productItemDataLayer(
                position = trackerPosition,
                itemCategory = product.categoryBreadcrumbs,
                itemId = product.getProductId(),
                itemName = product.getProductName(),
                price = product.getProductPrice(),
                dimension40 = ""
            )
        )

        val dataLayer = TokoNowCommonAnalytics.getDataLayer(
            event = EVENT_SELECT_CONTENT,
            action = ACTION_CLICK_ADS_SLOT,
            category = EVENT_CATEGORY_PAGE,
            label = eventLabel,
            trackerId = TRACKER_ID_PRODUCT_CLICK,
            businessUnit = BUSINESS_UNIT_GROCERIES,
            currentSite = CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
            userId = userSession.userId
        ).apply {
            putString(KEY_ITEM_LIST, "")
            putParcelableArrayList(KEY_ITEMS, productItemsDataLayer)
        }

        getTracker().sendEnhanceEcommerceEvent(EVENT_SELECT_CONTENT, dataLayer)
    }

    fun trackProductAddToCart(
        title: String,
        quantity: Int,
        product: ProductCardCompactCarouselItemUiModel
    ) {
        val trackerPosition = product.index.getTrackerPosition()
        val eventLabel = "$title - $trackerPosition - ${product.getProductId()}"

        val productItemsDataLayer = arrayListOf(
            TokoNowCommonAnalytics.productItemDataLayer(
                itemCategory = product.categoryBreadcrumbs,
                itemId = product.getProductId(),
                itemName = product.getProductName(),
                price = product.getProductPrice(),
                quantity = quantity,
                shopId = product.shopId,
                shopName = product.shopName,
                shopType = product.shopType,
            )
        )

        val dataLayer = TokoNowCommonAnalytics.getDataLayer(
            event = EVENT_ADD_TO_CART,
            action = ACTION_ADD_TO_CART_ADS_SLOT,
            category = EVENT_CATEGORY_PAGE,
            label = eventLabel,
            trackerId = TRACKER_ID_PRODUCT_ATC,
            businessUnit = BUSINESS_UNIT_GROCERIES,
            currentSite = CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
            userId = userSession.userId
        ).apply {
            putParcelableArrayList(KEY_ITEMS, productItemsDataLayer)
        }

        getTracker().sendEnhanceEcommerceEvent(EVENT_ADD_TO_CART, dataLayer)
    }
}
