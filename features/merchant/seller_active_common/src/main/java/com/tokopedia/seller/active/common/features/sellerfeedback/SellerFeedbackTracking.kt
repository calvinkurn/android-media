package com.tokopedia.seller.active.common.features.sellerfeedback

import com.tokopedia.track.builder.Tracker

/**
 * Created by @ilhamsuaib on 27/03/23.
 */

object SellerFeedbackTracking {

    private const val EVENT_CATEGORY_HOMEPAGE = "seller app - home"
    private const val EVENT_VIEW_HOMEPAGE = "viewHomepageIris"
    private const val EVENT_ACTION_IMPRESSION_FEEDBACK_BUTTON = "impression feedback button"
    private const val EVENT_LABEL_HOMEPAGE = ""
    private const val EVENT_CLICK_HOMEPAGE = "clickHomepage"
    private const val EVENT_ACTION_CLICK_FEEDBACK_BUTTON = "click feedback button"

    fun eventViewHomepage() {
        Tracker.Builder()
            .setEvent(EVENT_VIEW_HOMEPAGE)
            .setEventAction(EVENT_ACTION_IMPRESSION_FEEDBACK_BUTTON)
            .setEventCategory(EVENT_CATEGORY_HOMEPAGE)
            .setEventLabel(EVENT_LABEL_HOMEPAGE)
            .build()
            .send()
    }

    fun eventClickFeedbackButton() {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_HOMEPAGE)
            .setEventAction(EVENT_ACTION_CLICK_FEEDBACK_BUTTON)
            .setEventCategory(EVENT_CATEGORY_HOMEPAGE)
            .setEventLabel(EVENT_LABEL_HOMEPAGE)
            .build()
            .send()
    }
}