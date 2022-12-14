package com.tokopedia.play.analytic.popup

import com.tokopedia.play.analytic.*
import com.tokopedia.play.analytic.KEY_SESSION_IRIS
import com.tokopedia.play.analytic.KEY_TRACK_BUSINESS_UNIT
import com.tokopedia.play.analytic.KEY_TRACK_CURRENT_SITE
import com.tokopedia.play.analytic.KEY_TRACK_GROUP_CHAT_ROOM
import com.tokopedia.play.analytic.KEY_TRACK_VIEW_CONTENT_IRIS
import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by astidhiyaa on 15/11/22
 */
class PlayFollowPopupAnalyticImpl @Inject constructor(
    private val userSession: UserSessionInterface,
) : PlayFollowPopupAnalytic {

    private val userId: String
        get() = userSession.userId

    private val sessionIris: String
        get() = TrackApp.getInstance().gtm.irisSessionId

    /**
     * Tracking purpose
     */
    private val String.convertPartnerType : String
        get() {
            return if (this == PartnerType.Buyer.value) "user" else this
        }

    override fun impressFollowPopUp(
        channelId: String,
        channelType: String,
        partnerType: String,
        partnerId: String
    ) {
        Tracker.Builder()
            .setEvent(KEY_TRACK_VIEW_CONTENT_IRIS)
            .setEventAction("impression - follow pop up")
            .setEventCategory(KEY_TRACK_GROUP_CHAT_ROOM)
            .setEventLabel("$channelId - $channelType - ${partnerType.convertPartnerType} - $partnerId")
            .setCustomProperty(KEY_TRACK_TRACKER_ID, "38068")
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(KEY_TRACK_CURRENT_SITE)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickDismissFollowPopUp(
        channelId: String,
        channelType: String,
        partnerType: String,
        partnerId: String
    ) {
        Tracker.Builder()
            .setEvent(KEY_TRACK_CLICK_CONTENT)
            .setEventAction("click - dismiss follow pop up")
            .setEventCategory(KEY_TRACK_GROUP_CHAT_ROOM)
            .setEventLabel("$channelId - $channelType - ${partnerType.convertPartnerType} - $partnerId")
            .setCustomProperty(KEY_TRACK_TRACKER_ID, "38069")
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(KEY_TRACK_CURRENT_SITE)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickFollowCreatorPopUp(
        channelId: String,
        channelType: String,
        partnerType: String,
        partnerId: String
    ) {
        Tracker.Builder()
            .setEvent(KEY_TRACK_CLICK_CONTENT)
            .setEventAction("click - follow creator pop up")
            .setEventCategory(KEY_TRACK_GROUP_CHAT_ROOM)
            .setEventLabel("$channelId - $channelType - ${partnerType.convertPartnerType} - $partnerId")
            .setCustomProperty(KEY_TRACK_TRACKER_ID, "38070")
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(KEY_TRACK_CURRENT_SITE)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickCreatorPopUp(
        channelId: String,
        channelType: String,
        partnerType: String,
        partnerId: String
    ) {
        Tracker.Builder()
            .setEvent(KEY_TRACK_CLICK_CONTENT)
            .setEventAction("click - creator name follow pop up")
            .setEventCategory(KEY_TRACK_GROUP_CHAT_ROOM)
            .setEventLabel("$channelId - $channelType - ${partnerType.convertPartnerType} - $partnerId")
            .setCustomProperty(KEY_TRACK_TRACKER_ID, "38071")
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(KEY_TRACK_CURRENT_SITE)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun impressToasterPopUp(channelId: String, channelType: String, isSuccess: Boolean) {
        val (eventAction, trackerId) = if (isSuccess) Pair(
            "impression - success follow toaster",
            "38072"
        )
        else Pair("impression - fail follow toaster", "38073")

        Tracker.Builder()
            .setEvent(KEY_TRACK_VIEW_CONTENT_IRIS)
            .setEventAction(eventAction)
            .setEventCategory(KEY_TRACK_GROUP_CHAT_ROOM)
            .setEventLabel("$channelId - $channelType")
            .setCustomProperty(KEY_TRACK_TRACKER_ID, trackerId)
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(KEY_TRACK_CURRENT_SITE)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickRetryToasterPopUp(channelId: String, channelType: String) {
        Tracker.Builder()
            .setEvent(KEY_TRACK_CLICK_CONTENT)
            .setEventAction("click - coba lagi fail toaster")
            .setEventCategory(KEY_TRACK_GROUP_CHAT_ROOM)
            .setEventLabel("$channelId - $channelType")
            .setCustomProperty(KEY_TRACK_TRACKER_ID, "38075")
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(KEY_TRACK_CURRENT_SITE)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }
}
