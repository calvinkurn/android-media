package com.tokopedia.play.analytic.kebab

import com.tokopedia.play.analytic.*
import com.tokopedia.play.analytic.KEY_TRACK_BUSINESS_UNIT
import com.tokopedia.play.analytic.KEY_TRACK_GROUP_CHAT_ROOM
import com.tokopedia.play.analytic.KEY_TRACK_TRACKER_ID
import com.tokopedia.play.analytic.KEY_TRACK_VIEW_CONTENT_IRIS
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
    private val userSession: UserSessionInterface,
) : PlayKebabAnalytic {

    @AssistedFactory
    interface Factory : PlayKebabAnalytic.Factory {
        override fun create(
            channelInfo: PlayChannelInfoUiModel,
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
            .setEvent(KEY_TRACK_VIEW_CONTENT_IRIS)
            .setEventAction("impression - three dots")
            .setEventCategory(KEY_TRACK_GROUP_CHAT_ROOM)
            .setEventLabel("$channelId - $channelType")
            .setCustomProperty(KEY_TRACK_TRACKER_ID, "39865")
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(KEY_TRACK_CURRENT_SITE)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun impressPiP() {
        Tracker.Builder()
            .setEvent(KEY_TRACK_VIEW_CONTENT_IRIS)
            .setEventAction("impression - pip button")
            .setEventCategory(KEY_TRACK_GROUP_CHAT_ROOM)
            .setEventLabel("$channelId - $channelType")
            .setCustomProperty(KEY_TRACK_TRACKER_ID, "39867")
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(KEY_TRACK_CURRENT_SITE)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun impressChromecast() {
        Tracker.Builder()
            .setEvent(KEY_TRACK_VIEW_CONTENT_IRIS)
            .setEventAction("impression - chromecast button")
            .setEventCategory(KEY_TRACK_GROUP_CHAT_ROOM)
            .setEventLabel("$channelId - $channelType")
            .setCustomProperty(KEY_TRACK_TRACKER_ID, "39868")
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(KEY_TRACK_CURRENT_SITE)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun impressWatchMode() {
        Tracker.Builder()
            .setEvent(KEY_TRACK_VIEW_CONTENT_IRIS)
            .setEventAction("impression - mode nonton button")
            .setEventCategory(KEY_TRACK_GROUP_CHAT_ROOM)
            .setEventLabel("$channelId - $channelType")
            .setCustomProperty(KEY_TRACK_TRACKER_ID, "39869")
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(KEY_TRACK_CURRENT_SITE)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun impressUserReport() {
        Tracker.Builder()
            .setEvent(KEY_TRACK_VIEW_CONTENT_IRIS)
            .setEventAction("impression - laporkan video")
            .setEventCategory(KEY_TRACK_GROUP_CHAT_ROOM)
            .setEventLabel("$channelId - $channelType")
            .setCustomProperty(KEY_TRACK_TRACKER_ID, "39870")
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(KEY_TRACK_CURRENT_SITE)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickPiP() {
        Tracker.Builder()
            .setEvent(KEY_TRACK_CLICK_CONTENT)
            .setEventAction("click - picture in picture")
            .setEventCategory(KEY_TRACK_GROUP_CHAT_ROOM)
            .setEventLabel("$channelId - $channelType")
            .setCustomProperty(KEY_TRACK_TRACKER_ID, "39871")
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(KEY_TRACK_CURRENT_SITE)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickChromecast() {
        Tracker.Builder()
            .setEvent(KEY_TRACK_CLICK_CONTENT)
            .setEventAction("click - chromecast button")
            .setEventCategory(KEY_TRACK_GROUP_CHAT_ROOM)
            .setEventLabel("$channelId - $channelType")
            .setCustomProperty(KEY_TRACK_TRACKER_ID, "39872")
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(KEY_TRACK_CURRENT_SITE)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickWatchMode() {
        Tracker.Builder()
            .setEvent(KEY_TRACK_CLICK_CONTENT)
            .setEventAction("click - mode nonton button")
            .setEventCategory(KEY_TRACK_GROUP_CHAT_ROOM)
            .setEventLabel("$channelId - $channelType")
            .setCustomProperty(KEY_TRACK_TRACKER_ID, "39873")
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(KEY_TRACK_CURRENT_SITE)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }
}
