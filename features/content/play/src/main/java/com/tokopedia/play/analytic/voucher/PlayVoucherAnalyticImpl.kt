package com.tokopedia.play.analytic.voucher

import com.tokopedia.content.analytic.BusinessUnit
import com.tokopedia.content.analytic.CurrentSite
import com.tokopedia.content.analytic.Event
import com.tokopedia.content.analytic.EventCategory
import com.tokopedia.content.analytic.Key
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
            .setEvent(Event.viewContentIris)
            .setEventAction("view - voucher widget")
            .setEventCategory(EventCategory.groupChatRoom)
            .setEventLabel("$channelId - $voucherId - $channelType")
            .setBusinessUnit(BusinessUnit.play)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickVoucherWidget(voucherId: String) {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - voucher widget")
            .setEventCategory(EventCategory.groupChatRoom)
            .setEventLabel("$channelId - $voucherId - $channelType")
            .setBusinessUnit(BusinessUnit.play)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun impressVoucherBottomSheet(voucherId: String) {
        if (voucherId.isBlank()) return

        Tracker.Builder()
            .setEvent(Event.viewContentIris)
            .setEventAction("view - voucher bottomsheet")
            .setEventCategory(EventCategory.groupChatRoom)
            .setEventLabel("$channelId - $voucherId - $channelType")
            .setBusinessUnit(BusinessUnit.play)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun impressToasterPrivate(voucherId: String) {
        Tracker.Builder()
            .setEvent(Event.viewContentIris)
            .setEventAction("view - toaster private voucher")
            .setEventCategory(EventCategory.groupChatRoom)
            .setEventLabel("$channelId - $voucherId - $channelType")
            .setBusinessUnit(BusinessUnit.play)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickToasterPrivate(voucherId: String) {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - lihat toaster private voucher")
            .setEventCategory(EventCategory.groupChatRoom)
            .setEventLabel("$channelId - $voucherId - $channelType")
            .setBusinessUnit(BusinessUnit.play)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickToasterPublic() {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - toaster public voucher")
            .setEventCategory(EventCategory.groupChatRoom)
            .setEventLabel("$channelId - $channelType")
            .setBusinessUnit(BusinessUnit.play)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun impressToasterPublic() {
        Tracker.Builder()
            .setEvent(Event.viewContentIris)
            .setEventAction("view - toaster public voucher")
            .setEventCategory(EventCategory.groupChatRoom)
            .setEventLabel("$channelId - $channelType")
            .setBusinessUnit(BusinessUnit.play)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun swipeWidget(voucherId: String) {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("swipe - voucher widget")
            .setEventCategory(EventCategory.groupChatRoom)
            .setEventLabel("$channelId - $voucherId - $channelType")
            .setBusinessUnit(BusinessUnit.play)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickCopyVoucher(voucherId: String) {
        Tracker.Builder()
            .setEvent(Event.clickGroupChat)
            .setEventAction("click copy on private voucher")
            .setEventCategory(EventCategory.groupChatRoom)
            .setEventLabel("$channelId - $voucherId - $channelType")
            .setBusinessUnit(BusinessUnit.play)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setCustomProperty(Key.isLoggedInStatus, userSession.isLoggedIn)
            .setCustomProperty(KEY_CHANNEL, channelInfo.title)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickInfoVoucher() {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - entry point voucher bottomsheet")
            .setEventCategory(EventCategory.groupChatRoom)
            .setEventLabel("$channelId - $channelType")
            .setCustomProperty(Key.trackerId, "40976")
            .setBusinessUnit(BusinessUnit.play)
            .setCurrentSite(CurrentSite.tokopediaMarketplace)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun impressInfoVoucher(voucher: PlayVoucherUiModel.Merchant) {
        val map = BaseTrackerBuilder().constructBasicPromotionView(
            event = Event.promoView,
            eventCategory = EventCategory.groupChatRoom,
            eventAction = "view - entry point voucher bottomsheet",
            eventLabel = "$channelId - $channelType",
            promotions = listOf(
                BaseTrackerConst.Promotion(
                    id = channelId,
                    name = "voucher bottomsheet",
                    creative = voucher.title,
                    position = "1"
                )
            )
        )
            .appendUserId(userId)
            .appendBusinessUnit(BusinessUnit.play)
            .appendCurrentSite(CurrentSite.tokopediaMarketplace)
            .appendCustomKeyValue(Key.trackerId, "40973")
            .build()

        trackingQueue.putEETracking(map as? HashMap<String, Any>)
    }
}
