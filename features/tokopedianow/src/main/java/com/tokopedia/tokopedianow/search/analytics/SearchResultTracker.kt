package com.tokopedia.tokopedianow.search.analytics

import android.os.Bundle
import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.product.detail.common.ProductTrackingConstant.Tracking.KEY_DIMENSION_81
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_ADD_TO_CART
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_TOKONOW
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_NAME_ADD_TO_CART
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_PRODUCT_CLICK
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_PRODUCT_VIEW
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_SELECT_CONTENT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_ITEM_LIST
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_BUSINESS_UNIT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CATEGORY_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CURRENT_SITE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_DIMENSION_100
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_DIMENSION_40
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_DIMENSION_45
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_DIMENSION_90
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_DIMENSION_96
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_INDEX
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEMS
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_BRAND
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_CATEGORY
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_LIST
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_NAME
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_VARIANT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_PRICE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_PRODUCT_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_QUANTITY
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_SHOP_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_SHOP_NAME
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_SHOP_TYPE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_TRACKER_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_USER_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_PHYSICAL_GOODS
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.CURRENT_SITE_HOME_AND_BROWSE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalytics.getTracker
import com.tokopedia.tokopedianow.common.util.TrackerUtil.getTrackerPosition
import com.tokopedia.tokopedianow.search.analytics.SearchResultTracker.Action.ACTION_CLICK_ATC_SRP_PRODUCT
import com.tokopedia.tokopedianow.search.analytics.SearchResultTracker.Action.ACTION_CLICK_SRP_PRODUCT
import com.tokopedia.tokopedianow.search.analytics.SearchResultTracker.Action.ACTION_CLICK_VIEW_ALL_RECOMMENDATION
import com.tokopedia.tokopedianow.search.analytics.SearchResultTracker.Action.ACTION_IMPRESSION_SRP_PRODUCT
import com.tokopedia.tokopedianow.search.analytics.SearchResultTracker.Category.CATEGORY_EMPTY_SEARCH_RESULT
import com.tokopedia.tokopedianow.search.analytics.SearchResultTracker.TrackerId.TRACKER_ID_CLICK_ATC_SRP_PRODUCT
import com.tokopedia.tokopedianow.search.analytics.SearchResultTracker.TrackerId.TRACKER_ID_CLICK_SRP_PRODUCT
import com.tokopedia.tokopedianow.search.analytics.SearchResultTracker.TrackerId.TRACKER_ID_IMPRESSION_SRP_PRODUCT
import com.tokopedia.tokopedianow.search.analytics.SearchResultTracker.Value.EMPTY
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils.EVENT
import com.tokopedia.track.TrackAppUtils.EVENT_ACTION
import com.tokopedia.track.TrackAppUtils.EVENT_CATEGORY
import com.tokopedia.track.TrackAppUtils.EVENT_LABEL

object SearchResultTracker {

    object Action {
        const val ACTION_IMPRESSION_SRP_PRODUCT =
            "impression product on tokonow product recommendation"
        const val ACTION_CLICK_SRP_PRODUCT =
            "click product on tokonow product recommendation"
        const val ACTION_CLICK_ATC_SRP_PRODUCT =
            "click add to cart on tokonow product recommendation"
        const val ACTION_CLICK_VIEW_ALL_RECOMMENDATION =
            "click view all on tokonow recommendation"
    }

    object Category {
        const val CATEGORY_EMPTY_SEARCH_RESULT = "tokonow empty search result"
    }

    object TrackerId {
        const val TRACKER_ID_IMPRESSION_SRP_PRODUCT = "17820"
        const val TRACKER_ID_CLICK_SRP_PRODUCT = "17822"
        const val TRACKER_ID_CLICK_ATC_SRP_PRODUCT = "17823"
    }

    object Value {
        const val EMPTY = ""
        const val SRP_PRODUCT_ITEM_LABEL =
            "/searchproduct - tokonow - recomproduct - rekomendasi untuk anda - %s"
    }

    fun trackImpressionProduct(
        position: Int,
        eventLabel: String,
        eventAction: String,
        eventCategory: String,
        itemList: String,
        userId: String,
        product: RecommendationItem
    ) {
        val productId = product.productId.toString()

        val items = arrayListOf(
            productItemDataLayer(
                index = position.getTrackerPosition().toString(),
                productId = productId,
                productName = product.name,
                price = product.priceInt,
            ).apply {
                putString(KEY_DIMENSION_100, "")
                putString(KEY_DIMENSION_40, itemList)
                putString(KEY_DIMENSION_81, "")
                putString(KEY_DIMENSION_90, "")
                putString(KEY_DIMENSION_96, "")
            }
        )

        val dataLayer = getEcommerceDataLayer(
            event = EVENT_VIEW_ITEM_LIST,
            action = eventAction,
            category = eventCategory,
            label = eventLabel,
            trackerId = getTrackerId(eventAction),
            itemList = itemList,
            items = items,
            productId = productId,
            userId = userId
        )

        getTracker().sendEnhanceEcommerceEvent(EVENT_PRODUCT_VIEW, dataLayer)
    }

