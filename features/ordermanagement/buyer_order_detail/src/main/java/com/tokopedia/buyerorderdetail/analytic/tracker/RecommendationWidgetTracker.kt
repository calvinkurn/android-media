package com.tokopedia.buyerorderdetail.analytic.tracker

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst


object RecommendationWidgetTracker : BaseTrackerConst() {

    private const val EVENT_CATEGORY = "topads PG order detail recom widget"
    private const val IMPRESSION_PRODUCT = "impression product"
    private const val CLICK_PRODUCT = "click product"
    private const val EVENT_LABEL = "order status"

    fun getImpressionTracker(
        recommendationItem: RecommendationItem,
        userId: String
    ) =
        BaseTrackerBuilder()
            .constructBasicProductView(
                event = Event.PRODUCT_VIEW,
                eventCategory = EVENT_CATEGORY,
                eventAction = IMPRESSION_PRODUCT,
                eventLabel = EVENT_LABEL,
                list = "",
                buildCustomList = null,
                products = listOf(
                    mapToProductTracking(
                        recommendationItem
                    )
                )
            )
            .appendBusinessUnit(BusinessUnit.DEFAULT)
            .appendCurrentSite(CurrentSite.DEFAULT)
            .appendUserId(userId)
            .build()

    fun sendClickTracker(
        recommendationItem: RecommendationItem,
        userId: String,
    ) {
        val tracker = BaseTrackerBuilder()
            .constructBasicProductClick(
                event = Event.PRODUCT_CLICK,
                eventCategory = EVENT_CATEGORY,
                eventAction = CLICK_PRODUCT,
                eventLabel = EVENT_LABEL,
                list = "",
                products = listOf(
                    mapToProductTracking(
                        recommendationItem
                    )
                ),
                buildCustomList = null
            )
            .appendBusinessUnit(BusinessUnit.DEFAULT)
            .appendCurrentSite(CurrentSite.DEFAULT)
            .appendUserId(userId)
            .build()
        getTracker().sendEnhanceEcommerceEvent(tracker)
    }

    private fun mapToProductTracking(
        recommendationItem: RecommendationItem,
    ): Product {
        return Product(
                id = recommendationItem.productId.toString(),
                name = recommendationItem.name,
                productPrice = recommendationItem.priceInt.toString(),
                productPosition = recommendationItem.position.toString(),
                isFreeOngkir = false,
                category = recommendationItem.categoryBreadcrumbs,
                variant = "None / other",
                brand = "None / other",
                isTopAds = recommendationItem.isTopAds
        )

    }
}
