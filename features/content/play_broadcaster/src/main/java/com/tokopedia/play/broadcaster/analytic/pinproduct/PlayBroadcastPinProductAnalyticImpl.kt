package com.tokopedia.play.broadcaster.analytic.pinproduct

import com.tokopedia.content.analytic.BusinessUnit
import com.tokopedia.content.analytic.Event
import com.tokopedia.content.analytic.Key
import com.tokopedia.play.broadcaster.analytic.*
import com.tokopedia.play.broadcaster.analytic.KEY_TRACK_CATEGORY_PLAY
import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by fachrizalmrsln on 01/08/22.
 */
class PlayBroadcastPinProductAnalyticImpl @Inject constructor(
    userSession: UserSessionInterface,
    configStore: HydraConfigStore
) : PlayBroadcastPinProductAnalytic {

    private val partnerId = userSession.shopId
    private val userId = userSession.userId
    private val channelId by lazy(LazyThreadSafetyMode.NONE) { configStore.getChannelId() }

    override fun onClickPinProductLiveRoom(productId: String) {
        sendClickContent(
            eventAction = "click - pin product live room",
            eventLabel = "$partnerId - $channelId - $productId - $KEY_TRACK_CATEGORY_SELLER"
        )
    }

    override fun onClickPinProductBottomSheet(productId: String) {
        sendClickContent(
            eventAction = "click - pin product bottom sheet",
            eventLabel = "$partnerId - $channelId - $productId - $KEY_TRACK_CATEGORY_SELLER"
        )
    }

    override fun onImpressPinProductLiveRoom(productId: String) {
        sendImpressionContent(
            eventAction = "view - pin product live room",
            eventLabel = "$partnerId - $channelId - $productId - $KEY_TRACK_CATEGORY_SELLER"
        )
    }

    override fun onImpressPinProductBottomSheet(productId: String) {
        sendImpressionContent(
            eventAction = "view - pin product bottom sheet",
            eventLabel = "$partnerId - $channelId - $productId - $KEY_TRACK_CATEGORY_SELLER"
        )
    }

    override fun onImpressFailPinProductLiveRoom() {
        sendImpressionContent(
            eventAction = "view - fail pin product live room",
            eventLabel = "$partnerId - $channelId - $KEY_TRACK_CATEGORY_SELLER"
        )
    }

    override fun onImpressFailPinProductBottomSheet() {
        sendImpressionContent(
            eventAction = "view - fail pin product bottom sheet",
            eventLabel = "$partnerId - $channelId - $KEY_TRACK_CATEGORY_SELLER"
        )
    }

    override fun onImpressFailUnPinProductLiveRoom() {
        sendImpressionContent(
            eventAction = "view - fail un-pin product live room",
            eventLabel = "$partnerId - $channelId - $KEY_TRACK_CATEGORY_SELLER"
        )
    }

    override fun onImpressFailUnPinProductBottomSheet() {
        sendImpressionContent(
            eventAction = "view - fail un-pin product bottom sheet",
            eventLabel = "$partnerId - $channelId - $KEY_TRACK_CATEGORY_SELLER"
        )
    }

    override fun onImpressColdDownPinProductSecondEvent(isLiveRoom: Boolean) {
        val screen = if (isLiveRoom) "product carousel" else "bottom sheet"
        sendImpressionContent(
            eventAction = "view - cold down pin product 5 second",
            eventLabel = "$partnerId - $screen - $KEY_TRACK_CATEGORY_SELLER"
        )
    }

    private fun sendClickContent(
        eventAction: String,
        eventLabel: String
    ) {
        sendEvent(eventAction, eventLabel, Event.clickContent)
    }

    private fun sendImpressionContent(
        eventAction: String,
        eventLabel: String
    ) {
        sendEvent(eventAction, eventLabel, Event.viewContentIris)
    }

    private fun sendEvent(
        eventAction: String,
        eventLabel: String,
        eventKey: String
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                Key.event to eventKey,
                Key.eventAction to eventAction,
                Key.eventCategory to KEY_TRACK_CATEGORY_PLAY,
                Key.eventLabel to eventLabel,
                Key.businessUnit to BusinessUnit.play,
                Key.currentSite to currentSite,
                Key.userId to userId
            )
        )
    }
}
