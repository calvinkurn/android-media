package com.tokopedia.tokopedianow.annotation.analytic

import com.tokopedia.tokopedianow.category.analytic.CategoryAnalytic.CATEGORY.EVENT_CATEGORY_PAGE_L1
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_GROCERIES
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_GROCERIES
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_TRACKER_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalytics.joinDash
import com.tokopedia.track.builder.Tracker

class AnnotationWidgetAnalytic(
    private val categoryIdL1: String = "",
    private val annotationType: String = ""
) {

    companion object {
        const val ANNOTATION_TYPE_BRAND = "Brand"

        private const val EVENT_ACTION_IMPRESSION_ANNOTATION_WIDGET = "impression annotation widget"
        private const val EVENT_IMPRESSION_ANNOTATION_CARD =
            "impression annotation card on annotation widget"
        private const val EVENT_ACTION_CLICK_ANNOTATION_CARD =
            "click annotation card on annotation widget"
        private const val EVENT_ACTION_CLICK_ARROW_BUTTON =
            "click arrow button on annotation widget"
        private const val EVENT_ACTION_CLICK_VIEW_ALL =
            "click view all on annotation widget"

        private const val TRACKER_ID_IMPRESSION_ANNOTATION_WIDGET = "49079"
        private const val TRACKER_ID_IMPRESSION_ANNOTATION_CARD = "49141"
        private const val TRACKER_ID_CLICK_ANNOTATION_CARD = "49081"
        private const val TRACKER_ID_CLICK_ARROW_BUTTON = "49082"
        private const val TRACKER_ID_CLICK_CLICK_VIEW_ALL = "49085"
    }

    private val defaultEventLabel = joinDash(categoryIdL1, annotationType)

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4417
    // Tracker ID: 49079
    fun sendImpressionAnnotationWidgetEvent() {
        Tracker.Builder()
            .setEvent(EVENT_VIEW_GROCERIES)
            .setEventAction(EVENT_ACTION_IMPRESSION_ANNOTATION_WIDGET)
            .setEventCategory(EVENT_CATEGORY_PAGE_L1)
            .setEventLabel(defaultEventLabel)
            .setCustomProperty(KEY_TRACKER_ID, TRACKER_ID_IMPRESSION_ANNOTATION_WIDGET)
            .setBusinessUnit(BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE)
            .setCurrentSite(CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4417
    // Tracker ID: 49081
    fun sendClickAnnotationCardEvent(annotationName: String) {
        val eventLabel = joinDash(categoryIdL1, annotationType, annotationName)

        Tracker.Builder()
            .setEvent(EVENT_CLICK_GROCERIES)
            .setEventAction(EVENT_ACTION_CLICK_ANNOTATION_CARD)
            .setEventCategory(EVENT_CATEGORY_PAGE_L1)
            .setEventLabel(eventLabel)
            .setCustomProperty(KEY_TRACKER_ID, TRACKER_ID_CLICK_ANNOTATION_CARD)
            .setBusinessUnit(BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE)
            .setCurrentSite(CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4417
    // Tracker ID: 49082
    fun sendClickArrowButtonEvent() {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_GROCERIES)
            .setEventAction(EVENT_ACTION_CLICK_ARROW_BUTTON)
            .setEventCategory(EVENT_CATEGORY_PAGE_L1)
            .setEventLabel(defaultEventLabel)
            .setCustomProperty(KEY_TRACKER_ID, TRACKER_ID_CLICK_ARROW_BUTTON)
            .setBusinessUnit(BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE)
            .setCurrentSite(CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4417
    // Tracker ID: 49085
    fun sendClickViewAllEvent() {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_GROCERIES)
            .setEventAction(EVENT_ACTION_CLICK_VIEW_ALL)
            .setEventCategory(EVENT_CATEGORY_PAGE_L1)
            .setEventLabel(defaultEventLabel)
            .setCustomProperty(KEY_TRACKER_ID, TRACKER_ID_CLICK_CLICK_VIEW_ALL)
            .setBusinessUnit(BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE)
            .setCurrentSite(CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4417
    // Tracker ID: 49141
    fun sendImpressionAnnotationCardEvent(annotationName: String) {
        val eventLabel = joinDash(categoryIdL1, annotationType, annotationName)

        Tracker.Builder()
            .setEvent(EVENT_VIEW_GROCERIES)
            .setEventAction(EVENT_IMPRESSION_ANNOTATION_CARD)
            .setEventCategory(EVENT_CATEGORY_PAGE_L1)
            .setEventLabel(eventLabel)
            .setCustomProperty(KEY_TRACKER_ID, TRACKER_ID_IMPRESSION_ANNOTATION_CARD)
            .setBusinessUnit(BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE)
            .setCurrentSite(CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            .build()
            .send()
    }
}
