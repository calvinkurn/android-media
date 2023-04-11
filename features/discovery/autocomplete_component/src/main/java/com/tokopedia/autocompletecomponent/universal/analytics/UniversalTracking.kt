package com.tokopedia.autocompletecomponent.universal.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue

object UniversalTracking {

    private const val ECOMMERCE = "ecommerce"
    private const val CAROUSEL_UNIFICATION_LIST_NAME = " /search - carousel %s - component:%s"

    fun trackEventImpressionCarouselUnification(
        trackingQueue: TrackingQueue,
        eventLabel: String,
        products: ArrayList<Any>,
    ) {
        val impressionDataLayer = DataLayer.mapOf(
            UniversalTrackingConstant.EVENT, UniversalEventTracking.Event.PRODUCT_VIEW,
            UniversalTrackingConstant.EVENT_CATEGORY, UniversalEventTracking.Category.SEARCH_RESULT,
            UniversalTrackingConstant.EVENT_ACTION, UniversalEventTracking.Action.IMPRESSION_CAROUSEL_PRODUCT,
            UniversalTrackingConstant.EVENT_LABEL, eventLabel,
            ECOMMERCE, DataLayer.mapOf(
                "currencyCode", "IDR",
                "impressions", products,
            ),
        ) as HashMap<String, Any>

        trackingQueue.putEETracking(impressionDataLayer)
    }

    fun trackEventClickCarouselUnification(
        eventLabel: String,
        type: String,
        componentId: String,
        products: ArrayList<Any>,
    ) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            DataLayer.mapOf(
                UniversalTrackingConstant.EVENT, UniversalEventTracking.Event.PRODUCT_CLICK,
                UniversalTrackingConstant.EVENT_CATEGORY, UniversalEventTracking.Category.SEARCH_RESULT,
                UniversalTrackingConstant.EVENT_ACTION, UniversalEventTracking.Action.CLICK_CAROUSEL_PRODUCT,
                UniversalTrackingConstant.EVENT_LABEL, eventLabel,
                ECOMMERCE, DataLayer.mapOf("click",
                    DataLayer.mapOf(
                        "actionField", DataLayer.mapOf(
                            "list", getCarouselUnificationListName(type, componentId)
                        ),
                        "products", products,
                    )
                ),
            )
        )
    }

    fun getCarouselUnificationListName(type: String, componentId: String): String =
        CAROUSEL_UNIFICATION_LIST_NAME.format(type, componentId)
}