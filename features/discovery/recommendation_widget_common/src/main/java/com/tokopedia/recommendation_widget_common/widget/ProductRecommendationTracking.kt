package com.tokopedia.recommendation_widget_common.widget

import android.os.Bundle
import com.tokopedia.recommendation_widget_common.extension.hasLabelGroupFulfillment
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

object ProductRecommendationTracking: BaseTrackerConst() {
    const val SELECT_CONTENT = "select_content"
    const val PRODUCT_CLICK = Event.PRODUCT_CLICK
    const val EVENT_ATC = "addToCart"

    const val VALUE_IS_TOPADS = "- product topads"
    const val VALUE_NON_LOGIN = " - non login"
    const val VALUE_ID = " %s"

    const val EVENT_ACTION_IMPRESSION_PRODUCT_RECOMMENDATION = "impression - product recommendation%s"
    const val EVENT_ACTION_CLICK_PRODUCT_RECOMMENDATION = "click - product recommendation%s"
    const val EVENT_ACTION_CLICK_SEE_MORE_COMPARISON = "click - see more comparison%s"
    const val EVENT_ACTION_IMPRESSION_PRODUCT_TOKONOW = "impression product on tokonow%s product recommendation"
    const val EVENT_ACTION_CLICK_PRODUCT_TOKONOW = "click product on tokonow%s product recommendation"
    const val EVENT_ACTION_ATC_CLICK_PRODUCT_TOKONOW = "click add to cart on tokonow%s product recommendation"

    const val EVENT_LABEL_PRODUCT = "%s - %s"

    const val EVENT_LIST_PRODUCT = "/product - %s - rekomendasi untuk anda - %s%s - %s - %s"
    const val EVENT_TOKONOW_LIST_PRODUCT = "/%s - tokonow - rekomendasi untuk anda - %s"

    const val EVENT_CATEGORY_TOKONOW = "tokonow %s"

