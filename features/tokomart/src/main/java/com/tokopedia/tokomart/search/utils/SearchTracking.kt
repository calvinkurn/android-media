package com.tokopedia.tokomart.search.utils

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.MISC.BUSINESSUNIT
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.MISC.CURRENTSITE
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_VALUE
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.VALUE.CURRENT_SITE_VALUE
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.VALUE.EVENT_CLICK_VALUE
import com.tokopedia.tokomart.search.utils.SearchTracking.Action.CLICK_APPLY_FILTER
import com.tokopedia.tokomart.search.utils.SearchTracking.Action.CLICK_CATEGORY_FILTER
import com.tokopedia.tokomart.search.utils.SearchTracking.Action.CLICK_FILTER_OPTION
import com.tokopedia.tokomart.search.utils.SearchTracking.Action.CLICK_FUZZY_KEYWORDS_REPLACE
import com.tokopedia.tokomart.search.utils.SearchTracking.Action.CLICK_PRODUCT
import com.tokopedia.tokomart.search.utils.SearchTracking.Action.IMPRESSION_PRODUCT
import com.tokopedia.tokomart.search.utils.SearchTracking.Category.TOKONOW_SEARCH_RESULT
import com.tokopedia.tokomart.search.utils.SearchTracking.ECommerce.ACTION_FIELD
import com.tokopedia.tokomart.search.utils.SearchTracking.ECommerce.CLICK
import com.tokopedia.tokomart.search.utils.SearchTracking.ECommerce.CURRENCYCODE
import com.tokopedia.tokomart.search.utils.SearchTracking.ECommerce.ECOMMERCE
import com.tokopedia.tokomart.search.utils.SearchTracking.ECommerce.IDR
import com.tokopedia.tokomart.search.utils.SearchTracking.ECommerce.IMPRESSIONS
import com.tokopedia.tokomart.search.utils.SearchTracking.ECommerce.LIST
import com.tokopedia.tokomart.search.utils.SearchTracking.ECommerce.PRODUCTS
import com.tokopedia.tokomart.search.utils.SearchTracking.Event.PRODUCT_CLICK
import com.tokopedia.tokomart.search.utils.SearchTracking.Event.PRODUCT_VIEW
import com.tokopedia.tokomart.search.utils.SearchTracking.Misc.TOKONOW_SEARCH_PRODUCT_ORGANIC
import com.tokopedia.tokomart.search.utils.SearchTracking.Misc.USER_ID
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.TrackAppUtils.EVENT
import com.tokopedia.track.TrackAppUtils.EVENT_ACTION
import com.tokopedia.track.TrackAppUtils.EVENT_CATEGORY
import com.tokopedia.track.TrackAppUtils.EVENT_LABEL
import com.tokopedia.trackingoptimizer.TrackingQueue
import java.util.*

object SearchTracking {

    object Event {
        const val PRODUCT_VIEW = "productView"
        const val PRODUCT_CLICK = "productClick"
    }

    object Action {
        const val GENERAL_SEARCH = "general search"
        const val IMPRESSION_PRODUCT = "impression - product"
        const val CLICK_PRODUCT = "click - product"
        const val CLICK_FILTER_OPTION = "click - filter option"
        const val CLICK_APPLY_FILTER = "click - apply filter"
        const val CLICK_CATEGORY_FILTER = "click - category filter"
        const val CLICK_FUZZY_KEYWORDS_REPLACE = "click - fuzzy keywords - replace"
    }

    object Category {
        const val TOKONOW_TOP_NAV = "tokonow - top nav"
        const val TOKONOW_SEARCH_RESULT = "tokonow - search result"
    }

    object ECommerce {
        const val ECOMMERCE = "ecommerce"
        const val CURRENCYCODE = "currencyCode"
        const val IDR = "IDR"
        const val IMPRESSIONS = "impressions"
        const val CLICK = "click"
        const val ACTION_FIELD = "actionField"
        const val LIST = "list"
        const val PRODUCTS = "products"
    }

    object Misc {
        const val USER_ID = "userId"
        const val HASIL_PENCARIAN_DI_TOKONOW = "Hasil pencarian di TokoNOW!"
        const val LOCAL_SEARCH = "local_search"
        const val TOKONOW_SEARCH_PRODUCT_ORGANIC = "/tokonow - searchproduct - organic"
    }

