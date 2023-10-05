package com.tokopedia.inbox.universalinbox.analytics

import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.ACTION_FIELD
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.BRAND
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.CATEGORY
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.CLICK
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.CLICK_INBOX
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.CLICK_ON_PRODUCT_RECOMMENDATION
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.CURRENCY_CODE
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.CURRENCY_CODE_IDR
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.DATA_DIMENSION_83
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.EVENT
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.EVENT_ACTION
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.EVENT_CATEGORY
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.EVENT_ECOMMERCE
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.EVENT_LABEL
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.ID
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.IMPRESSIONS
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.IMPRESSION_ON_PRODUCT_RECOMMENDATION
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.INBOX_PAGE
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.LIST
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.NAME
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.POSITION
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.PRICE
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.PRODUCTS
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.PRODUCT_CLICK
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.PRODUCT_VIEW
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.REGEX_NUMBER
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.VALUE_BEBAS_ONGKIR
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.VALUE_BEBAS_ONGKIR_EXTRA
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.VALUE_NONE_OTHER
import com.tokopedia.inbox.universalinbox.analytics.UniversalInboxAnalyticsConstants.VARIANT
import com.tokopedia.recommendation_widget_common.extension.hasLabelGroupFulfillment
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.ContextAnalytics
import com.tokopedia.trackingoptimizer.TrackingQueue
import java.util.*
import javax.inject.Inject
class UniversalInboxTopAdsAnalytic @Inject constructor() {

    private val dataLayerList: ArrayList<Any> = arrayListOf()

    fun eventInboxTopAdsProductView(trackingQueue: TrackingQueue?) {
        if (dataLayerList.isNotEmpty()) {
            val map = mutableMapOf(
                EVENT to PRODUCT_VIEW,
                EVENT_CATEGORY to INBOX_PAGE,
                EVENT_ACTION to IMPRESSION_ON_PRODUCT_RECOMMENDATION,
                EVENT_LABEL to "",
                EVENT_ECOMMERCE to mapOf(
                    CURRENCY_CODE to CURRENCY_CODE_IDR,
                    IMPRESSIONS to listOf(
                        dataLayerList.toTypedArray()
                    )
                )
            )
            trackingQueue?.putEETracking(map as HashMap<String, Any>)
            clearDataLayerList()
        }
    }

    fun addInboxTopAdsProductViewImpressions(
        recommendationItem: RecommendationItem,
        position: Int,
        isTopAds: Boolean
    ) {
        val priceCurrency = recommendationItem.price.replace(REGEX_NUMBER.toRegex(), "")
        dataLayerList.add(
            mapOf(
                NAME to recommendationItem.name,
                ID to recommendationItem.productId,
                PRICE to priceCurrency,
                BRAND to VALUE_NONE_OTHER,
                VARIANT to VALUE_NONE_OTHER,
                CATEGORY to recommendationItem.departmentId,
                LIST to getListAttribute(recommendationItem, isTopAds),
                POSITION to position.toString(),
                DATA_DIMENSION_83 to getDimension83Attribute(recommendationItem)
            )
        )
    }

    fun eventInboxTopAdsProductClick(
        recommendationItem: RecommendationItem,
        position: Int,
        isTopAds: Boolean
    ) {
        val tracker: ContextAnalytics = TrackApp.getInstance().gtm
        val priceCurrency = recommendationItem.price.replace(REGEX_NUMBER.toRegex(), "")
        val map = mapOf(
            EVENT to PRODUCT_CLICK,
            EVENT_CATEGORY to INBOX_PAGE,
            EVENT_ACTION to CLICK_ON_PRODUCT_RECOMMENDATION,
            EVENT_LABEL to "",
            EVENT_ECOMMERCE to mapOf(
                CLICK to mapOf(
                    ACTION_FIELD to mapOf(
                        LIST to getListAttribute(recommendationItem, isTopAds)
                    ),
                    PRODUCTS to listOf(
                        mapOf(
                            NAME to recommendationItem.name,
                            ID to recommendationItem.productId,
                            PRICE to priceCurrency,
                            BRAND to VALUE_NONE_OTHER,
                            CATEGORY to recommendationItem.categoryBreadcrumbs,
                            VARIANT to VALUE_NONE_OTHER,
                            POSITION to position.toString(),
                            DATA_DIMENSION_83 to getDimension83Attribute(recommendationItem)
                        )
                    )
                )
            )
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    fun eventClickRecommendationWishlist(isAdd: Boolean) {
        val tracker: ContextAnalytics = TrackApp.getInstance().gtm
        val map = mapOf(
            EVENT to CLICK_INBOX,
            EVENT_CATEGORY to INBOX_PAGE,
            EVENT_ACTION to getWishListEventAction(isAdd),
            EVENT_LABEL to ""
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    private fun getWishListEventAction(isAdd: Boolean) =
        String.format(
            Locale.getDefault(),
            "click %s wishlist on product recommendation",
            if (isAdd) "add" else "remove"
        )

    private fun clearDataLayerList() {
        dataLayerList.clear()
    }

    private fun getDimension83Attribute(recommendationItem: RecommendationItem): String {
        return if (recommendationItem.isFreeOngkirActive &&
            recommendationItem.labelGroupList.hasLabelGroupFulfillment()
        ) {
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
}
