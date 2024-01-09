package com.tokopedia.tokopedianow.annotation.analytic

import com.tokopedia.tokopedianow.annotation.analytic.AllAnnotationAnalytics.ACTION.EVENT_ACTION_CLICK_ANNOTATION_CARD
import com.tokopedia.tokopedianow.annotation.analytic.AllAnnotationAnalytics.ACTION.EVENT_ACTION_IMPRESS_ALL_ANNOTATION_PAGE
import com.tokopedia.tokopedianow.annotation.analytic.AllAnnotationAnalytics.ACTION.EVENT_ACTION_IMPRESS_ANNOTATION_CARD
import com.tokopedia.tokopedianow.annotation.analytic.AllAnnotationAnalytics.ACTION.EVENT_CLICK_BACK_ALL_ANNOTATION_PAGE
import com.tokopedia.tokopedianow.annotation.analytic.AllAnnotationAnalytics.CATEGORY.EVENT_CATEGORY_PAGE
import com.tokopedia.tokopedianow.annotation.analytic.AllAnnotationAnalytics.TRACKER_ID.ID_BACK_ALL_ANNOTATION_PAGE
import com.tokopedia.tokopedianow.annotation.analytic.AllAnnotationAnalytics.TRACKER_ID.ID_CLICK_ANNOTATION_CARD
import com.tokopedia.tokopedianow.annotation.analytic.AllAnnotationAnalytics.TRACKER_ID.ID_IMPRESS_ALL_ANNOTATION_PAGE
import com.tokopedia.tokopedianow.annotation.analytic.AllAnnotationAnalytics.TRACKER_ID.ID_IMPRESS_ANNOTATION_CARD
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_GROCERIES
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_GROCERIES
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_TRACKER_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalytics.joinDash
import com.tokopedia.track.builder.Tracker

/**
 * All Annotation Tracker
 * Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4417
 **/

class AllAnnotationAnalytics(
    val categoryId: String,
    val annotationType: String
) {
    internal object CATEGORY {
        const val EVENT_CATEGORY_PAGE = "tokonow - all annotation page"
    }

    internal object ACTION {
        const val EVENT_ACTION_IMPRESS_ANNOTATION_CARD = "impression annotation card on all-annotation page"
        const val EVENT_ACTION_CLICK_ANNOTATION_CARD = "click annotation card on all-annotation page"
        const val EVENT_ACTION_IMPRESS_ALL_ANNOTATION_PAGE = "impression all-annotation page"
        const val EVENT_CLICK_BACK_ALL_ANNOTATION_PAGE = "click back on all-annotation page"
    }

    internal object TRACKER_ID {
        const val ID_IMPRESS_ANNOTATION_CARD = "49083"
        const val ID_CLICK_ANNOTATION_CARD = "49084"
        const val ID_IMPRESS_ALL_ANNOTATION_PAGE = "49086"
        const val ID_BACK_ALL_ANNOTATION_PAGE = "49087"
    }

    fun trackImpressAnnotationCard(
        annotationValue: String
    ) {
        Tracker.Builder()
            .setEvent(EVENT_VIEW_GROCERIES)
            .setEventAction(EVENT_ACTION_IMPRESS_ANNOTATION_CARD)
            .setEventCategory(EVENT_CATEGORY_PAGE)
            .setEventLabel(joinDash(categoryId, annotationType, annotationValue))
            .setCustomProperty(KEY_TRACKER_ID, ID_IMPRESS_ANNOTATION_CARD)
            .setBusinessUnit(BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE)
            .setCurrentSite(CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            .build()
            .send()
    }

    fun trackClickAnnotationCard(
        annotationValue: String
    ) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_GROCERIES)
            .setEventAction(EVENT_ACTION_CLICK_ANNOTATION_CARD)
            .setEventCategory(EVENT_CATEGORY_PAGE)
            .setEventLabel(joinDash(categoryId, annotationType, annotationValue))
            .setCustomProperty(KEY_TRACKER_ID, ID_CLICK_ANNOTATION_CARD)
            .setBusinessUnit(BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE)
            .setCurrentSite(CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            .build()
            .send()
    }

    fun trackImpressAllAnnotationPage() {
        Tracker.Builder()
            .setEvent(EVENT_VIEW_GROCERIES)
            .setEventAction(EVENT_ACTION_IMPRESS_ALL_ANNOTATION_PAGE)
            .setEventCategory(EVENT_CATEGORY_PAGE)
            .setEventLabel(joinDash(categoryId, annotationType))
            .setCustomProperty(KEY_TRACKER_ID, ID_IMPRESS_ALL_ANNOTATION_PAGE)
            .setBusinessUnit(BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE)
            .setCurrentSite(CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            .build()
            .send()
    }

    fun trackClickBackAllAnnotationPage() {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_GROCERIES)
            .setEventAction(EVENT_CLICK_BACK_ALL_ANNOTATION_PAGE)
            .setEventCategory(EVENT_CATEGORY_PAGE)
            .setEventLabel(joinDash(categoryId, annotationType))
            .setCustomProperty(KEY_TRACKER_ID, ID_BACK_ALL_ANNOTATION_PAGE)
            .setBusinessUnit(BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE)
            .setCurrentSite(CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            .build()
            .send()
    }
}
