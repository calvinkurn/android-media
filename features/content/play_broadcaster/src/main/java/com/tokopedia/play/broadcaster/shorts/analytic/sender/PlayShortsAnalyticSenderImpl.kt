package com.tokopedia.play.broadcaster.shorts.analytic.sender

import com.tokopedia.config.GlobalConfig
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.play.broadcaster.shorts.analytic.const.Label.IS_LOGGED_IN_STATUS_LABEL
import com.tokopedia.play.broadcaster.shorts.analytic.const.Label.SCREEN_NAME_LABEL
import com.tokopedia.play.broadcaster.shorts.analytic.const.Label.SESSION_IRIS_LABEL
import com.tokopedia.play.broadcaster.shorts.analytic.const.Label.TRACKER_ID_LABEL
import com.tokopedia.play.broadcaster.shorts.analytic.const.Value
import com.tokopedia.play.broadcaster.shorts.analytic.const.Value.SHORTS_BUSINESS_UNIT
import com.tokopedia.play.broadcaster.shorts.analytic.const.Value.SHORTS_CLICK_CONTENT
import com.tokopedia.play.broadcaster.shorts.analytic.const.Value.SHORTS_EVENT_CATEGORY
import com.tokopedia.play.broadcaster.shorts.analytic.const.Value.SHORTS_OPEN_SCREEN
import com.tokopedia.play.broadcaster.shorts.analytic.const.Value.SHORTS_VIEW_CONTENT
import com.tokopedia.play.broadcaster.shorts.analytic.helper.PlayShortsAnalyticHelper
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 24, 2022
 */
class PlayShortsAnalyticSenderImpl @Inject constructor(
    private val userSession: UserSessionInterface
) : PlayShortsAnalyticSender {

    private val currentSite: String
        get() = if (GlobalConfig.isSellerApp()) {
            Value.SHORTS_CURRENT_SITE_SELLER
        } else {
            Value.SHORTS_CURRENT_SITE_MAIN
        }

    private val sessionIris: String
        get() = TrackApp.getInstance().gtm.irisSessionId

    override fun sendGeneralOpenScreen(
        screenName: String,
        trackerId: String,
    ) {
        sendGeneralEvent(
            Tracker.Builder()
                .setEvent(SHORTS_OPEN_SCREEN)
                .setCustomProperty(IS_LOGGED_IN_STATUS_LABEL, userSession.isLoggedIn)
                .setCustomProperty(TRACKER_ID_LABEL, trackerId)
                .setCustomProperty(SCREEN_NAME_LABEL, screenName)
        )
    }

    override fun sendGeneralViewEvent(
        eventAction: String,
        account: ContentAccountUiModel,
        trackerId: String
    ) {
        sendGeneralEvent(
            Tracker.Builder()
                .setEvent(SHORTS_VIEW_CONTENT)
                .setEventCategory(SHORTS_EVENT_CATEGORY)
                .setEventAction(eventAction)
                .setEventLabel(PlayShortsAnalyticHelper.getEventLabelByAccount(account))
                .setCustomProperty(TRACKER_ID_LABEL, trackerId)
        )
    }

    override fun sendGeneralViewEvent(eventAction: String, eventLabel: String, trackerId: String) {
        sendGeneralEvent(
            Tracker.Builder()
                .setEvent(SHORTS_VIEW_CONTENT)
                .setEventCategory(SHORTS_EVENT_CATEGORY)
                .setEventAction(eventAction)
                .setEventLabel(eventLabel)
                .setCustomProperty(TRACKER_ID_LABEL, trackerId)
        )
    }

    override fun sendGeneralClickEvent(
        eventAction: String,
        account: ContentAccountUiModel,
        trackerId: String
    ) {
        sendGeneralEvent(
            Tracker.Builder()
                .setEvent(SHORTS_CLICK_CONTENT)
                .setEventCategory(SHORTS_EVENT_CATEGORY)
                .setEventAction(eventAction)
                .setEventLabel(PlayShortsAnalyticHelper.getEventLabelByAccount(account))
                .setCustomProperty(TRACKER_ID_LABEL, trackerId)
        )
    }

    override fun sendGeneralClickEvent(
        eventAction: String,
        eventLabel: String,
        trackerId: String
    ) {
        sendGeneralEvent(
            Tracker.Builder()
                .setEvent(SHORTS_CLICK_CONTENT)
                .setEventCategory(SHORTS_EVENT_CATEGORY)
                .setEventAction(eventAction)
                .setEventLabel(eventLabel)
                .setCustomProperty(TRACKER_ID_LABEL, trackerId)
        )
    }

    private fun sendGeneralEvent(
        trackerBuilder: Tracker.Builder
    ) {
        trackerBuilder
            .setBusinessUnit(SHORTS_BUSINESS_UNIT)
            .setCurrentSite(currentSite)
            .setUserId(userSession.userId)
            .setCustomProperty(SESSION_IRIS_LABEL, sessionIris)
            .build()
            .send()
    }
}
