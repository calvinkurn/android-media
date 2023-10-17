package com.tokopedia.play.broadcaster.analytic.sender

import com.tokopedia.content.analytic.BusinessUnit
import com.tokopedia.content.analytic.Event
import com.tokopedia.content.analytic.Key
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.play.broadcaster.analytic.const.Value
import com.tokopedia.play.broadcaster.analytic.currentSite
import com.tokopedia.play.broadcaster.analytic.helper.PlayBroadcasterAnalyticHelper
import com.tokopedia.play.broadcaster.analytic.sessionIris
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
        trackerId: String
    ) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(
            screenName,
            mapOf(
                Key.trackerId to trackerId,
                Key.businessUnit to BusinessUnit.play,
                Key.currentSite to currentSite,
                Key.sessionIris to sessionIris
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
                .setEvent(Event.viewContentIris)
                .setEventCategory(Value.BROADCASTER_EVENT_CATEGORY)
                .setEventAction(eventAction)
                .setEventLabel(PlayBroadcasterAnalyticHelper.getEventLabelByAccount(account))
                .setCustomProperty(Key.trackerId, trackerId)
        )
    }

    override fun sendGeneralViewEvent(eventAction: String, eventLabel: String, trackerId: String) {
        sendGeneralEvent(
            Tracker.Builder()
                .setEvent(Event.viewContentIris)
                .setEventCategory(Value.BROADCASTER_EVENT_CATEGORY)
                .setEventAction(eventAction)
                .setEventLabel(eventLabel)
                .setCustomProperty(Key.trackerId, trackerId)
        )
    }

    override fun sendGeneralClickEvent(
        eventAction: String,
        account: ContentAccountUiModel,
        trackerId: String
    ) {
        sendGeneralEvent(
            Tracker.Builder()
                .setEvent(Event.clickContent)
                .setEventCategory(Value.BROADCASTER_EVENT_CATEGORY)
                .setEventAction(eventAction)
                .setEventLabel(PlayBroadcasterAnalyticHelper.getEventLabelByAccount(account))
                .setCustomProperty(Key.trackerId, trackerId)
        )
    }

    override fun sendGeneralClickEvent(
        eventAction: String,
        eventLabel: String,
        trackerId: String
    ) {
        sendGeneralEvent(
            Tracker.Builder()
                .setEvent(Event.clickContent)
                .setEventCategory(Value.BROADCASTER_EVENT_CATEGORY)
                .setEventAction(eventAction)
                .setEventLabel(eventLabel)
                .setCustomProperty(Key.trackerId, trackerId)
        )
    }

    private fun sendGeneralEvent(
        trackerBuilder: Tracker.Builder
    ) {
        trackerBuilder
            .setBusinessUnit(BusinessUnit.play)
            .setCurrentSite(currentSite)
            .setUserId(userSession.userId)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .build()
            .send()
    }
}
