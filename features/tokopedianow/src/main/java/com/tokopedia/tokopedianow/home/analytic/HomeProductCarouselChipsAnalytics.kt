package com.tokopedia.tokopedianow.home.analytic

import android.os.Bundle
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.CATEGORY.EVENT_CATEGORY_TOKONOW_RECOM_HOMEPAGE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_ADD_TO_CART
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_SELECT_CONTENT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_ITEM_LIST
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_BUSINESS_UNIT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CATEGORY_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CURRENT_SITE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_DIMENSION_40
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_DIMENSION_45
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_DIMENSION_84
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_DIMENSION_90
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
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_HOME_AND_BROWSE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalytics.getTracker
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.tokopedianow.common.util.TrackerUtil.getTrackerPosition
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProductCarouselChipsUiModel
import com.tokopedia.track.TrackAppUtils.EVENT
import com.tokopedia.track.TrackAppUtils.EVENT_ACTION
import com.tokopedia.track.TrackAppUtils.EVENT_CATEGORY
import com.tokopedia.track.TrackAppUtils.EVENT_LABEL
import com.tokopedia.user.session.UserSessionInterface

/**
 * Thanos: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3875
 */

class HomeProductCarouselChipsAnalytics(
    private val userSession: UserSessionInterface
) {

    object Event {
        const val VIEW_HOMEPAGE_IRIS = "viewHomepageIris"
        const val CLICK_HOMEPAGE = "clickHomepage"
    }

    object Action {
        const val IMPRESSION_CHIP_WIDGET = "impression chip widget"
        const val CLICK_CHIP_NAME = "click chip name"
        const val IMPRESSION_PRODUCT = "chip widget product impression"
        const val CLICK_PRODUCT = "chip widget product click"
        const val ADD_TO_CART = "chip widget product atc"
    }

    object TrackerId {
        const val IMPRESSION_CHIP_WIDGET = "42883"
        const val CLICK_CHIP_NAME = "42884"
        const val IMPRESSION_PRODUCT = "42885"
        const val CLICK_PRODUCT = "42886"
        const val ADD_TO_CART = "42887"
    }

    fun trackWidgetImpression() {
        val dataLayer = getDataLayer(
            event = Event.VIEW_HOMEPAGE_IRIS,
            action = Action.IMPRESSION_CHIP_WIDGET,
            label = "",
            trackerId = TrackerId.IMPRESSION_CHIP_WIDGET
        )

        getTracker().sendEnhanceEcommerceEvent(Event.VIEW_HOMEPAGE_IRIS, dataLayer)
    }

    fun trackClickChip(
        channelId: String,
        headerName: String,
        chipName: String
    ) {
      val dataLayer = getDataLayer(
            event = Event.CLICK_HOMEPAGE,
            action = Action.CLICK_CHIP_NAME,
            label = "$channelId - $headerName - $chipName",
            trackerId = TrackerId.CLICK_CHIP_NAME
        ).apply {
            putString(KEY_PRODUCT_ID, "")
        }

        getTracker().sendEnhanceEcommerceEvent(Event.CLICK_HOMEPAGE, dataLayer)
    }

    fun trackProductCardImpression(
        position: Int,
        channelId: String,
        chipName: String,
        product: ProductCardCompactCarouselItemUiModel
    ) {
        val index = position.getTrackerPosition().toString()
        val productCard = product.productCardModel
        val productId = productCard.productId

        val pageName = product.pageName
        val headerName = product.headerName
        val recommendationType = product.recommendationType
        val itemList = "/tokonow - recomproduct - carousel - $recommendationType - $pageName - $headerName"

        val items = arrayListOf(
            productItemDataLayer(
                index = index,
                productId = productId,
                productName = productCard.name,
                price = productCard.price.filter { it.isDigit() }.toLongOrZero(),
                productCategory = product.categoryBreadcrumbs
            ).apply {
                putString(KEY_DIMENSION_40, itemList)
                putString(KEY_DIMENSION_84, channelId)
                putString(KEY_DIMENSION_90, "$pageName.$recommendationType")
            }
        )
        val dataLayer = getEcommerceDataLayer(
            event = EVENT_VIEW_ITEM_LIST,
            action = Action.IMPRESSION_PRODUCT,
            label = "$channelId - $headerName - $chipName",
            trackerId = TrackerId.IMPRESSION_PRODUCT,
            itemList = itemList,
            items = items,
            productId = productId
        )

        getTracker().sendEnhanceEcommerceEvent(EVENT_VIEW_ITEM_LIST, dataLayer)
    }

    fun trackProductCardClick(
        position: Int,
        channelId: String,
        chipName: String,
        product: ProductCardCompactCarouselItemUiModel
    ) {
        val index = position.getTrackerPosition().toString()
        val productCard = product.productCardModel
        val productId = productCard.productId

        val pageName = product.pageName
        val headerName = product.headerName
        val recommendationType = product.recommendationType
        val itemList = "/tokonow - recomproduct - carousel - $recommendationType - $pageName - $headerName"

        val items = arrayListOf(
            productItemDataLayer(
                index = index,
                productId = productId,
                productName = productCard.name,
                price = productCard.price.filter { it.isDigit() }.toLongOrZero(),
                productCategory = product.categoryBreadcrumbs
            ).apply {
                putString(KEY_DIMENSION_40, itemList)
                putString(KEY_DIMENSION_84, channelId)
                putString(KEY_DIMENSION_90, "$pageName.$recommendationType")
            }
        )
        val dataLayer = getEcommerceDataLayer(
            event = EVENT_SELECT_CONTENT,
            action = Action.CLICK_PRODUCT,
            label = "$channelId - $headerName - $chipName",
            trackerId = TrackerId.CLICK_PRODUCT,
            itemList = itemList,
            items = items,
            productId = productId
        )

        getTracker().sendEnhanceEcommerceEvent(EVENT_SELECT_CONTENT, dataLayer)
    }

    fun trackClickAddToCart(
        position: Int,
        channelId: String,
        chipName: String,
        cartId: String,
        quantity: Int,
        carousel: HomeProductCarouselChipsUiModel
    ) {
        val productList = carousel.carouselItemList
            .filterIsInstance<ProductCardCompactCarouselItemUiModel>()
        val product = productList[position]
        val index = position.getTrackerPosition().toString()
        val productCard = product.productCardModel
        val productId = productCard.productId

        val pageName = product.pageName
        val headerName = product.headerName
        val recommendationType = product.recommendationType
        val itemList = "/tokonow - recomproduct - carousel - $recommendationType - $pageName - $headerName"

        val items = arrayListOf(
            productItemDataLayer(
                index = index,
                productId = productId,
                productName = productCard.name,
                price = productCard.price.filter { it.isDigit() }.toLongOrZero(),
                productCategory = product.categoryBreadcrumbs
            ).apply {
                putString(KEY_DIMENSION_40, itemList)
                putString(KEY_DIMENSION_45, cartId)
                putString(KEY_DIMENSION_90, "$pageName.$recommendationType")
                putInt(KEY_QUANTITY, quantity)
                putString(KEY_SHOP_ID, product.shopId)
                putString(KEY_SHOP_NAME, product.shopName)
                putString(KEY_SHOP_TYPE, product.shopType)
                putString(KEY_CATEGORY_ID, "")
            }
        )
        val dataLayer = getEcommerceDataLayer(
            event = EVENT_ADD_TO_CART,
            action = Action.ADD_TO_CART,
            label = "$channelId - $headerName - $chipName",
            trackerId = TrackerId.ADD_TO_CART,
            itemList = itemList,
            items = items,
            productId = productId
        )

        getTracker().sendEnhanceEcommerceEvent(EVENT_ADD_TO_CART, dataLayer)
    }

    private fun getDataLayer(
        event: String,
        action: String,
        label: String = "",
        trackerId: String = ""
    ): Bundle {
        return Bundle().apply {
            putString(EVENT, event)
            putString(EVENT_ACTION, action)
            putString(EVENT_CATEGORY, EVENT_CATEGORY_TOKONOW_RECOM_HOMEPAGE)
            putString(EVENT_LABEL, label)

            if (trackerId.isNotEmpty()) {
                putString(KEY_TRACKER_ID, trackerId)
            }

            putString(KEY_BUSINESS_UNIT, BUSINESS_UNIT_HOME_AND_BROWSE)
            putString(KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
        }
    }

    private fun getEcommerceDataLayer(
        event: String,
        action: String,
        label: String = "",
        trackerId: String = "",
        itemList: String = "",
        items: ArrayList<Bundle>,
        productId: String = ""
    ): Bundle {
        return Bundle().apply {
            putString(EVENT, event)
            putString(EVENT_ACTION, action)
            putString(EVENT_CATEGORY, EVENT_CATEGORY_TOKONOW_RECOM_HOMEPAGE)
            putString(EVENT_LABEL, label)
            putString(KEY_TRACKER_ID, trackerId)
            putString(KEY_BUSINESS_UNIT, BUSINESS_UNIT_HOME_AND_BROWSE)
            putString(KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            putString(KEY_ITEM_LIST, itemList)
            putParcelableArrayList(KEY_ITEMS, items)
            if (productId.isNotBlank()) {
                putString(KEY_PRODUCT_ID, productId)
            }
            putString(KEY_USER_ID, userSession.userId)
        }
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
}
