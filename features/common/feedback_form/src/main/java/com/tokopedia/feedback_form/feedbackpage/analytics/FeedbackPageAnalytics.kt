package com.tokopedia.feedback_form.feedbackpage.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

object FeedbackPageAnalytics {

    private const val FEEDBACK_FORM = "feedback form"
    private const val CLICK_FEEDBACK = "clickFeedback"
    private const val CLICK_SCREENSHOT = "clickScreenshot"
    private const val OPEN_FEEDBACK = "open feedback form"
    private const val CLICK_SUBMIT = "click submit"
    private const val CLICK_JIRA = "click jira"
    private const val COPY_JIRA = "copy jira"
    private const val LABEL_SCREENSHOT = "screenshot"
    private const val LABEL_SETTING = "setting"
    private const val LABEL_FEEDBACK = "feedback"
    private const val LABEL_BUG = "Bug"



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

    fun eventOpenFeedbackFromScreenshot() {
        sendEventCategoryActionLabel(CLICK_FEEDBACK, FEEDBACK_FORM, OPEN_FEEDBACK, LABEL_SCREENSHOT)
    }

    fun eventOpenFeedbackFromSettings() {
        sendEventCategoryActionLabel(CLICK_FEEDBACK, FEEDBACK_FORM, OPEN_FEEDBACK, LABEL_SETTING)
    }

    fun eventClickSubmitButtonFeedback() {
        sendEventCategoryActionLabel(CLICK_FEEDBACK, FEEDBACK_FORM, CLICK_SUBMIT, LABEL_FEEDBACK)
    }

    fun eventClickSubmitButtonBug() {
        sendEventCategoryActionLabel(CLICK_FEEDBACK, FEEDBACK_FORM, CLICK_SUBMIT, LABEL_BUG)
    }

    fun eventClickJiraLink() {
        sendEventCategoryAction(CLICK_FEEDBACK, FEEDBACK_FORM, CLICK_JIRA)
    }

    fun eventCopyJiraLink() {
        sendEventCategoryAction(CLICK_FEEDBACK, FEEDBACK_FORM, COPY_JIRA)
    }
}