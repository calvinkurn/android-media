package com.tokopedia.recommendation_widget_common.widget

import android.os.Bundle
import com.tokopedia.recommendation_widget_common.extension.hasLabelGroupFulfillment
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

object ProductRecommendationTracking : BaseTrackerConst() {
    const val PRODUCT_CLICK = Event.PRODUCT_CLICK
    const val EVENT_ATC = "addToCart"

    const val VALUE_IS_TOPADS = "- product topads"
    const val VALUE_NON_LOGIN = " - non login"

    const val EVENT_ACTION_IMPRESSION_PRODUCT_RECOMMENDATION =
        "impression - product recommendation%s"
    const val EVENT_ACTION_CLICK_PRODUCT_RECOMMENDATION = "click - product recommendation%s"
    const val EVENT_ACTION_CLICK_SEE_MORE_COMPARISON = "click - see more comparison%s"

    const val EVENT_LABEL_PRODUCT = "%s - %s"
    const val EVENT_LABEL_CLICK_SEE_ALL = "%s - %s - %s"

    const val EVENT_LIST_PRODUCT = "/product - %s%s - rekomendasi untuk anda - %s%s - %s - %s"
    const val EVENT_TOKONOW_LIST_PRODUCT = "/%s - tokonow - rekomendasi untuk anda - %s"

    const val COMPARISON_WIDGET = "comparison widget"

    fun getImpressionProductTracking(
        recommendationItem: RecommendationItem,
        //used for construct Event Category
        androidPageName: String = "",
        headerTitle: String,
        chipsTitle: String = "",
        position: Int,
        isLoggedIn: Boolean,
        anchorProductId: String = "",
        eventLabel: String? = null,
        userId: String = "",
        eventAction: String? = null,
        listValue: String? = null,
        eventCategory: String? = null
    ): HashMap<String, Any> {
        val trackingBuilder =
            BaseTrackerBuilder()
                .constructBasicProductView(
                    event = Event.PRODUCT_VIEW,
                    eventCategory = eventCategory?: androidPageName,
                    eventAction = eventAction
                        ?: String.format(
                            EVENT_ACTION_IMPRESSION_PRODUCT_RECOMMENDATION,
                            if (isLoggedIn) "" else VALUE_NON_LOGIN
                        ),
                    eventLabel = eventLabel
                        ?: String.format(
                            EVENT_LABEL_PRODUCT,
                            headerTitle,
                            chipsTitle
                        ),
                    list = listValue ?: buildRecommendationList(
                        recomPageName = recommendationItem.pageName,
                        recommendationType = recommendationItem.recommendationType,
                        isTopads = recommendationItem.isTopAds,
                        widgetType = recommendationItem.type,
                        anchorProductId = anchorProductId,
                        isLoggedIn = isLoggedIn
                    ),
                    products = listOf(
                        mapRecommendationItemToProductTracking(recommendationItem, position)
                    )
                )
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
        return trackingBuilder.build() as HashMap<String, Any>
    }

    fun getClickProductTracking(
        recommendationItem: RecommendationItem,
        androidPageName: String = "",
        headerTitle: String,
        chipsTitle: String = "",
        position: Int,
        isLoggedIn: Boolean,
        anchorProductId: String = "",
        eventLabel: String? = null,
        userId: String = "",
        eventAction: String? = null,
        listValue: String? = null,
        eventCategory: String? = null,
        widgetType: String = ""
    ): HashMap<String, Any> {
        val trackingBuilder =
            BaseTrackerBuilder()
                .constructBasicProductClick(
                    event = PRODUCT_CLICK,
                    eventCategory = eventCategory?: androidPageName,
                    eventAction = eventAction ?:  String.format(
                        EVENT_ACTION_CLICK_PRODUCT_RECOMMENDATION,
                        if (isLoggedIn) "" else VALUE_NON_LOGIN
                    ),
                    eventLabel = eventLabel
                        ?: String.format(
                            EVENT_LABEL_PRODUCT,
                            headerTitle,
                            chipsTitle
                        ),
                    list = listValue ?: buildRecommendationList(
                        recomPageName = recommendationItem.pageName,
                        recommendationType = recommendationItem.recommendationType,
                        isTopads = recommendationItem.isTopAds,
                        widgetType = widgetType.ifBlank { recommendationItem.type },
                        anchorProductId = anchorProductId,
                        isLoggedIn = isLoggedIn
                    ),
                    products = listOf(
                        mapRecommendationItemToProductTracking(recommendationItem, position)
                    )
                )
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
        return trackingBuilder.build() as HashMap<String, Any>
    }

