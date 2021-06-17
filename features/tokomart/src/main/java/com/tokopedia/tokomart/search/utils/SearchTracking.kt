package com.tokopedia.tokomart.search.utils

import android.text.TextUtils
import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.MISC.BUSINESSUNIT
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.MISC.CURRENTSITE
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_VALUE
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.VALUE.CURRENT_SITE_VALUE
import com.tokopedia.tokomart.search.utils.SearchTracking.Action.IMPRESSION_PRODUCT
import com.tokopedia.tokomart.search.utils.SearchTracking.Category.TOKONOW_SEARCH_RESULT
import com.tokopedia.tokomart.search.utils.SearchTracking.ECommerce.CURRENCYCODE
import com.tokopedia.tokomart.search.utils.SearchTracking.ECommerce.ECOMMERCE
import com.tokopedia.tokomart.search.utils.SearchTracking.ECommerce.IDR
import com.tokopedia.tokomart.search.utils.SearchTracking.ECommerce.IMPRESSIONS
import com.tokopedia.tokomart.search.utils.SearchTracking.Event.PRODUCT_VIEW
import com.tokopedia.tokomart.search.utils.SearchTracking.Misc.USER_ID
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.trackingoptimizer.TrackingQueue
import java.util.HashMap

object SearchTracking {

    object Event {
        const val PRODUCT_VIEW = "productView"
    }

    object Action {
        const val GENERAL_SEARCH = "general search"
        const val IMPRESSION_PRODUCT = "impression - product"
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
    }

    object Misc {
        const val USER_ID = "userId"
        const val HASIL_PENCARIAN_DI_TOKONOW = "Hasil pencarian di TokoNOW!"
        const val LOCAL_SEARCH = "local_search"
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
                TrackAppUtils.EVENT, PRODUCT_VIEW,
                TrackAppUtils.EVENT_CATEGORY, TOKONOW_SEARCH_RESULT,
                TrackAppUtils.EVENT_ACTION, IMPRESSION_PRODUCT,
                TrackAppUtils.EVENT_LABEL, keyword,
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
}