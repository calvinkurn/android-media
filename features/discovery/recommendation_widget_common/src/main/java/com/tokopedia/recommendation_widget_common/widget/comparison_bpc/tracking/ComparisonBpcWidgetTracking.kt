package com.tokopedia.recommendation_widget_common.widget.comparison_bpc.tracking

import com.tokopedia.recommendation_widget_common.extension.hasLabelGroupFulfillment
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst
import java.util.*
import kotlin.collections.HashMap

/**
 * Created by dhaba
 */
object ComparisonBpcWidgetTracking : BaseTrackerConst() {
//    private const val DEFAULT_VALUE = ""
//    private const val DEFAULT_QUANTITY = 0
//    const val PRODUCT_CLICK = Event.PRODUCT_CLICK
//    const val EVENT_ATC = "addToCart"
//
//    const val VALUE_IS_TOPADS = "- product topads"
//    const val VALUE_NON_LOGIN = " - non login"
//
//    const val EVENT_ACTION_IMPRESSION_PRODUCT_RECOMMENDATION =
//        "impression comparison widget%s"
//    const val EVENT_ACTION_CLICK_PRODUCT_RECOMMENDATION = "click - product recommendation%s"
//    const val EVENT_ACTION_CLICK_SEE_MORE_COMPARISON = "click - see more comparison%s"
//
//    const val EVENT_LABEL_CLICK_SEE_ALL = "%s - %s - %s"
//
//    const val EVENT_LIST_PRODUCT = "/product - %s%s - rekomendasi untuk anda - %s%s - %s - %s"
//    const val EVENT_TOKONOW_LIST_PRODUCT = "/%s - tokonow - rekomendasi untuk anda - %s"
//
//    const val COMPARISON_WIDGET = "comparison widget"
//
//    fun getImpressionProductTrackingComparisonWidget(
//        androidPageName: String = "",
//        userId: String = "",
//        eventLabel: String? = null,
//        headerTitle: String,
//        chipsTitle: String = "",
//        recommendationItem: RecommendationItem,
//        position: Int,
//    ) : HashMap<String, Any> {
//        val isLogin = userId.isNotBlank()
//        val trackingBuilder =
//            BaseTrackerBuilder()
//                .constructBasicProductView(
//                    event = Event.PRODUCT_VIEW,
//                    eventCategory = androidPageName,
//                    eventAction = String.format(
//                        EVENT_ACTION_IMPRESSION_PRODUCT_RECOMMENDATION,
//                        if (isLogin) DEFAULT_VALUE else VALUE_NON_LOGIN
//                    ),
//                    eventLabel = recommendationItem.productId.toString(),
//                    list = DEFAULT_VALUE,
//                    products = listOf(
//                        mapRecommendationItemToProductTracking(
//                            recommendationItem,
//                            position
//                        )
//                    )
//                )
//                .appendBusinessUnit(BusinessUnit.DEFAULT)
//                .appendCurrentSite(CurrentSite.DEFAULT)
//                .appendUserId(userId)
//        return trackingBuilder.build() as HashMap<String, Any>
//    }
//
//    private fun mapRecommendationItemToProductTracking(
//        it: RecommendationItem,
//        position: Int
//    ): Product {
//        val productPosition = (position + 1).toString()
//        return Product(
//            name = it.name,
//            id = it.productId.toString(),
//            productPrice = it.priceInt.toString(),
//            brand = Value.NONE_OTHER,
//            category = it.categoryBreadcrumbs.lowercase(Locale.getDefault()),
//            variant = Value.NONE_OTHER,
//            productPosition = productPosition,
//            isFreeOngkir = it.isFreeOngkirActive && !it.labelGroupList.hasLabelGroupFulfillment(),
//            isFreeOngkirExtra = it.isFreeOngkirActive && it.labelGroupList.hasLabelGroupFulfillment(),
//            headerName = it.header,
//            recommendationType = it.recommendationType,
//            shopId = it.shopId.toString(),
//            pageName = it.pageName,
//            shopName = it.shopName,
//            shopType = it.shopType,
//            quantity = if (it.quantity > DEFAULT_QUANTITY) it.quantity.toString() else DEFAULT_VALUE,
//            cartId = it.cartId
//        )
//    }
}