    fun sendGeneralEvent(dataLayer: Map<String, Any>) {
        TrackApp.getInstance().gtm.sendGeneralEvent(dataLayer)
    }

    fun sendProductImpressionEvent(
            trackingQueue: TrackingQueue,
            list: List<Any>,
            keyword: String,
            userId: String,
    ) {
        val map = DataLayer.mapOf(
                EVENT, PRODUCT_VIEW,
                EVENT_CATEGORY, TOKONOW_SEARCH_RESULT,
                EVENT_ACTION, IMPRESSION_PRODUCT,
                EVENT_LABEL, keyword,
                BUSINESSUNIT, BUSINESS_UNIT_VALUE,
                CURRENTSITE, CURRENT_SITE_VALUE,
                USER_ID, userId,
                ECOMMERCE, DataLayer.mapOf(
                    CURRENCYCODE, IDR,
                    IMPRESSIONS, DataLayer.listOf(*list.toTypedArray())
                )
        ) as HashMap<String, Any>

        trackingQueue.putEETracking(map)
    }

    fun sendProductClickEvent(
            dataLayer: Any,
            keyword: String,
            userId: String,
    ) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        EVENT, PRODUCT_CLICK,
                        EVENT_ACTION, TOKONOW_SEARCH_RESULT,
                        EVENT_CATEGORY, CLICK_PRODUCT,
                        EVENT_LABEL, keyword,
                        BUSINESSUNIT, BUSINESS_UNIT_VALUE,
                        CURRENTSITE, CURRENT_SITE_VALUE,
                        USER_ID, userId,
                        ECOMMERCE, DataLayer.mapOf(
                            CLICK, DataLayer.mapOf(
                                ACTION_FIELD, DataLayer.mapOf(
                                    LIST, TOKONOW_SEARCH_PRODUCT_ORGANIC,
                                    PRODUCTS, DataLayer.listOf(dataLayer)
                                )
                            ),
                        )
                )
        )
    }
    
    fun sendOpenFilterPageEvent() {
        sendGeneralEvent(
                DataLayer.mapOf(
                    EVENT, EVENT_CLICK_VALUE,
                    EVENT_ACTION, CLICK_FILTER_OPTION,
                    EVENT_CATEGORY, TOKONOW_SEARCH_RESULT,
                    EVENT_LABEL, "",
                    BUSINESSUNIT, BUSINESS_UNIT_VALUE,
                    CURRENTSITE, CURRENT_SITE_VALUE,
                )
        )
    }

    fun sendApplySortFilterEvent(filterParams: String) {
        sendGeneralEvent(
                DataLayer.mapOf(
                    EVENT, EVENT_CLICK_VALUE,
                    EVENT_ACTION, CLICK_APPLY_FILTER,
                    EVENT_CATEGORY, TOKONOW_SEARCH_RESULT,
                    EVENT_LABEL, filterParams,
                    BUSINESSUNIT, BUSINESS_UNIT_VALUE,
                    CURRENTSITE, CURRENT_SITE_VALUE,
                )
        )
    }

    fun sendApplyCategoryL2FilterEvent(categoryName: String) {
        sendGeneralEvent(
                DataLayer.mapOf(
                    EVENT, EVENT_CLICK_VALUE,
                    EVENT_ACTION, CLICK_CATEGORY_FILTER,
                    EVENT_CATEGORY, TOKONOW_SEARCH_RESULT,
                    EVENT_LABEL, categoryName,
                    BUSINESSUNIT, BUSINESS_UNIT_VALUE,
                    CURRENTSITE, CURRENT_SITE_VALUE,
                )
        )
    }

    fun sendSuggestionClickEvent(originalKeyword: String, fuzzyKeyword: String, ) {
        sendGeneralEvent(
                DataLayer.mapOf(
                    EVENT, EVENT_CLICK_VALUE,
                    EVENT_ACTION, CLICK_FUZZY_KEYWORDS_REPLACE,
                    EVENT_CATEGORY, TOKONOW_SEARCH_RESULT,
                    EVENT_LABEL, "$originalKeyword - $fuzzyKeyword",
                    BUSINESSUNIT, BUSINESS_UNIT_VALUE,
                    CURRENTSITE, CURRENT_SITE_VALUE,
                )
        )
    }
}