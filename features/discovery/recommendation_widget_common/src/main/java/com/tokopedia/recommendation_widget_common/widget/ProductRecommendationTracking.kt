package com.tokopedia.recommendation_widget_common.widget

import com.tokopedia.recommendation_widget_common.extension.hasLabelGroupFulfillment
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

object ProductRecommendationTracking: BaseTrackerConst() {
    const val VALUE_IS_TOPADS = "- product topads"
    const val VALUE_NON_LOGIN = " - non login"
    const val EVENT_ACTION_IMPRESSION_PRODUCT_RECOMMENDATION = "impression - product recommendation%s"
    const val EVENT_ACTION_CLICK_PRODUCT_RECOMMENDATION = "click - product recommendation%s"
    const val EVENT_ACTION_CLICK_SEE_MORE_COMPARISON = "click - see more comparison%s"
    const val EVENT_LABEL_PRODUCT = "%s - %s"
    const val EVENT_LIST_PRODUCT = "/product - %s - rekomendasi untuk anda - %s%s - %s - %s"

    fun getImpressionProductTracking(
            recommendationItems: List<RecommendationItem>,
            androidPageName: String,
            headerTitle: String,
            chipsTitle: String = "",
            recomPageName: String,
            recommendationType: String,
            isTopads: Boolean,
            widgetType: String,
            productId: String = "",
            position: Int,
            isLoggedIn: Boolean,
            anchorProductId: String = ""
    ): HashMap<String, Any> {
        val trackingBuilder =
                BaseTrackerBuilder()
                        .constructBasicProductView(
                                event = Event.PRODUCT_VIEW,
                                eventCategory = androidPageName,
                                eventAction = String.format(
                                        EVENT_ACTION_IMPRESSION_PRODUCT_RECOMMENDATION,
                                        if (isLoggedIn) "" else VALUE_NON_LOGIN
                                ),
                                eventLabel = String.format(
                                        EVENT_LABEL_PRODUCT,
                                        headerTitle,
                                        chipsTitle
                                ),
                                list = buildRecommendationList(recomPageName, recommendationType, isTopads, widgetType, anchorProductId),
                                products = recommendationItems.map {
                                    mapRecommendationItemToProductTracking(it, position)
                                }
                        )
                        .appendBusinessUnit(BusinessUnit.DEFAULT)
                        .appendCurrentSite(CurrentSite.DEFAULT)
        return trackingBuilder.build() as HashMap<String, Any>
    }

    fun getClickProductTracking(
            recommendationItems: List<RecommendationItem>,
            androidPageName: String,
            headerTitle: String,
            chipsTitle: String,
            recomPageName: String,
            recommendationType: String,
            isTopads: Boolean,
            widgetType: String,
            productId: String,
            position: Int,
            isLoggedIn: Boolean,
            anchorProductId: String
    ): HashMap<String, Any> {
        val trackingBuilder =
                BaseTrackerBuilder()
                        .constructBasicProductClick(
                                event = Event.PRODUCT_CLICK,
                                eventCategory = androidPageName,
                                eventAction = String.format(
                                        EVENT_ACTION_CLICK_PRODUCT_RECOMMENDATION,
                                        if (isLoggedIn) "" else VALUE_NON_LOGIN
                                ),
                                eventLabel = String.format(
                                        EVENT_LABEL_PRODUCT,
                                        headerTitle,
                                        chipsTitle
                                ),
                                list = buildRecommendationList(recomPageName, recommendationType, isTopads, widgetType, anchorProductId),
                                products = recommendationItems.map {
                                    mapRecommendationItemToProductTracking(it, position)
                                }
                        )
                        .appendBusinessUnit(BusinessUnit.DEFAULT)
                        .appendCurrentSite(CurrentSite.DEFAULT)
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

    private fun mapRecommendationItemToProductTracking(it: RecommendationItem, position: Int): Product {
        return Product(
                name = it.name,
                id = it.productId.toString(),
                productPrice = it.priceInt.toString(),
                brand = Value.NONE_OTHER,
                category = it.categoryBreadcrumbs.toLowerCase(),
                variant = Value.NONE_OTHER,
                productPosition = position.toString(),
                isFreeOngkir = it.isFreeOngkirActive && !it.labelGroupList.hasLabelGroupFulfillment(),
                isFreeOngkirExtra = it.isFreeOngkirActive && it.labelGroupList.hasLabelGroupFulfillment(),
                headerName = it.header,
                recommendationType = it.recommendationType,
                shopId = it.shopId.toString(),
                pageName = it.pageName
        )
    }
}

