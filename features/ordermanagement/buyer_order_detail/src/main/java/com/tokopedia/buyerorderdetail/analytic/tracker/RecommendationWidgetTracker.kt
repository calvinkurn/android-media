package com.tokopedia.buyerorderdetail.analytic.tracker

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst


object RecommendationWidgetTracker : BaseTrackerConst() {

    private const val EVENT_CATEGORY = "topads PG order detail recom widget"
    private const val IMPRESSION_PRODUCT = "impression product"
    private const val CLICK_PRODUCT = "click product"

    fun getImpressionTracker(
        recommendationItem: RecommendationItem,
        userId: String
    ) =
        BaseTrackerBuilder()
            .constructBasicProductView(
                event = Event.PRODUCT_VIEW,
                eventCategory = EVENT_CATEGORY,
                eventAction = IMPRESSION_PRODUCT,
                eventLabel = Label.NONE,
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


//            {"event":"productClick"
//                ,"eventAction":"click product",
//                "eventCategory":"topads PG order detail recom widget",
//                "eventLabel":"order status",
//                "businessUnit":"{businessUnit}",
//                "currentSite":"{currentSite}",
//                "ecommerce":{"click":
//                    {"actionField":{"list":"{list}"},
//                        "products":[
//                        {"brand":"{brand}",
//                            "category":"{category}",
//                            "id":"{id}",
//                            "name":"{name}",
//                            "position":"{position}",
//                            "price":"{price}",
//                            "variant":"{variant}"}]}},
//                "userId":"{userId}"}

    fun sendClickTracker(
        recommendationItem: RecommendationItem,
        userId: String,
    ) {
        val tracker = BaseTrackerBuilder()
            .constructBasicProductClick(
                event = Event.PRODUCT_CLICK,
                eventCategory = EVENT_CATEGORY,
                eventAction = CLICK_PRODUCT,
                eventLabel = "",
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
            variant = "",
            brand = ""
        )

    }
}
