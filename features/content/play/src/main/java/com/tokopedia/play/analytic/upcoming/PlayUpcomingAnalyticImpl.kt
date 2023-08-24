package com.tokopedia.play.analytic.upcoming

import com.tokopedia.content.analytic.BusinessUnit
import com.tokopedia.content.analytic.CurrentSite
import com.tokopedia.content.analytic.Event
import com.tokopedia.content.analytic.Key
import com.tokopedia.play.analytic.*
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 14, 2021
 */
class PlayUpcomingAnalyticImpl @Inject constructor(
    private val userSession: UserSessionInterface
) : PlayUpcomingAnalytic {

    private val userId: String
        get() = userSession.userId

    override fun impressUpcomingPage(channelId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                Key.event to Event.viewUpcomingIris,
                Key.eventAction to "impression on upcoming page",
                Key.eventCategory to KEY_TRACK_UPCOMING_PAGE,
                Key.eventLabel to channelId,
                Key.businessUnit to BusinessUnit.play,
                Key.currentSite to CurrentSite.tokopediaMarketplace,
                Key.sessionIris to TrackApp.getInstance().gtm.irisSessionId,
                Key.userId to userId
            )
        )
    }

    override fun clickRemindMe(channelId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                Key.event to Event.clickUpcoming,
                Key.eventAction to "click on ingatkan saya",
                Key.eventCategory to KEY_TRACK_UPCOMING_PAGE,
                Key.eventLabel to channelId,
                Key.businessUnit to BusinessUnit.play,
                Key.currentSite to CurrentSite.tokopediaMarketplace,
                Key.sessionIris to TrackApp.getInstance().gtm.irisSessionId,
                Key.userId to userId
            )
        )
    }

    override fun clickWatchNow(channelId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                Key.event to Event.clickUpcoming,
                Key.eventAction to "click on tonton sekarang",
                Key.eventCategory to KEY_TRACK_UPCOMING_PAGE,
                Key.eventLabel to channelId,
                Key.businessUnit to BusinessUnit.play,
                Key.currentSite to CurrentSite.tokopediaMarketplace,
                Key.sessionIris to TrackApp.getInstance().gtm.irisSessionId,
                Key.userId to userId
            )
        )
    }

    override fun clickCancelRemindMe(channelId: String) {
        Tracker.Builder()
            .setEvent(Event.clickTopAds)
            .setEventAction("click - batalkan pengingat")
            .setEventCategory(KEY_TRACK_UPCOMING_PAGE)
            .setEventLabel(channelId)
            .setBusinessUnit(BusinessUnit.play)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .build()
            .send()
    }

    override fun impressDescription(channelId: String) {
        Tracker.Builder()
            .setEvent(Event.viewTopAdsIris)
            .setEventAction("impression - description")
            .setEventCategory(KEY_TRACK_UPCOMING_PAGE)
            .setEventLabel(channelId)
            .setBusinessUnit(BusinessUnit.play)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .build()
            .send()
    }

    override fun clickSeeAllDescription(channelId: String) {
        Tracker.Builder()
            .setEvent(Event.clickTopAds)
            .setEventAction("click - lihat semua button")
            .setEventCategory(KEY_TRACK_UPCOMING_PAGE)
            .setEventLabel(channelId)
            .setBusinessUnit(BusinessUnit.play)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .build()
            .send()
    }

    override fun clickSeeLessDescription(channelId: String) {
        Tracker.Builder()
            .setEvent(Event.clickTopAds)
            .setEventAction("click - tampilkan sedikit button")
            .setEventCategory(KEY_TRACK_UPCOMING_PAGE)
            .setEventLabel(channelId)
            .setBusinessUnit(BusinessUnit.play)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .build()
            .send()
    }

    override fun clickCover(channelId: String) {
        Tracker.Builder()
            .setEvent(Event.clickTopAds)
            .setEventAction("tap - live room")
            .setEventCategory(KEY_TRACK_UPCOMING_PAGE)
            .setEventLabel(channelId)
            .setBusinessUnit(BusinessUnit.play)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .build()
            .send()
    }

    override fun impressCoverWithoutComponent(channelId: String) {
        Tracker.Builder()
            .setEvent(Event.viewTopAdsIris)
            .setEventAction("impression - cover image")
            .setEventCategory(KEY_TRACK_UPCOMING_PAGE)
            .setEventLabel(channelId)
            .setBusinessUnit(BusinessUnit.play)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .build()
            .send()
    }

    override fun impressShare(channelId: String) {
        Tracker.Builder()
            .setEvent(Event.viewTopAdsIris)
            .setEventAction("impression - share button")
            .setEventCategory(KEY_TRACK_UPCOMING_PAGE)
            .setEventLabel(channelId)
            .setBusinessUnit(BusinessUnit.play)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .build()
            .send()
    }
}
