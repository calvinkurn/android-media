package com.tokopedia.tokopedianow.searchcategory.analytics

import com.tokopedia.productcard.compact.productcard.presentation.uimodel.ProductCardCompactUiModel
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_ADD_TO_CART
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_SELECT_CONTENT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_ITEM_LIST
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEMS
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_LIST
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_GROCERIES
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalytics
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalytics.getTracker
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.tokopedianow.common.util.TrackerUtil.getTrackerPosition
import com.tokopedia.user.session.UserSessionInterface

/**
 * Ads Slot Tracker
 * https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/3991
 */
abstract class ProductAdsCarouselAnalytics(
    private val userSession: UserSessionInterface,
    private val addressData: TokoNowLocalAddress
) {

    companion object {
        private const val ACTION_IMPRESSION_ADS_SLOT = "impression product - ads slot"
        private const val ACTION_CLICK_ADS_SLOT = "click product - ads slot"
        private const val ACTION_ADD_TO_CART_ADS_SLOT = "click add to cart - ads slot"
    }

    abstract val eventCategory: String
    abstract val trackerIdImpression: String
    abstract val trackerIdClick: String
    abstract val trackerIdAddToCart: String

    fun trackProductImpression(
        position: Int,
        title: String,
        product: ProductCardCompactCarouselItemUiModel
    ) {
        val trackerPosition = position.getTrackerPosition()
        val eventLabel = "$title - $trackerPosition - ${product.getProductId()} - " +
            "${addressData.getWarehouseId()}"

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
            category = eventCategory,
            label = eventLabel,
            trackerId = trackerIdImpression,
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
        val eventLabel = "$title - $trackerPosition - ${product.getProductId()} - " +
            "${addressData.getWarehouseId()}"

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
            category = eventCategory,
            label = eventLabel,
            trackerId = trackerIdClick,
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
        position: Int,
        title: String,
        quantity: Int,
        shopId: String,
        shopName: String,
        shopType: String,
        categoryBreadcrumbs: String,
        product: ProductCardCompactUiModel
    ) {
        val productId = product.productId
        val eventLabel = "$title - $position - $productId - " +
            "${addressData.getWarehouseId()}"

        val productItemsDataLayer = arrayListOf(
            TokoNowCommonAnalytics.productItemDataLayer(
                itemCategory = categoryBreadcrumbs,
                itemId = productId,
                itemName = product.name,
                price = product.price,
                quantity = quantity,
                shopId = shopId,
                shopName = shopName,
                shopType = shopType
            )
        )

        val dataLayer = TokoNowCommonAnalytics.getDataLayer(
            event = EVENT_ADD_TO_CART,
            action = ACTION_ADD_TO_CART_ADS_SLOT,
            category = eventCategory,
            label = eventLabel,
            trackerId = trackerIdAddToCart,
            businessUnit = BUSINESS_UNIT_GROCERIES,
            currentSite = CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
            userId = userSession.userId
        ).apply {
            putParcelableArrayList(KEY_ITEMS, productItemsDataLayer)
        }

        getTracker().sendEnhanceEcommerceEvent(EVENT_ADD_TO_CART, dataLayer)
    }
}