    fun getClickSpecDetailTracking(
        eventClick: String,
        eventCategory: String,
        isLoggedIn: Boolean,
        recomTitle: String,
        pageName: String,
        userId: String
    ): Pair<Bundle, String> {
        val bundle = Bundle()
        val eventAction = String.format(
            EVENT_ACTION_CLICK_SEE_MORE_COMPARISON,
            if (isLoggedIn) "" else VALUE_NON_LOGIN
        )
        val eventLabel = String.format(
            EVENT_LABEL_CLICK_SEE_ALL,
            recomTitle,
            pageName,
            COMPARISON_WIDGET
        )
        bundle.putString(Event.KEY, eventClick)
        bundle.putString(Category.KEY, eventCategory)
        bundle.putString(Action.KEY, eventAction)
        bundle.putString(Label.KEY, eventLabel)
        bundle.putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
        bundle.putString(CurrentSite.KEY, CurrentSite.DEFAULT)
        bundle.putString(UserId.KEY, userId)
        return Pair(bundle, eventClick)
    }

    fun getAddToCartClickProductTracking(
        recommendationItem: RecommendationItem,
        androidPageName: String = "",
        headerTitle: String,
        chipsTitle: String = "",
        position: Int,
        anchorProductId: String = "",
        isLoggedIn: Boolean,
        eventLabel: String? = null,
        userId: String = "",
        quantity: Int = 0,
        cartId: String = "",
        listValue: String? = null,
        eventCategory: String? = null,
        eventAction: String? = null
        ): HashMap<String, Any> {
        recommendationItem.quantity = quantity
        recommendationItem.cartId = cartId
        val trackingBuilder =
            BaseTrackerBuilder()
                .constructBasicProductAtcClick(
                    event = EVENT_ATC,
                    eventCategory = eventCategory?: androidPageName,
                    eventAction = eventAction ?:  String.format(
                        EVENT_ACTION_CLICK_PRODUCT_RECOMMENDATION,
                        if (isLoggedIn) "" else VALUE_NON_LOGIN
                    ),
                    eventLabel = eventLabel
                        ?: String.format(
                            EVENT_LABEL_PRODUCT,
                            headerTitle,
                            chipsTitle
                        ),
                    list = listValue?:
                    buildRecommendationList(
                        recomPageName = recommendationItem.pageName,
                        recommendationType = recommendationItem.recommendationType,
                        isTopads = recommendationItem.isTopAds,
                        widgetType = recommendationItem.type,
                        anchorProductId = anchorProductId,
                        isLoggedIn = isLoggedIn
                    ),
                    products = listOf(
                        mapRecommendationItemToProductTracking(recommendationItem, position)
                    )
                )
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
        return trackingBuilder.build() as HashMap<String, Any>
    }

    private fun buildRecommendationList(
        recomPageName: String,
        recommendationType: String,
        isTopads: Boolean,
        widgetType: String,
        anchorProductId: String,
        isLoggedIn: Boolean
    ): String {
        return String.format(
            EVENT_LIST_PRODUCT,
            recomPageName,
            if (widgetType == COMPARISON_WIDGET && !isLoggedIn) VALUE_NON_LOGIN else "",
            recommendationType,
            if (isTopads) VALUE_IS_TOPADS else "",
            widgetType,
            anchorProductId
        )
    }

    fun buildTokonowRecommendationList(
        pageList: String,
        pageRecommendationType: String
    ): String {
        return String.format(
            EVENT_TOKONOW_LIST_PRODUCT,
            pageList,
            pageRecommendationType
        )
    }

    private fun mapRecommendationItemToProductTracking(
        it: RecommendationItem,
        position: Int
    ): Product {
        return Product(
            name = it.name,
            id = it.productId.toString(),
            productPrice = it.priceInt.toString(),
            brand = Value.NONE_OTHER,
            category = it.categoryBreadcrumbs.toLowerCase(),
            variant = Value.NONE_OTHER,
            productPosition = (position + 1).toString(),
            isFreeOngkir = it.isFreeOngkirActive && !it.labelGroupList.hasLabelGroupFulfillment(),
            isFreeOngkirExtra = it.isFreeOngkirActive && it.labelGroupList.hasLabelGroupFulfillment(),
            headerName = it.header,
            recommendationType = it.recommendationType,
            shopId = it.shopId.toString(),
            pageName = it.pageName,
            shopName = it.shopName,
            shopType = it.shopType,
            quantity = if (it.quantity > 0) it.quantity.toString() else "",
            cartId = it.cartId,
            warehouseId = it.warehouseId.toString()
        )
    }
}

