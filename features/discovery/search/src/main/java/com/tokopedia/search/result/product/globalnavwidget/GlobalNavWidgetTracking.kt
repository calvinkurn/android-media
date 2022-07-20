package com.tokopedia.search.result.product.globalnavwidget

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.search.analytics.SearchEventTracking
import com.tokopedia.search.analytics.SearchTrackingConstant.ECOMMERCE
import com.tokopedia.search.analytics.SearchTrackingConstant.EVENT
import com.tokopedia.search.analytics.SearchTrackingConstant.EVENT_ACTION
import com.tokopedia.search.analytics.SearchTrackingConstant.EVENT_CATEGORY
import com.tokopedia.search.analytics.SearchTrackingConstant.EVENT_LABEL
import com.tokopedia.search.analytics.SearchTrackingConstant.PROMOTIONS
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue
import java.util.*
import kotlin.collections.HashMap

object GlobalNavWidgetTracking {

    private const val CLICK_SEE_ALL_NAV_WIDGET = "click - lihat semua widget"
    private const val IMPRESSION_WIDGET_DIGITAL_PRODUCT = "impression widget - digital product"

    @JvmStatic
    fun trackEventClickGlobalNavWidgetItem(
        item: Any?,
        keyword: String,
        productName: String,
        applink: String,
    ) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            DataLayer.mapOf(
                EVENT, SearchEventTracking.Event.PROMO_CLICK,
                EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                EVENT_ACTION, SearchEventTracking.Action.CLICK,
                EVENT_LABEL, generateEventLabelGlobalNav(keyword, productName, applink),
                ECOMMERCE, DataLayer.mapOf(
                    SearchEventTracking.Event.PROMO_CLICK,
                    DataLayer.mapOf(PROMOTIONS, DataLayer.listOf(item))
                )
            )
        )
    }

    private fun generateEventLabelGlobalNav(
        keyword: String,
        productName: String,
        applink: String,
    ): String {
        val eventLabelFormat = "keyword: %s - product: %s - applink: %s"
        return String.format(Locale.getDefault(), eventLabelFormat, keyword, productName, applink)
    }

    @JvmStatic
    fun eventUserClickSeeAllGlobalNavWidget(keyword: String,
                                            productName: String,
                                            applink: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            SearchEventTracking.Event.SEARCH_RESULT,
            SearchEventTracking.Category.SEARCH_RESULT,
            CLICK_SEE_ALL_NAV_WIDGET,
            generateEventLabelGlobalNav(keyword, productName, applink)
        )
    }

    @JvmStatic
    fun trackEventImpressionGlobalNavWidgetItem(
        trackingQueue: TrackingQueue,
        list: ArrayList<Any>,
        keyword: String?,
    ) {
        trackingQueue.putEETracking(
            DataLayer.mapOf(
                EVENT, SearchEventTracking.Event.PROMO_VIEW,
                EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                EVENT_ACTION, IMPRESSION_WIDGET_DIGITAL_PRODUCT,
                EVENT_LABEL, keyword,
                ECOMMERCE, DataLayer.mapOf(
                    SearchEventTracking.Event.PROMO_VIEW, DataLayer.mapOf(PROMOTIONS, list)
                )
            ) as HashMap<String, Any>
        )
    }
}