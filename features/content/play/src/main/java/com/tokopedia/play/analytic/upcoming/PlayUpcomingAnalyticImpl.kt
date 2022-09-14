package com.tokopedia.play.analytic.upcoming

import com.tokopedia.play.analytic.*
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 14, 2021
 */
class PlayUpcomingAnalyticImpl @Inject constructor(
    private val userSession: UserSessionInterface,
) : PlayUpcomingAnalytic {

    private val userId: String
        get() = userSession.userId

    override fun impressUpcomingPage(channelId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_VIEW_UPCOMING_IRIS,
                KEY_EVENT_ACTION to "impression on upcoming page",
                KEY_EVENT_CATEGORY to KEY_TRACK_UPCOMING_PAGE,
                KEY_EVENT_LABEL to channelId,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                KEY_USER_ID to userId
            )
        )
    }

    override fun clickRemindMe(channelId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_CLICK_UPCOMING,
                KEY_EVENT_ACTION to "click on ingatkan saya",
                KEY_EVENT_CATEGORY to KEY_TRACK_UPCOMING_PAGE,
                KEY_EVENT_LABEL to channelId,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                KEY_USER_ID to userId
            )
        )
    }

    override fun clickWatchNow(channelId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to KEY_TRACK_CLICK_UPCOMING,
                KEY_EVENT_ACTION to "click on tonton sekarang",
                KEY_EVENT_CATEGORY to KEY_TRACK_UPCOMING_PAGE,
                KEY_EVENT_LABEL to channelId,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_CURRENT_SITE to KEY_TRACK_CURRENT_SITE,
                KEY_SESSION_IRIS to TrackApp.getInstance().gtm.irisSessionId,
                KEY_USER_ID to userId
            )
        )
    }


    override fun clickCancelRemindMe(channelId: String) {
        Tracker.Builder()
            .setEvent(KEY_TRACK_CLICK_TOP_ADS)
            .setEventAction("click - batalkan pengingat")
            .setEventCategory(KEY_TRACK_UPCOMING_PAGE)
            .setEventLabel(channelId)
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(KEY_TRACK_CURRENT_SITE)
            .build()
            .send()
    }

    override fun impressDescription(channelId: String) {
        Tracker.Builder()
            .setEvent(KEY_TRACK_VIEW_TOP_ADS)
            .setEventAction("impression - description")
            .setEventCategory(KEY_TRACK_UPCOMING_PAGE)
            .setEventLabel(channelId)
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(KEY_TRACK_CURRENT_SITE)
            .build()
            .send()
    }

    override fun clickSeeAllDescription(channelId: String) {
        Tracker.Builder()
            .setEvent(KEY_TRACK_CLICK_TOP_ADS)
            .setEventAction("click - lihat semua button")
            .setEventCategory(KEY_TRACK_UPCOMING_PAGE)
            .setEventLabel(channelId)
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(KEY_TRACK_CURRENT_SITE)
            .build()
            .send()
    }

    override fun clickSeeLessDescription(channelId: String) {
        Tracker.Builder()
            .setEvent(KEY_TRACK_CLICK_TOP_ADS)
            .setEventAction("click - tampilkan sedikit button")
            .setEventCategory(KEY_TRACK_UPCOMING_PAGE)
            .setEventLabel(channelId)
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(KEY_TRACK_CURRENT_SITE)
            .build()
            .send()
    }

    override fun clickCover(channelId: String) {
        Tracker.Builder()
            .setEvent(KEY_TRACK_CLICK_TOP_ADS)
            .setEventAction("tap - live room")
            .setEventCategory(KEY_TRACK_UPCOMING_PAGE)
            .setEventLabel(channelId)
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(KEY_TRACK_CURRENT_SITE)
            .build()
            .send()
    }

    override fun impressCoverWithoutComponent(channelId: String) {
        Tracker.Builder()
            .setEvent(KEY_TRACK_VIEW_TOP_ADS)
            .setEventAction("impression - cover image")
            .setEventCategory(KEY_TRACK_UPCOMING_PAGE)
            .setEventLabel(channelId)
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(KEY_TRACK_CURRENT_SITE)
            .build()
            .send()
    }

    override fun impressShare(channelId: String) {
        Tracker.Builder()
            .setEvent(KEY_TRACK_VIEW_TOP_ADS)
            .setEventAction("impression - share button")
            .setEventCategory(KEY_TRACK_UPCOMING_PAGE)
            .setEventLabel(channelId)
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(KEY_TRACK_CURRENT_SITE)
            .build()
            .send()
    }
}