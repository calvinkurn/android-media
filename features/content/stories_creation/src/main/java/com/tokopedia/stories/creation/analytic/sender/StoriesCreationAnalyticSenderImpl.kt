package com.tokopedia.stories.creation.analytic.sender

import com.tokopedia.config.GlobalConfig
import com.tokopedia.content.analytic.BusinessUnit
import com.tokopedia.content.analytic.CurrentSite
import com.tokopedia.content.analytic.Event
import com.tokopedia.content.analytic.EventCategory
import com.tokopedia.content.analytic.Key
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.stories.creation.analytic.helper.StoriesCreationAnalyticHelper
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on October 17, 2023
 */
class StoriesCreationAnalyticSenderImpl @Inject constructor(
    private val userSession: UserSessionInterface
) : StoriesCreationAnalyticSender {

    private val currentSite: String
        get() = if (GlobalConfig.isSellerApp()) {
            CurrentSite.tokopediaSeller
        } else {
            CurrentSite.tokopediaMarketplace
        }

    private val sessionIris: String
        get() = TrackApp.getInstance().gtm.irisSessionId

    override fun sendGeneralOpenScreen(
        screenName: String,
        trackerId: String,
    ) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(
            screenName,
            mapOf(
                Key.trackerId to trackerId,
                Key.businessUnit to BusinessUnit.content,
                Key.currentSite to currentSite,
                Key.sessionIris to sessionIris,
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
                .setEventCategory(EventCategory.storyCreation)
                .setEventAction(eventAction)
                .setEventLabel(StoriesCreationAnalyticHelper.getEventLabelByAccount(account))
                .setCustomProperty(Key.trackerId, trackerId)
        )
    }

    override fun sendGeneralViewEvent(eventAction: String, eventLabel: String, trackerId: String) {
        sendGeneralEvent(
            Tracker.Builder()
                .setEvent(Event.viewContentIris)
                .setEventCategory(EventCategory.storyCreation)
                .setEventAction(eventAction)
                .setEventLabel(eventLabel)
                .setCustomProperty(Key.trackerId, trackerId)
        )
    }

    override fun sendGeneralViewEventContent(
        eventAction: String,
        eventLabel: String,
        trackerId: String
    ) {
        sendGeneralEventContent(
            Tracker.Builder()
                .setEvent(Event.viewContentIris)
                .setEventCategory(EventCategory.storyCreation)
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
                .setEventCategory(EventCategory.storyCreation)
                .setEventAction(eventAction)
                .setEventLabel(StoriesCreationAnalyticHelper.getEventLabelByAccount(account))
                .setCustomProperty(Key.trackerId, trackerId)
        )
    }

    override fun sendGeneralClickEvent(
        eventAction: String,
        eventLabel: String,
        trackerId: String,
    ) {
        sendGeneralEvent(
            Tracker.Builder()
                .setEvent(Event.clickContent)
                .setEventCategory(EventCategory.storyCreation)
                .setEventAction(eventAction)
                .setEventLabel(eventLabel)
                .setCustomProperty(Key.trackerId, trackerId)
        )
    }

    override fun sendGeneralClickEventContent(
        eventAction: String,
        eventLabel: String,
        trackerId: String
    ) {
        sendGeneralEventContent(
            Tracker.Builder()
                .setEvent(Event.clickContent)
                .setEventCategory(EventCategory.storyCreation)
                .setEventAction(eventAction)
                .setEventLabel(eventLabel)
                .setCustomProperty(Key.trackerId, trackerId)
        )
    }

    private fun sendGeneralEvent(
        trackerBuilder: Tracker.Builder
    ) {
        trackerBuilder
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite(currentSite)
            .setUserId(userSession.userId)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .build()
            .send()
    }

    private fun sendGeneralEventContent(
        trackerBuilder: Tracker.Builder
    ) {
        trackerBuilder
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite(currentSite)
            .setUserId(userSession.userId)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .build()
            .send()
    }
}
