package com.tokopedia.play.analytic.popup

import com.tokopedia.content.analytic.BusinessUnit
import com.tokopedia.content.analytic.CurrentSite
import com.tokopedia.content.analytic.Event
import com.tokopedia.content.analytic.EventCategory
import com.tokopedia.content.analytic.Key
import com.tokopedia.play.widget.ui.model.PartnerType
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by astidhiyaa on 15/11/22
 */
class PlayFollowPopupAnalyticImpl @Inject constructor(
    private val userSession: UserSessionInterface
) : PlayFollowPopupAnalytic {

    private val userId: String
        get() = userSession.userId

    private val sessionIris: String
        get() = TrackApp.getInstance().gtm.irisSessionId

    /**
     * Tracking purpose
     */
    private val String.convertPartnerType: String
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
            .setEvent(Event.viewContentIris)
            .setEventAction("impression - follow pop up")
            .setEventCategory(EventCategory.groupChatRoom)
            .setEventLabel("$channelId - $channelType - ${partnerType.convertPartnerType} - $partnerId")
            .setCustomProperty(Key.trackerId, "38068")
            .setBusinessUnit(BusinessUnit.play)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .setCustomProperty(Key.sessionIris, sessionIris)
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
            .setEvent(Event.clickContent)
            .setEventAction("click - dismiss follow pop up")
            .setEventCategory(EventCategory.groupChatRoom)
            .setEventLabel("$channelId - $channelType - ${partnerType.convertPartnerType} - $partnerId")
            .setCustomProperty(Key.trackerId, "38069")
            .setBusinessUnit(BusinessUnit.play)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .setCustomProperty(Key.sessionIris, sessionIris)
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
            .setEvent(Event.clickContent)
            .setEventAction("click - follow creator pop up")
            .setEventCategory(EventCategory.groupChatRoom)
            .setEventLabel("$channelId - $channelType - ${partnerType.convertPartnerType} - $partnerId")
            .setCustomProperty(Key.trackerId, "38070")
            .setBusinessUnit(BusinessUnit.play)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .setCustomProperty(Key.sessionIris, sessionIris)
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
            .setEvent(Event.clickContent)
            .setEventAction("click - creator name follow pop up")
            .setEventCategory(EventCategory.groupChatRoom)
            .setEventLabel("$channelId - $channelType - ${partnerType.convertPartnerType} - $partnerId")
            .setCustomProperty(Key.trackerId, "38071")
            .setBusinessUnit(BusinessUnit.play)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun impressToasterPopUp(channelId: String, channelType: String, isSuccess: Boolean) {
        val (eventAction, trackerId) = if (isSuccess) {
            Pair(
                "impression - success follow toaster",
                "38072"
            )
        } else {
            Pair("impression - fail follow toaster", "38073")
        }

        Tracker.Builder()
            .setEvent(Event.viewContentIris)
            .setEventAction(eventAction)
            .setEventCategory(EventCategory.groupChatRoom)
            .setEventLabel("$channelId - $channelType")
            .setCustomProperty(Key.trackerId, trackerId)
            .setBusinessUnit(BusinessUnit.play)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickRetryToasterPopUp(channelId: String, channelType: String) {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - coba lagi fail toaster")
            .setEventCategory(EventCategory.groupChatRoom)
            .setEventLabel("$channelId - $channelType")
            .setCustomProperty(Key.trackerId, "38075")
            .setBusinessUnit(BusinessUnit.play)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }
}
