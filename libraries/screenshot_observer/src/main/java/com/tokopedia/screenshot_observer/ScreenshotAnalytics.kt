package com.tokopedia.screenshot_observer

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

object ScreenshotAnalytics {

    private const val FEEDBACK_FORM = "feedback form"
    private const val CLICK_FEEDBACK = "clickFeedback"
    private const val CLICK_SCREENSHOT = "clickScreenshot"

    private fun sendEventCategoryAction(event: String, eventCategory: String,
                                        eventAction: String) {
        sendEventCategoryActionLabel(event, eventCategory, eventAction, "")
    }

    private fun sendEventCategoryActionLabel(event: String, eventCategory: String,
                                             eventAction: String, eventLabel: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                event, eventCategory, eventAction, eventLabel))
    }

    fun eventUseScreenshot() {
        sendEventCategoryAction(CLICK_FEEDBACK, FEEDBACK_FORM, CLICK_SCREENSHOT)
    }

}