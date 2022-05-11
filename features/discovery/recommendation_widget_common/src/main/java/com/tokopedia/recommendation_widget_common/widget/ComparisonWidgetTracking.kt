package com.tokopedia.recommendation_widget_common.widget

import com.tokopedia.recommendation_widget_common.extension.hasLabelGroupFulfillment
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

/**
 * Created by dhaba
 */
object ComparisonWidgetTracking : BaseTrackerConst() {
    fun getImpressionProductTrackingComparisonWidget(
        eventAction: String? = null,
        androidPageName: String = "",
        userId: String = "",
        eventLabel: String? = null,
        headerTitle: String,
        chipsTitle: String = "",
        recommendationItem: RecommendationItem,
        position: Int,
    ) : HashMap<String, Any> {
        val isLogin = userId.isNotBlank()
        val trackingBuilder =
            BaseTrackerBuilder()
                .constructBasicProductView(
                    event = Event.PRODUCT_VIEW,
                    eventCategory = androidPageName,
                    eventAction = eventAction
                        ?: String.format(
                            ProductRecommendationTracking.EVENT_ACTION_IMPRESSION_PRODUCT_RECOMMENDATION,
                            if (isLogin) "" else ProductRecommendationTracking.VALUE_NON_LOGIN
                        ),
                    eventLabel = eventLabel
                        ?: String.format(
                            ProductRecommendationTracking.EVENT_LABEL_PRODUCT,
                            headerTitle,
                            chipsTitle
                        ),
                    list = "",
                    products = listOf(
                        mapRecommendationItemToProductTracking(
                            recommendationItem,
                            position
                        )
                    )
                )
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
        return trackingBuilder.build() as HashMap<String, Any>
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
            cartId = it.cartId
        )
    }
}