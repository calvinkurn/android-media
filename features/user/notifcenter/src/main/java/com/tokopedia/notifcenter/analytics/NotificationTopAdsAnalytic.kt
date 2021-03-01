package com.tokopedia.notifcenter.analytics

import com.tokopedia.recommendation_widget_common.extension.hasLabelGroupFulfillment
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.ContextAnalytics
import com.tokopedia.trackingoptimizer.TrackingQueue
import java.util.*
import javax.inject.Inject
class NotificationTopAdsAnalytic @Inject constructor() {

    private val dataLayerList: ArrayList<Any> = arrayListOf()

    fun eventInboxTopAdsProductView(trackingQueue: TrackingQueue?) {
        if (dataLayerList.isNotEmpty()) {
            val map = mutableMapOf(
                    "event" to "productView",
                    "eventCategory" to "inbox page",
                    "eventAction" to "impression on product recommendation",
                    "eventLabel" to "",
                    "ecommerce" to mapOf(
                            "currencyCode" to "IDR",
                            "impressions" to listOf(
                                    dataLayerList.toTypedArray()
                            )
                    )
            )
            trackingQueue?.putEETracking(map as HashMap<String, Any>)
            clearDataLayerList()
        }
    }

    fun addInboxTopAdsProductViewImpressions(
            recommendationItem: RecommendationItem, position: Int, isTopAds: Boolean
    ) {
        val priceCurrency = recommendationItem.price.replace("[^0-9]".toRegex(), "")
        dataLayerList.add(
                mapOf(
                        "name" to recommendationItem.name,
                        "id" to recommendationItem.productId,
                        "price" to priceCurrency,
                        "brand" to "none/other",
                        "variant" to "none/other",
                        "category" to recommendationItem.departmentId,
                        "list" to getListAttribute(recommendationItem, isTopAds),
                        "position" to position.toString(),
                        DATA_DIMENSION_83 to getDimension83Attribute(recommendationItem))
        )
    }

    fun eventInboxTopAdsProductClick(
            recommendationItem: RecommendationItem, position: Int, isTopAds: Boolean
    ) {
        val tracker: ContextAnalytics = TrackApp.getInstance().gtm
        val priceCurrency = recommendationItem.price.replace("[^0-9]".toRegex(), "")
        val map = mapOf(
                "event" to "productClick",
                "eventCategory" to "inbox page",
                "eventAction" to "click on product recommendation",
                "eventLabel" to "",
                "ecommerce" to mapOf(
                        "click" to mapOf(
                                "actionField" to mapOf(
                                        "list" to getListAttribute(recommendationItem, isTopAds)
                                ),
                                "products" to listOf(
                                        mapOf(
                                                "name" to recommendationItem.name,
                                                "id" to recommendationItem.productId,
                                                "price" to priceCurrency,
                                                "brand" to "none/other",
                                                "category" to recommendationItem.categoryBreadcrumbs,
                                                "varian" to "none/other",
                                                "position" to position.toString(),
                                                DATA_DIMENSION_83 to getDimension83Attribute(recommendationItem))
                                )
                        )
                )
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    fun eventClickRecommendationWishlist(isAdd: Boolean) {
        val tracker: ContextAnalytics = TrackApp.getInstance().gtm
        val map = mapOf(
                "event" to "clickInbox",
                "eventCategory" to "inbox page",
                "eventAction" to getWishListEventAction(isAdd),
                "eventLabel" to ""
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    private fun getWishListEventAction(isAdd: Boolean) =
            String.format(
                    "click %s wishlist on product recommendation",
                    if (isAdd) "add" else "remove"
            )

    private fun clearDataLayerList() {
        dataLayerList.clear()
    }

    private fun getDimension83Attribute(recommendationItem: RecommendationItem): String {
        return if (recommendationItem.isFreeOngkirActive && recommendationItem.labelGroupList.hasLabelGroupFulfillment()) {
            VALUE_BEBAS_ONGKIR_EXTRA
        } else if (recommendationItem.isFreeOngkirActive) {
            VALUE_BEBAS_ONGKIR
        } else {
            VALUE_NONE_OTHER
        }
    }

    private fun getListAttribute(
            recommendationItem: RecommendationItem,
            isTopAds: Boolean
    ): String {
        val isTopAdsString = if (isTopAds) " - product topads" else ""
        return "/inbox - rekomendasi untuk anda - " +
                recommendationItem.recommendationType + isTopAdsString
    }

    companion object {
        private const val DATA_DIMENSION_83 = "dimension83"
        private const val VALUE_BEBAS_ONGKIR = "bebas ongkir"
        private const val VALUE_BEBAS_ONGKIR_EXTRA = "bebas ongkir extra"
        private const val VALUE_NONE_OTHER = "none / other"
    }
}

