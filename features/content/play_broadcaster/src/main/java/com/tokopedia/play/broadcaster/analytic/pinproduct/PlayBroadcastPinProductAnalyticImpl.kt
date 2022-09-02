package com.tokopedia.play.broadcaster.analytic.pinproduct

import com.tokopedia.play.broadcaster.analytic.*
import com.tokopedia.play.broadcaster.analytic.KEY_BUSINESS_UNIT
import com.tokopedia.play.broadcaster.analytic.KEY_CURRENT_SITE
import com.tokopedia.play.broadcaster.analytic.KEY_EVENT
import com.tokopedia.play.broadcaster.analytic.KEY_EVENT_ACTION
import com.tokopedia.play.broadcaster.analytic.KEY_EVENT_CATEGORY
import com.tokopedia.play.broadcaster.analytic.KEY_EVENT_LABEL
import com.tokopedia.play.broadcaster.analytic.KEY_TRACK_BUSINESS_UNIT
import com.tokopedia.play.broadcaster.analytic.KEY_TRACK_CATEGORY_PLAY
import com.tokopedia.play.broadcaster.analytic.KEY_TRACK_CLICK_EVENT_SELLER
import com.tokopedia.play.broadcaster.analytic.KEY_TRACK_VIEW_EVENT_SELLER
import com.tokopedia.play.broadcaster.analytic.KEY_USER_ID
import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by fachrizalmrsln on 01/08/22.
 */
class PlayBroadcastPinProductAnalyticImpl @Inject constructor(
    userSession: UserSessionInterface,
    configStore: HydraConfigStore,
) : PlayBroadcastPinProductAnalytic {

    private val partnerId = userSession.shopId
    private val userId = userSession.userId
    private val channelId by lazy (LazyThreadSafetyMode.NONE){ configStore.getChannelId() }

    override fun onClickPinProductLiveRoom(productId: String) {
        sendClickContent(
            eventAction = "click - pin product live room",
            eventLabel = "$partnerId - $channelId - $productId - $KEY_TRACK_CATEGORY_SELLER",
        )
    }

    override fun onClickPinProductBottomSheet(productId: String) {
        sendClickContent(
            eventAction = "click - pin product bottom sheet",
            eventLabel = "$partnerId - $channelId - $productId - $KEY_TRACK_CATEGORY_SELLER",
        )
    }

    override fun onImpressPinProductLiveRoom(productId: String) {
        sendImpressionContent(
            eventAction = "view - pin product live room",
            eventLabel = "$partnerId - $channelId - $productId - $KEY_TRACK_CATEGORY_SELLER",
        )
    }

    override fun onImpressPinProductBottomSheet(productId: String) {
        sendImpressionContent(
            eventAction = "view - pin product bottom sheet",
            eventLabel = "$partnerId - $channelId - $productId - $KEY_TRACK_CATEGORY_SELLER",
        )
    }

    override fun onImpressFailPinProductLiveRoom() {
        sendImpressionContent(
            eventAction = "view - fail pin product live room",
            eventLabel = "$partnerId - $channelId - $KEY_TRACK_CATEGORY_SELLER",
        )
    }

    override fun onImpressFailPinProductBottomSheet() {
        sendImpressionContent(
            eventAction = "view - fail pin product bottom sheet",
            eventLabel = "$partnerId - $channelId - $KEY_TRACK_CATEGORY_SELLER",
        )
    }

    override fun onImpressFailUnPinProductLiveRoom() {
        sendImpressionContent(
            eventAction = "view - fail un-pin product live room",
            eventLabel = "$partnerId - $channelId - $KEY_TRACK_CATEGORY_SELLER",
        )
    }

    override fun onImpressFailUnPinProductBottomSheet() {
        sendImpressionContent(
            eventAction = "view - fail un-pin product bottom sheet",
            eventLabel = "$partnerId - $channelId - $KEY_TRACK_CATEGORY_SELLER",
        )
    }

    override fun onImpressColdDownPinProductSecondEvent(isLiveRoom: Boolean) {
        val screen = if (isLiveRoom) "product carousel" else "bottom sheet"
        sendImpressionContent(
            eventAction = "view - cold down pin product 5 second",
            eventLabel = "$partnerId - $screen - $KEY_TRACK_CATEGORY_SELLER",
        )
    }

    private fun sendClickContent(
        eventAction: String,
        eventLabel: String,
    ) {
        sendEvent(eventAction, eventLabel, KEY_TRACK_CLICK_EVENT_SELLER)
    }

    private fun sendImpressionContent(
        eventAction: String,
        eventLabel: String,
    ) {
        sendEvent(eventAction, eventLabel, KEY_TRACK_VIEW_EVENT_SELLER)
    }

    private fun sendEvent(
        eventAction: String,
        eventLabel: String,
        eventKey: String,
    ) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf(
                KEY_EVENT to eventKey,
                KEY_EVENT_ACTION to eventAction,
                KEY_EVENT_CATEGORY to KEY_TRACK_CATEGORY_PLAY,
                KEY_EVENT_LABEL to eventLabel,
                KEY_BUSINESS_UNIT to KEY_TRACK_BUSINESS_UNIT,
                KEY_CURRENT_SITE to currentSite,
                KEY_USER_ID to userId
            )
        )
    }
}