    fun trackClickProduct(
        position: Int,
        eventLabel: String,
        eventAction: String,
        eventCategory: String,
        itemList: String,
        userId: String,
        product: RecommendationItem
    ) {
        val productId = product.productId.toString()

        val items = arrayListOf(
            productItemDataLayer(
                index = position.getTrackerPosition().toString(),
                productId = productId,
                productName = product.name,
                price = product.priceInt,
            ).apply {
                putString(KEY_DIMENSION_100, "")
                putString(KEY_DIMENSION_40, itemList)
                putString(KEY_DIMENSION_81, "")
                putString(KEY_DIMENSION_90, "")
                putString(KEY_DIMENSION_96, "")
            }
        )

        val dataLayer = getEcommerceDataLayer(
            event = EVENT_SELECT_CONTENT,
            action = eventAction,
            category = eventCategory,
            label = eventLabel,
            trackerId = getTrackerId(eventAction),
            itemList = itemList,
            items = items,
            productId = productId,
            userId = userId
        )

        getTracker().sendEnhanceEcommerceEvent(EVENT_PRODUCT_CLICK, dataLayer)
    }

    fun trackClickAddToCartProduct(
        eventLabel: String,
        userId: String,
        quantity: Int,
        cartId: String,
        product: RecommendationItem,
        eventCategory: String,
        eventAction: String,
        dimension40: String
    ) {
        val productId = product.productId.toString()

        val item = productItemDataLayer(
            productId = productId,
            productName = product.name,
            price = product.priceInt
        ).apply {
            putString(KEY_CATEGORY_ID, "")
            putString(KEY_DIMENSION_40, dimension40)
            putString(KEY_DIMENSION_45, cartId)
            putString(KEY_DIMENSION_90, "")
            putInt(KEY_QUANTITY, quantity)
            putInt(KEY_SHOP_ID, product.shopId)
            putString(KEY_SHOP_NAME, product.shopName)
            putString(KEY_SHOP_TYPE, product.shopType)
        }

        val dataLayer = getEcommerceDataLayer(
            event = EVENT_ADD_TO_CART,
            action = eventAction,
            category = eventCategory,
            label = eventLabel,
            trackerId =  getTrackerId(eventAction),
            items = arrayListOf(item),
            productId = productId,
            userId = userId
        )

        getTracker().sendEnhanceEcommerceEvent(EVENT_NAME_ADD_TO_CART, dataLayer)
    }

    fun sendRecommendationSeeAllClickEvent(keyword: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            DataLayer.mapOf(
                EVENT, EVENT_CLICK_TOKONOW,
                EVENT_ACTION, ACTION_CLICK_VIEW_ALL_RECOMMENDATION,
                EVENT_CATEGORY, CATEGORY_EMPTY_SEARCH_RESULT,
                EVENT_LABEL, keyword,
                KEY_BUSINESS_UNIT, BUSINESS_UNIT_PHYSICAL_GOODS,
                KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
            )
        )
    }

    private fun getEcommerceDataLayer(
        event: String,
        action: String,
        category: String,
        label: String = "",
        items: ArrayList<Bundle>,
        productId: String = "",
        userId: String = "",
        trackerId: String = "",
        itemList: String = ""
    ): Bundle {
        return Bundle().apply {
            putString(EVENT, event)
            putString(EVENT_ACTION, action)
            putString(EVENT_CATEGORY, category)
            putString(EVENT_LABEL, label)
            putString(KEY_BUSINESS_UNIT, BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE)
            putString(KEY_CURRENT_SITE, CURRENT_SITE_HOME_AND_BROWSE)
            putParcelableArrayList(KEY_ITEMS, items)

            if (userId.isNotEmpty()) {
                putString(KEY_USER_ID, userId)
            }
            if (productId.isNotBlank()) {
                putString(KEY_PRODUCT_ID, productId)
            }
            if (trackerId.isNotEmpty()) {
                putString(KEY_TRACKER_ID, trackerId)
            }
            if (itemList.isNotEmpty()) {
                putString(KEY_ITEM_LIST, itemList)
            }
        }
    }

    private fun productItemDataLayer(
        index: String = "",
        productId: String = "",
        productName: String = "",
        price: Int = 0,
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
            putInt(KEY_PRICE, price)
        }
    }

    private fun getTrackerId(eventAction: String): String {
        return when(eventAction) {
            ACTION_IMPRESSION_SRP_PRODUCT -> TRACKER_ID_IMPRESSION_SRP_PRODUCT
            ACTION_CLICK_SRP_PRODUCT -> TRACKER_ID_CLICK_SRP_PRODUCT
            ACTION_CLICK_ATC_SRP_PRODUCT -> TRACKER_ID_CLICK_ATC_SRP_PRODUCT
            else -> EMPTY
        }
    }
}
