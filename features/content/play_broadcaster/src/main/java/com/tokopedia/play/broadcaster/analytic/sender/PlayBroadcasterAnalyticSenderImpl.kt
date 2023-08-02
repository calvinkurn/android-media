package com.tokopedia.play.broadcaster.analytic.sender

import com.tokopedia.config.GlobalConfig
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.play.broadcaster.analytic.const.Value
import com.tokopedia.play.broadcaster.analytic.const.Label
import com.tokopedia.play.broadcaster.analytic.helper.PlayBroadcasterAnalyticHelper
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on March 17, 2023
 */
class PlayBroadcasterAnalyticSenderImpl @Inject constructor(
    private val userSession: UserSessionInterface
) : PlayBroadcasterAnalyticSender {

    override fun sendGeneralOpenScreen(
        screenName: String,
        trackerId: String,
    ) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(
            screenName,
            mapOf(
                Label.TRACKER_ID_LABEL to trackerId,
                Label.BUSINESS_UNIT_LABEL to Value.BROADCASTER_BUSINESS_UNIT,
                Label.CURRENT_SITE_LABEL to PlayBroadcasterAnalyticHelper.currentSite,
                Label.SESSION_IRIS_LABEL to PlayBroadcasterAnalyticHelper.sessionIris,
            )
        )
    }

    override fun sendGeneralViewEvent(
        eventAction: String,
        account: ContentAccountUiModel,
        trackerId: String
    ) {
        sendGeneralEvent(
            Tracker.Builder()
                .setEvent(Value.BROADCASTER_VIEW_CONTENT)
                .setEventCategory(Value.BROADCASTER_EVENT_CATEGORY)
                .setEventAction(eventAction)
                .setEventLabel(PlayBroadcasterAnalyticHelper.getEventLabelByAccount(account))
                .setCustomProperty(Label.TRACKER_ID_LABEL, trackerId)
        )
    }

    override fun sendGeneralViewEvent(eventAction: String, eventLabel: String, trackerId: String) {
        sendGeneralEvent(
            Tracker.Builder()
                .setEvent(Value.BROADCASTER_VIEW_CONTENT)
                .setEventCategory(Value.BROADCASTER_EVENT_CATEGORY)
                .setEventAction(eventAction)
                .setEventLabel(eventLabel)
                .setCustomProperty(Label.TRACKER_ID_LABEL, trackerId)
        )
    }

    override fun sendGeneralClickEvent(
        eventAction: String,
        account: ContentAccountUiModel,
        trackerId: String
    ) {
        sendGeneralEvent(
            Tracker.Builder()
                .setEvent(Value.BROADCASTER_CLICK_CONTENT)
                .setEventCategory(Value.BROADCASTER_EVENT_CATEGORY)
                .setEventAction(eventAction)
                .setEventLabel(PlayBroadcasterAnalyticHelper.getEventLabelByAccount(account))
                .setCustomProperty(Label.TRACKER_ID_LABEL, trackerId)
        )
    }

    override fun sendGeneralClickEvent(
        eventAction: String,
        eventLabel: String,
        trackerId: String
    ) {
        sendGeneralEvent(
            Tracker.Builder()
                .setEvent(Value.BROADCASTER_CLICK_CONTENT)
                .setEventCategory(Value.BROADCASTER_EVENT_CATEGORY)
                .setEventAction(eventAction)
                .setEventLabel(eventLabel)
                .setCustomProperty(Label.TRACKER_ID_LABEL, trackerId)
        )
    }

    private fun sendGeneralEvent(
        trackerBuilder: Tracker.Builder
    ) {
        trackerBuilder
            .setBusinessUnit(Value.BROADCASTER_BUSINESS_UNIT)
            .setCurrentSite(PlayBroadcasterAnalyticHelper.currentSite)
            .setUserId(userSession.userId)
            .setCustomProperty(Label.SESSION_IRIS_LABEL, PlayBroadcasterAnalyticHelper.sessionIris)
            .build()
            .send()
    }
}