    fun getImpressionProductTracking(
            recommendationItem: RecommendationItem,
            androidPageName: String,
            headerTitle: String,
            chipsTitle: String = "",
            position: Int,
            isLoggedIn: Boolean,
            anchorProductId: String = "",
            isTokonow: Boolean = false,
            listPage: String = "",
            pageId: String = "",
            eventLabel: String? = null,
            userId: String = "",
            eventAction: String? = null,
            listValue: String? = null
    ): HashMap<String, Any> {
        val trackingBuilder =
                BaseTrackerBuilder()
                        .constructBasicProductView(
                                event = Event.PRODUCT_VIEW,
                                eventCategory = if (isTokonow) String.format(
                                    EVENT_CATEGORY_TOKONOW, androidPageName
                                ) else androidPageName,
                                eventAction = eventAction
                                    ?: if (isTokonow) String.format(
                                        EVENT_ACTION_IMPRESSION_PRODUCT_TOKONOW, if (pageId.isNotEmpty()) String.format(VALUE_ID, pageId) else ""
                                    ) else String.format(
                                        EVENT_ACTION_IMPRESSION_PRODUCT_RECOMMENDATION,
                                        if (isLoggedIn) "" else VALUE_NON_LOGIN
                                    ),
                                eventLabel = eventLabel
                                    ?: String.format(
                                        EVENT_LABEL_PRODUCT,
                                        headerTitle,
                                        chipsTitle
                                    ),
                                list = listValue?:
                                if (isTokonow)
                                    buildTokonowRecommendationList(
                                        pageList = listPage,
                                        pageRecommendationType = recommendationItem.recommendationType
                                    )
                                else
                                    buildRecommendationList(
                                        recomPageName = recommendationItem.pageName,
                                        recommendationType = recommendationItem.recommendationType,
                                        isTopads = recommendationItem.isTopAds,
                                        widgetType = recommendationItem.type,
                                        anchorProductId = anchorProductId
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
            androidPageName: String,
            headerTitle: String,
            chipsTitle: String = "",
            position: Int,
            isLoggedIn: Boolean,
            anchorProductId: String = "",
            isTokonow: Boolean = false,
            listPage: String = "",
            pageId: String = "",
            eventLabel: String? = null,
            userId: String = "",
            eventAction: String? = null,
            listValue: String? = null
    ): HashMap<String, Any> {
        val trackingBuilder =
                BaseTrackerBuilder()
                        .constructBasicProductClick(
                                event = PRODUCT_CLICK,
                                eventCategory = if (isTokonow) String.format(
                                    EVENT_CATEGORY_TOKONOW, androidPageName
                                ) else androidPageName,
                                eventAction = eventAction?:
                                    if (isTokonow) String.format(
                                        EVENT_ACTION_CLICK_PRODUCT_TOKONOW, if (pageId.isNotEmpty()) String.format(VALUE_ID, pageId) else ""
                                    ) else String.format(
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
                                if (isTokonow)
                                    buildTokonowRecommendationList(
                                        pageList = listPage,
                                        pageRecommendationType = recommendationItem.recommendationType
                                    )
                                else
                                    buildRecommendationList(
                                        recomPageName = recommendationItem.pageName,
                                        recommendationType = recommendationItem.recommendationType,
                                        isTopads = recommendationItem.isTopAds,
                                        widgetType = recommendationItem.type,
                                        anchorProductId = anchorProductId
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
    ): HashMap<String, Any> {
        val trackerBuilder = BaseTrackerBuilder()
                .constructBasicGeneralClick(
                        event = eventClick,
                        eventCategory = eventCategory,
                        eventAction = String.format(
                                EVENT_ACTION_CLICK_SEE_MORE_COMPARISON,
                                if (isLoggedIn) "" else VALUE_NON_LOGIN
                        ),
                        eventLabel = String.format(
                                EVENT_LABEL_PRODUCT,
                                recomTitle,
                                pageName
                        )
                )
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
        return trackerBuilder.build() as HashMap<String, Any>
    }

    fun getAddToCartClickProductTracking(
        recommendationItem: RecommendationItem,
        androidPageName: String,
        headerTitle: String,
        chipsTitle: String = "",
        position: Int,
        isLoggedIn: Boolean,
        anchorProductId: String = "",
        isTokonow: Boolean = false,
        listPage: String = "",
        pageId: String = "",
        eventLabel: String? = null,
        userId: String = "",
        quantity: Int = 0,
        cartId: String = ""
    ): HashMap<String, Any> {
        recommendationItem.quantity = quantity
        recommendationItem.cartId = cartId
        val trackingBuilder =
            BaseTrackerBuilder()
                .constructBasicProductAtcClick(
                    event = EVENT_ATC,
                    eventCategory = if (isTokonow) String.format(
                        EVENT_CATEGORY_TOKONOW, androidPageName
                    ) else androidPageName,
                    eventAction = if (isTokonow) String.format(
                        EVENT_ACTION_ATC_CLICK_PRODUCT_TOKONOW, if (pageId.isNotEmpty()) String.format(VALUE_ID, pageId) else ""
                    ) else "",
                    eventLabel = eventLabel
                        ?: String.format(
                            EVENT_LABEL_PRODUCT,
                            headerTitle,
                            chipsTitle
                        ),
                    list = if (isTokonow)
                        buildTokonowRecommendationList(
                            pageList = listPage,
                            pageRecommendationType = recommendationItem.recommendationType
                        )
                    else
                        buildRecommendationList(
                            recomPageName = recommendationItem.pageName,
                            recommendationType = recommendationItem.recommendationType,
                            isTopads = recommendationItem.isTopAds,
                            widgetType = recommendationItem.type,
                            anchorProductId = anchorProductId
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

    private fun buildRecommendationList(recomPageName: String, recommendationType: String, isTopads: Boolean, widgetType: String, anchorProductId: String): String {
        return String.format(
                EVENT_LIST_PRODUCT,
                recomPageName,
                recommendationType,
                if (isTopads) VALUE_IS_TOPADS else "",
                widgetType,
                anchorProductId
        )
    }

    private fun buildTokonowRecommendationList(
        pageList: String,
        pageRecommendationType: String
    ): String {
        return String.format(
            EVENT_TOKONOW_LIST_PRODUCT,
            pageList,
            pageRecommendationType
        )
    }

    private fun mapRecommendationItemToProductTracking(it: RecommendationItem, position: Int): Product {
        return Product(
                name = it.name,
                id = it.productId.toString(),
                productPrice = it.priceInt.toString(),
                brand = Value.NONE_OTHER,
                category = it.categoryBreadcrumbs.toLowerCase(),
                variant = Value.NONE_OTHER,
                productPosition = (position+1).toString(),
                isFreeOngkir = it.isFreeOngkirActive && !it.labelGroupList.hasLabelGroupFulfillment(),
                isFreeOngkirExtra = it.isFreeOngkirActive && it.labelGroupList.hasLabelGroupFulfillment(),
                headerName = it.header,
                recommendationType = it.recommendationType,
                shopId = it.shopId.toString(),
                pageName = it.pageName,
                shopName = it.shopName,
                shopType = it.shopType,
                quantity = if (it.quantity > 0) it.quantity.toString() else "",
                cartId = it.cartId
        )
    }
}

