package com.tokopedia.search.result.product.broadmatch

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.discovery.common.analytics.SearchComponentTracking
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingRollence
import com.tokopedia.iris.Iris
import com.tokopedia.remoteconfig.RollenceKey.SEARCH_BROAD_MATCH_TRACKER_UNIFICATION
import com.tokopedia.search.analytics.SearchEventTracking
import com.tokopedia.search.analytics.SearchEventTracking.Companion.BUSINESS_UNIT
import com.tokopedia.search.analytics.SearchEventTracking.Companion.CURRENT_SITE
import com.tokopedia.search.analytics.SearchTrackingConstant.ECOMMERCE
import com.tokopedia.search.analytics.SearchTrackingConstant.EVENT
import com.tokopedia.search.analytics.SearchTrackingConstant.EVENT_ACTION
import com.tokopedia.search.analytics.SearchTrackingConstant.EVENT_CATEGORY
import com.tokopedia.search.analytics.SearchTrackingConstant.EVENT_LABEL
import com.tokopedia.search.analytics.SearchTrackingConstant.PAGE_SOURCE
import com.tokopedia.search.analytics.SearchTrackingConstant.USER_ID
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue
import java.util.Locale

object BroadMatchTracking {

    private const val BROADMATCH_LIST_NAME = "/search - broad match - %s - component:%s"
    private const val IMPRESSION_BROAD_MATCH = "impression - broad match"
    private const val CLICK_BROAD_MATCH_LIHAT_SEMUA = "click - broad match lihat semua"

    @Suppress("LongParameterList")
    @JvmStatic
    fun trackEventClickBroadMatchItem(
        keyword: String,
        alternativeKeyword: String,
        userId: String,
        isOrganicAds: Boolean,
        componentId: String,
        broadMatchItems: ArrayList<Any>,
    ) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            DataLayer.mapOf(
                EVENT, SearchEventTracking.Event.PRODUCT_CLICK,
                EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                EVENT_ACTION, SearchEventTracking.Action.CLICK_BROAD_MATCH,
                EVENT_LABEL, String.format(Locale.getDefault(), "%s - %s", keyword, alternativeKeyword),
                CURRENT_SITE, SearchEventTracking.TOKOPEDIA_MARKETPLACE,
                BUSINESS_UNIT, SearchEventTracking.SEARCH,
                USER_ID, userId,
                ECOMMERCE, DataLayer.mapOf(
                    SearchEventTracking.ECommerce.CLICK, DataLayer.mapOf(
                        SearchEventTracking.ECommerce.ACTION_FIELD, DataLayer.mapOf(
                            SearchEventTracking.ECommerce.LIST,
                            getBroadMatchListName(isOrganicAds, componentId)
                        ),
                        SearchEventTracking.ECommerce.PRODUCTS, broadMatchItems
                    )
                )
            )
        )
    }

    fun getBroadMatchListName(isOrganicAds: Boolean, componentId: String): String {
        val organicStatus =
            if (isOrganicAds) SearchEventTracking.ORGANIC_ADS
            else SearchEventTracking.ORGANIC

        return String.format(
            BROADMATCH_LIST_NAME,
            organicStatus,
            componentId
        )
    }

    @JvmStatic
    fun trackEventImpressionBroadMatch(
        trackingQueue: TrackingQueue,
        keyword: String?,
        alternativeKeyword: String?,
        userId: String?,
        broadMatchItems: ArrayList<Any>,
    ) {
        val map = DataLayer.mapOf(
            EVENT, SearchEventTracking.Event.PRODUCT_VIEW,
            EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
            EVENT_ACTION, IMPRESSION_BROAD_MATCH,
            EVENT_LABEL, String.format(Locale.getDefault(), "%s - %s", keyword, alternativeKeyword),
            CURRENT_SITE, SearchEventTracking.TOKOPEDIA_MARKETPLACE,
            BUSINESS_UNIT, SearchEventTracking.SEARCH,
            USER_ID, userId,
            ECOMMERCE, DataLayer.mapOf(
                SearchEventTracking.ECommerce.CURRENCY_CODE, SearchEventTracking.ECommerce.IDR,
                SearchEventTracking.ECommerce.IMPRESSIONS, broadMatchItems
            )
        ) as HashMap<String, Any>

        trackingQueue.putEETracking(map)
    }

    @JvmStatic
    fun trackEventImpressionBroadMatch(iris: Iris, searchComponentTracking: SearchComponentTracking) {
        SearchComponentTrackingRollence.impress(
            iris,
            listOf(searchComponentTracking),
            SEARCH_BROAD_MATCH_TRACKER_UNIFICATION
        )
    }

    @JvmStatic
    fun trackEventClickBroadMatchSeeMore(
        searchComponentTracking: SearchComponentTracking,
        keyword: String?,
        alternativeKeyword: String?,
        pageSource: String,
    ) {
        SearchComponentTrackingRollence.click(
            searchComponentTracking,
            SEARCH_BROAD_MATCH_TRACKER_UNIFICATION,
        ) {
            val eventLabel = String.format(
                Locale.getDefault(),
                "%s - %s",
                keyword,
                alternativeKeyword
            )

            TrackApp.getInstance().gtm.sendGeneralEvent(
                DataLayer.mapOf(
                    EVENT, SearchEventTracking.Event.SEARCH_RESULT,
                    EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                    EVENT_ACTION, CLICK_BROAD_MATCH_LIHAT_SEMUA,
                    EVENT_LABEL, eventLabel,
                    PAGE_SOURCE, pageSource,
                )
            )
        }
    }
}
