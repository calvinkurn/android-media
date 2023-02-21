package com.tokopedia.play.analytic.voucher

import com.tokopedia.play.analytic.*
import com.tokopedia.play.view.uimodel.PlayVoucherUiModel
import com.tokopedia.play.view.uimodel.recom.PlayChannelInfoUiModel
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.Tracker
import com.tokopedia.track.builder.util.BaseTrackerConst
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by astidhiyaa on 06/09/22
 */
class PlayVoucherAnalyticImpl @Inject constructor(
    private val userSession: UserSessionInterface,
    private val trackingQueue: TrackingQueue
) : PlayVoucherAnalytic {

    private var channelInfo = PlayChannelInfoUiModel()

    private val userId: String
        get() = if (userSession.isLoggedIn) userSession.userId else "0"

    private val sessionIris: String
        get() = TrackApp.getInstance().gtm.irisSessionId

    private val channelId: String
        get() = channelInfo.id

    private val channelType: String
        get() = channelInfo.channelType.value

    override fun setData(channelInfoUiModel: PlayChannelInfoUiModel) {
        channelInfo = channelInfoUiModel
    }

    override fun impressVoucherWidget(voucherId: String) {
        Tracker.Builder()
            .setEvent(KEY_TRACK_VIEW_CONTENT_IRIS)
            .setEventAction("view - voucher widget")
            .setEventCategory(KEY_TRACK_GROUP_CHAT_ROOM)
            .setEventLabel("$channelId - $voucherId - $channelType")
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(KEY_TRACK_CURRENT_SITE)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickVoucherWidget(voucherId: String) {
        Tracker.Builder()
            .setEvent(KEY_TRACK_CLICK_CONTENT)
            .setEventAction("click - voucher widget")
            .setEventCategory(KEY_TRACK_GROUP_CHAT_ROOM)
            .setEventLabel("$channelId - $voucherId - $channelType")
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(KEY_TRACK_CURRENT_SITE)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun impressVoucherBottomSheet(voucherId: String) {
        if (voucherId.isBlank()) return

        Tracker.Builder()
            .setEvent(KEY_TRACK_VIEW_CONTENT_IRIS)
            .setEventAction("view - voucher bottomsheet")
            .setEventCategory(KEY_TRACK_GROUP_CHAT_ROOM)
            .setEventLabel("$channelId - $voucherId - $channelType")
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(KEY_TRACK_CURRENT_SITE)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun impressToasterPrivate(voucherId: String) {
        Tracker.Builder()
            .setEvent(KEY_TRACK_VIEW_CONTENT_IRIS)
            .setEventAction("view - toaster private voucher")
            .setEventCategory(KEY_TRACK_GROUP_CHAT_ROOM)
            .setEventLabel("$channelId - $voucherId - $channelType")
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(KEY_TRACK_CURRENT_SITE)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickToasterPrivate(voucherId: String) {
        Tracker.Builder()
            .setEvent(KEY_TRACK_CLICK_CONTENT)
            .setEventAction("click - lihat toaster private voucher")
            .setEventCategory(KEY_TRACK_GROUP_CHAT_ROOM)
            .setEventLabel("$channelId - $voucherId - $channelType")
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(KEY_TRACK_CURRENT_SITE)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickToasterPublic() {
        Tracker.Builder()
            .setEvent(KEY_TRACK_CLICK_CONTENT)
            .setEventAction("click - toaster public voucher")
            .setEventCategory(KEY_TRACK_GROUP_CHAT_ROOM)
            .setEventLabel("$channelId - $channelType")
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(KEY_TRACK_CURRENT_SITE)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun impressToasterPublic() {
        Tracker.Builder()
            .setEvent(KEY_TRACK_VIEW_CONTENT_IRIS)
            .setEventAction("view - toaster public voucher")
            .setEventCategory(KEY_TRACK_GROUP_CHAT_ROOM)
            .setEventLabel("$channelId - $channelType")
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(KEY_TRACK_CURRENT_SITE)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun swipeWidget(voucherId: String) {
        Tracker.Builder()
            .setEvent(KEY_TRACK_CLICK_CONTENT)
            .setEventAction("swipe - voucher widget")
            .setEventCategory(KEY_TRACK_GROUP_CHAT_ROOM)
            .setEventLabel("$channelId - $voucherId - $channelType")
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(KEY_TRACK_CURRENT_SITE)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickCopyVoucher(voucherId: String) {
        Tracker.Builder()
            .setEvent(KEY_TRACK_CLICK_GROUP_CHAT)
            .setEventAction("click copy on private voucher")
            .setEventCategory(KEY_TRACK_GROUP_CHAT_ROOM)
            .setEventLabel("$channelId - $voucherId - $channelType")
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(KEY_TRACK_CURRENT_SITE)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setCustomProperty(KEY_IS_LOGGED_IN_STATUS, userSession.isLoggedIn)
            .setCustomProperty(KEY_CHANNEL, channelInfo.title)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickInfoVoucher() {
        Tracker.Builder()
            .setEvent(KEY_TRACK_CLICK_CONTENT)
            .setEventAction("click - entry point voucher bottomsheet")
            .setEventCategory(KEY_TRACK_GROUP_CHAT_ROOM)
            .setEventLabel("$channelId - $channelType")
            .setCustomProperty(KEY_TRACK_TRACKER_ID, "40976")
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(KEY_TRACK_CURRENT_SITE)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun impressInfoVoucher(voucher: PlayVoucherUiModel.Merchant) {
        val map = BaseTrackerBuilder().constructBasicPromotionView(
            event = KEY_TRACK_PROMO_VIEW,
            eventCategory = KEY_TRACK_GROUP_CHAT_ROOM,
            eventAction = "view - entry point voucher bottomsheet",
            eventLabel = "$channelId - $channelType",
            promotions = listOf(
                BaseTrackerConst.Promotion(
                    id = channelId,
                    name = "/play/explorewidget",
                    creative = voucher.title,
                    position = "1"
                )
            )
        )
            .appendUserId(userId)
            .appendBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .appendCurrentSite(KEY_TRACK_CURRENT_SITE)
            .appendCustomKeyValue(KEY_TRACK_TRACKER_ID, "40973")
            .build()

        trackingQueue.putEETracking(map as? HashMap<String, Any>)
    }
}
