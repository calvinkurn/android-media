package com.tokopedia.play.analytic.kebab

import com.tokopedia.content.analytic.BusinessUnit
import com.tokopedia.content.analytic.CurrentSite
import com.tokopedia.content.analytic.Event
import com.tokopedia.content.analytic.EventCategory
import com.tokopedia.content.analytic.Key
import com.tokopedia.play.analytic.*
import com.tokopedia.play.view.uimodel.recom.PlayChannelInfoUiModel
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 * @author by astidhiyaa on 03/01/23
 */
class PlayKebabAnalyticImpl @AssistedInject constructor(
    @Assisted private val channelInfo: PlayChannelInfoUiModel,
    private val userSession: UserSessionInterface
) : PlayKebabAnalytic {

    @AssistedFactory
    interface Factory : PlayKebabAnalytic.Factory {
        override fun create(
            channelInfo: PlayChannelInfoUiModel
        ): PlayKebabAnalyticImpl
    }

    private val channelId: String
        get() = channelInfo.id

    private val channelType: String
        get() = channelInfo.channelType.value

    private val userId: String
        get() = if (userSession.isLoggedIn) userSession.userId else "0"

    private val sessionIris: String
        get() = TrackApp.getInstance().gtm.irisSessionId

    override fun impressKebab() {
        Tracker.Builder()
            .setEvent(Event.viewContentIris)
            .setEventAction("impression - three dots")
            .setEventCategory(EventCategory.groupChatRoom)
            .setEventLabel("$channelId - $channelType")
            .setCustomProperty(Key.trackerId, "39865")
            .setBusinessUnit(BusinessUnit.play)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun impressPiP() {
        Tracker.Builder()
            .setEvent(Event.viewContentIris)
            .setEventAction("impression - pip button")
            .setEventCategory(EventCategory.groupChatRoom)
            .setEventLabel("$channelId - $channelType")
            .setCustomProperty(Key.trackerId, "39867")
            .setBusinessUnit(BusinessUnit.play)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun impressChromecast() {
        Tracker.Builder()
            .setEvent(Event.viewContentIris)
            .setEventAction("impression - chromecast button")
            .setEventCategory(EventCategory.groupChatRoom)
            .setEventLabel("$channelId - $channelType")
            .setCustomProperty(Key.trackerId, "39868")
            .setBusinessUnit(BusinessUnit.play)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun impressWatchMode() {
        Tracker.Builder()
            .setEvent(Event.viewContentIris)
            .setEventAction("impression - mode nonton button")
            .setEventCategory(EventCategory.groupChatRoom)
            .setEventLabel("$channelId - $channelType")
            .setCustomProperty(Key.trackerId, "39869")
            .setBusinessUnit(BusinessUnit.play)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun impressUserReport() {
        Tracker.Builder()
            .setEvent(Event.viewContentIris)
            .setEventAction("impression - laporkan video")
            .setEventCategory(EventCategory.groupChatRoom)
            .setEventLabel("$channelId - $channelType")
            .setCustomProperty(Key.trackerId, "39870")
            .setBusinessUnit(BusinessUnit.play)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickPiP() {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - picture in picture")
            .setEventCategory(EventCategory.groupChatRoom)
            .setEventLabel("$channelId - $channelType")
            .setCustomProperty(Key.trackerId, "39871")
            .setBusinessUnit(BusinessUnit.play)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickChromecast() {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - chromecast button")
            .setEventCategory(EventCategory.groupChatRoom)
            .setEventLabel("$channelId - $channelType")
            .setCustomProperty(Key.trackerId, "39872")
            .setBusinessUnit(BusinessUnit.play)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickWatchMode() {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - mode nonton button")
            .setEventCategory(EventCategory.groupChatRoom)
            .setEventLabel("$channelId - $channelType")
            .setCustomProperty(Key.trackerId, "39873")
            .setBusinessUnit(BusinessUnit.play)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }
}
