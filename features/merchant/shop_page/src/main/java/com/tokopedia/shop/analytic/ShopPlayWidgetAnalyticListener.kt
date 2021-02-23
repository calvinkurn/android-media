package com.tokopedia.shop.analytic

import com.tokopedia.config.GlobalConfig
import com.tokopedia.play.widget.analytic.list.PlayWidgetInListAnalyticListener
import com.tokopedia.play.widget.ui.PlayWidgetMediumView
import com.tokopedia.play.widget.ui.PlayWidgetView
import com.tokopedia.play.widget.ui.model.PlayWidgetMediumBannerUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetMediumChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetMediumOverlayUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface

/**
 * Created by mzennis on 26/10/20.
 *
 * https://docs.google.com/spreadsheets/d/1l91ritx5rj-RJzcTNVXnMTcOp3sWZz6O2v__nfV64Co/edit#gid=1005486470 row 61-65
 * https://mynakama.tokopedia.com/datatracker/requestdetail/view/63
 * Save Live to VOD https://mynakama.tokopedia.com/datatracker/product/requestdetail/170
 */
class ShopPlayWidgetAnalyticListener(
        private val trackingQueue: TrackingQueue,
        private val userSession: UserSessionInterface
) : PlayWidgetInListAnalyticListener {

    var shopId: String = ""
    var widgetId: String = ""

    private val userId: String
        get() = userSession.userId
    private val shopName: String
        get() = userSession.shopName

    private val isPlayWidgetSellerApp: Boolean
        get() = GlobalConfig.isSellerApp()

    override fun onImpressPlayWidget(view: PlayWidgetView, item: PlayWidgetUiModel, verticalWidgetPosition: Int, businessWidgetPosition: Int) {
        if (!isPlayWidgetSellerApp) return
        val trackerMap = BaseTrackerBuilder().constructBasicPromotionView(
                event = PROMO_VIEW,
                eventCategory = SHOP_PAGE_SELLER,
                eventAction = "impression - widget play",
                eventLabel = "$shopId - $verticalWidgetPosition - $businessWidgetPosition",
                promotions = listOf(
                        BaseTrackerConst.Promotion(
                                id = widgetId,
                                name = "/shoppage play outside banner",
                                creative = shopName,
                                position = verticalWidgetPosition.toString()
                        )
                )
        )
                .appendUserId(userId)
                .appendBusinessUnit("play")
                .appendCurrentSite("tokopediaseller")
                .appendShopId(shopId)
                .build()

        if (trackerMap is HashMap<String, Any>) trackingQueue.putEETracking(trackerMap)
    }

    override fun onClickViewAll(view: PlayWidgetMediumView, verticalWidgetPosition: Int, businessWidgetPosition: Int,) {
        if (isPlayWidgetSellerApp) return
        TrackApp.getInstance().gtm.sendGeneralEvent(
                mapOf(
                        EVENT to CLICK_SHOP_PAGE,
                        EVENT_CATEGORY to SHOP_PAGE_BUYER,
                        EVENT_ACTION to "click view all play",
                        EVENT_LABEL to "$shopId - Tokopedia Play - $businessWidgetPosition",
                        BUSINESS_UNIT to "ads solution",
                        CURRENT_SITE to "tokopediamarketplace",
                        USER_ID to userId
                )
        )
    }

    override fun onImpressOverlayCard(
            view: PlayWidgetMediumView,
            item: PlayWidgetMediumOverlayUiModel,
            channelPositionInList: Int,
            verticalWidgetPosition: Int,
            businessWidgetPosition: Int,
    ) {
        if (isPlayWidgetSellerApp) return
        val trackerMap = BaseTrackerBuilder().constructBasicPromotionView(
                event = PROMO_VIEW,
                eventCategory = SHOP_PAGE_BUYER,
                eventAction = "impression on play sgc banner",
                eventLabel = "view on banner play - $shopId - $businessWidgetPosition",
                promotions = listOf(
                        BaseTrackerConst.Promotion(
                                id = widgetId,
                                name = "/shoppage play outside banner - p$channelPositionInList",
                                creative = item.imageUrl,
                                position = channelPositionInList.toString()
                        )
                )
        ).appendUserId(userId).build()

        if (trackerMap is HashMap<String, Any>) trackingQueue.putEETracking(trackerMap)
    }

    override fun onClickOverlayCard(
            view: PlayWidgetMediumView,
            item: PlayWidgetMediumOverlayUiModel,
            channelPositionInList: Int,
            verticalWidgetPosition: Int,
            businessWidgetPosition: Int,
    ) {
        if (isPlayWidgetSellerApp) return
        val trackerMap = BaseTrackerBuilder().constructBasicPromotionClick(
                event = PROMO_CLICK,
                eventCategory = SHOP_PAGE_BUYER,
                eventAction = CLICK,
                eventLabel = "click on banner play - $shopId - $businessWidgetPosition",
                promotions = listOf(
                        BaseTrackerConst.Promotion(
                                id = widgetId,
                                name = "/shoppage play outside banner - p$channelPositionInList",
                                creative = item.imageUrl,
                                position = channelPositionInList.toString()
                        )
                )
        ).appendUserId(userId).build()

        if (trackerMap is HashMap<String, Any>) trackingQueue.putEETracking(trackerMap)
    }

    override fun onImpressChannelCard(
            view: PlayWidgetMediumView,
            item: PlayWidgetMediumChannelUiModel,
            channelPositionInList: Int,
            isAutoPlay: Boolean,
            verticalWidgetPosition: Int,
            businessWidgetPosition: Int,
    ) {
        val trackerMap = if (isPlayWidgetSellerApp) {
            BaseTrackerBuilder().constructBasicPromotionView(
                    event = PROMO_VIEW,
                    eventCategory = SHOP_PAGE_SELLER,
                    eventAction = "impression card - widget play",
                    eventLabel = "$shopId - ${item.channelId} - $verticalWidgetPosition - $channelPositionInList - $businessWidgetPosition - ${getChannelStatusValue(item.channelType)}",
                    promotions = listOf(
                            BaseTrackerConst.Promotion(
                                    id = item.channelId,
                                    name = "/shoppage play inside banner",
                                    creative = item.title,
                                    position = channelPositionInList.toString()
                            )
                    )
            )
                    .appendUserId(userId)
                    .appendBusinessUnit("play")
                    .appendCurrentSite("tokopediaseller")
                    .appendShopId(shopId)
                    .build()
        } else {
            BaseTrackerBuilder().constructBasicPromotionView(
                    event = PROMO_VIEW,
                    eventCategory = SHOP_PAGE_BUYER,
                    eventAction = "impression on play sgc channel",
                    eventLabel = "view channel - $shopId - ${item.channelId} - $channelPositionInList - $businessWidgetPosition - $isAutoPlay",
                    promotions = listOf(
                            BaseTrackerConst.Promotion(
                                    id = widgetId,
                                    name = "/shoppage play inside banner - p$channelPositionInList",
                                    creative = item.video.coverUrl,
                                    position = channelPositionInList.toString()
                            )
                    )
            ).appendUserId(userId).build()
        }

        if (trackerMap is HashMap<String, Any>) trackingQueue.putEETracking(trackerMap)
    }

    override fun onClickChannelCard(
            view: PlayWidgetMediumView,
            item: PlayWidgetMediumChannelUiModel,
            channelPositionInList: Int,
            isAutoPlay: Boolean,
            verticalWidgetPosition: Int,
            businessWidgetPosition: Int,
    ) {
        val trackerMap = if (isPlayWidgetSellerApp) {
            BaseTrackerBuilder().constructBasicPromotionClick(
                    event = PROMO_CLICK,
                    eventCategory = SHOP_PAGE_SELLER,
                    eventAction = CLICK,
                    eventLabel = "card - widget play - $shopId - ${item.channelId} - $verticalWidgetPosition - $channelPositionInList - $businessWidgetPosition - ${getChannelStatusValue(item.channelType)}}",
                    promotions = listOf(
                            BaseTrackerConst.Promotion(
                                    id = item.channelId,
                                    name = "/shoppage play inside banner",
                                    creative = item.title,
                                    position = channelPositionInList.toString()
                            )
                    )
            )
                    .appendUserId(userId)
                    .appendBusinessUnit("play")
                    .appendCurrentSite("tokopediaseller")
                    .appendShopId(shopId)
                    .build()
        } else {
            BaseTrackerBuilder().constructBasicPromotionClick(
                    event = PROMO_CLICK,
                    eventCategory = SHOP_PAGE_BUYER,
                    eventAction = CLICK,
                    eventLabel = "click channel - $shopId - ${item.channelId} - $channelPositionInList - $businessWidgetPosition - $isAutoPlay",
                    promotions = listOf(
                            BaseTrackerConst.Promotion(
                                    id = widgetId,
                                    name = "/shoppage play inside banner - p$channelPositionInList",
                                    creative = item.video.coverUrl,
                                    position = channelPositionInList.toString()
                            )
                    )
            ).appendUserId(userId).build()
        }

        if (trackerMap is HashMap<String, Any>) trackingQueue.putEETracking(trackerMap)
    }

    override fun onClickToggleReminderChannel(
            view: PlayWidgetMediumView,
            item: PlayWidgetMediumChannelUiModel,
            channelPositionInList: Int,
            isRemindMe: Boolean,
            verticalWidgetPosition: Int,
            businessWidgetPosition: Int,
    ) {
        if (isPlayWidgetSellerApp) return
        TrackApp.getInstance().gtm.sendGeneralEvent(
                mapOf(
                        EVENT to CLICK_SHOP_PAGE,
                        EVENT_CATEGORY to SHOP_PAGE_BUYER,
                        EVENT_ACTION to "click ${if (!isRemindMe && userId.isNotBlank()) "on remove " else ""}remind me",
                        EVENT_LABEL to "${item.channelId} - $channelPositionInList - $businessWidgetPosition",
                        BUSINESS_UNIT to "ads solution",
                        CURRENT_SITE to "tokopediamarketplace",
                        USER_ID to userId
                )
        )
    }

    override fun onClickBannerCard(
            view: PlayWidgetMediumView,
            item: PlayWidgetMediumBannerUiModel,
            channelPositionInList: Int,
            verticalWidgetPosition: Int,
            businessWidgetPosition: Int,
    ) {
        if (isPlayWidgetSellerApp) return
        TrackApp.getInstance().gtm.sendGeneralEvent(
                mapOf(
                        EVENT to CLICK_SHOP_PAGE,
                        EVENT_CATEGORY to SHOP_PAGE_BUYER,
                        EVENT_ACTION to "click other content",
                        EVENT_LABEL to "$shopId - $businessWidgetPosition",
                        BUSINESS_UNIT to "ads solution",
                        CURRENT_SITE to "tokopediamarketplace",
                        USER_ID to userId
                )
        )
    }

    override fun onClickMoreActionChannel(view: PlayWidgetMediumView, item: PlayWidgetMediumChannelUiModel, channelPositionInList: Int, verticalWidgetPosition: Int, businessWidgetPosition: Int) {
        if (!isPlayWidgetSellerApp) return
        TrackApp.getInstance().gtm.sendGeneralEvent(
                mapOf(
                        EVENT to CLICK_SHOP_PAGE,
                        EVENT_CATEGORY to SHOP_PAGE_SELLER,
                        EVENT_ACTION to "click option button on card - widget play",
                        EVENT_LABEL to "$shopId - ${item.channelId} - $verticalWidgetPosition - $channelPositionInList - $businessWidgetPosition",
                        BUSINESS_UNIT to "play",
                        CURRENT_SITE to "tokopediaseller",
                        USER_ID to userId,
                        SHOP_ID to shopId
                )
        )
    }

    override fun onClickDeleteChannel(view: PlayWidgetMediumView, item: PlayWidgetMediumChannelUiModel, channelPositionInList: Int, verticalWidgetPosition: Int, businessWidgetPosition: Int) {
        if (!isPlayWidgetSellerApp) return
        TrackApp.getInstance().gtm.sendGeneralEvent(
                mapOf(
                        EVENT to CLICK_SHOP_PAGE,
                        EVENT_CATEGORY to SHOP_PAGE_SELLER,
                        EVENT_ACTION to "click delete on card - widget play",
                        EVENT_LABEL to "$shopId - ${item.channelId} - $verticalWidgetPosition - $channelPositionInList - $businessWidgetPosition - ${getChannelStatusValue(item.channelType)}}",
                        BUSINESS_UNIT to "play",
                        CURRENT_SITE to "tokopediaseller",
                        USER_ID to userId,
                        SHOP_ID to shopId
                )
        )
    }

    fun onImpressMoreActionChannel(item: PlayWidgetMediumChannelUiModel) {
        if (!isPlayWidgetSellerApp) return
        TrackApp.getInstance().gtm.sendGeneralEvent(
                mapOf(
                        EVENT to VIEW_SHOP_PAGE_IRIS,
                        EVENT_CATEGORY to SHOP_PAGE_SELLER,
                        EVENT_ACTION to "view bottom sheet - widget play",
                        EVENT_LABEL to "$shopId - ${item.channelId}",
                        BUSINESS_UNIT to "play",
                        CURRENT_SITE to "tokopediaseller",
                        USER_ID to userId,
                        SHOP_ID to shopId
                )
        )
    }

    fun onClickMoreActionShareLinkChannel(channelId: String) {
        if (!isPlayWidgetSellerApp) return
        TrackApp.getInstance().gtm.sendGeneralEvent(
                mapOf(
                        EVENT to CLICK_SHOP_PAGE,
                        EVENT_CATEGORY to SHOP_PAGE_SELLER,
                        EVENT_ACTION to "click share on bottom sheet - widget play",
                        EVENT_LABEL to "$shopId - $channelId",
                        BUSINESS_UNIT to "play",
                        CURRENT_SITE to "tokopediaseller",
                        USER_ID to userId,
                        SHOP_ID to shopId
                )
        )
    }

    fun onClickMoreActionPerformaChannel(channelId: String) {
        if (!isPlayWidgetSellerApp) return
        TrackApp.getInstance().gtm.sendGeneralEvent(
                mapOf(
                        EVENT to CLICK_SHOP_PAGE,
                        EVENT_CATEGORY to SHOP_PAGE_SELLER,
                        EVENT_ACTION to "click lihat performa - widget play",
                        EVENT_LABEL to "$shopId - $channelId",
                        BUSINESS_UNIT to "play",
                        CURRENT_SITE to "tokopediaseller",
                        USER_ID to userId,
                        SHOP_ID to shopId
                )
        )
    }

    fun onClickMoreActionDeleteChannel(channelId: String) {
        if (!isPlayWidgetSellerApp) return
        TrackApp.getInstance().gtm.sendGeneralEvent(
                mapOf(
                        EVENT to CLICK_SHOP_PAGE,
                        EVENT_CATEGORY to SHOP_PAGE_SELLER,
                        EVENT_ACTION to "click delete on bottom sheet - widget play",
                        EVENT_LABEL to "$shopId - $channelId",
                        BUSINESS_UNIT to "play",
                        CURRENT_SITE to "tokopediaseller",
                        USER_ID to userId,
                        SHOP_ID to shopId
                )
        )
    }

    fun onImpressDialogDeleteChannel(channelId: String) {
        if (!isPlayWidgetSellerApp) return
        TrackApp.getInstance().gtm.sendGeneralEvent(
                mapOf(
                        EVENT to VIEW_SHOP_PAGE_IRIS,
                        EVENT_CATEGORY to SHOP_PAGE_SELLER,
                        EVENT_ACTION to "view confirm on pop up delete - widget play",
                        EVENT_LABEL to "$shopId - $channelId",
                        BUSINESS_UNIT to "play",
                        CURRENT_SITE to "tokopediaseller",
                        USER_ID to userId,
                        SHOP_ID to shopId
                )
        )
    }

    fun onClickDialogDeleteChannel(channelId: String) {
        if (!isPlayWidgetSellerApp) return
        TrackApp.getInstance().gtm.sendGeneralEvent(
                mapOf(
                        EVENT to CLICK_SHOP_PAGE,
                        EVENT_CATEGORY to SHOP_PAGE_SELLER,
                        EVENT_ACTION to "click delete on pop up delete - widget play",
                        EVENT_LABEL to "$shopId - $channelId",
                        BUSINESS_UNIT to "play",
                        CURRENT_SITE to "tokopediaseller",
                        USER_ID to userId,
                        SHOP_ID to shopId
                )
        )
    }

    fun onImpressErrorDeleteChannel(channelId: String, errorMessage: String) {
        if (!isPlayWidgetSellerApp) return
        TrackApp.getInstance().gtm.sendGeneralEvent(
                mapOf(
                        EVENT to VIEW_SHOP_PAGE_IRIS,
                        EVENT_CATEGORY to SHOP_PAGE_SELLER,
                        EVENT_ACTION to "error state on shop page seller - widget play",
                        EVENT_LABEL to "$shopId - $channelId - $errorMessage",
                        BUSINESS_UNIT to "play",
                        CURRENT_SITE to "tokopediaseller",
                        USER_ID to userId,
                        SHOP_ID to shopId
                )
        )
    }

    private fun getChannelStatusValue(channelType: PlayWidgetChannelType): String {
        return when(channelType) {
            PlayWidgetChannelType.Live -> "live"
            PlayWidgetChannelType.Vod -> "active"
            PlayWidgetChannelType.Upcoming -> "upcoming"
            PlayWidgetChannelType.Transcoding -> "transcoding"
            PlayWidgetChannelType.FailedTranscoding -> "error"
            else -> ""
        }
    }

    companion object {
        const val EVENT = "event"
        const val EVENT_CATEGORY = "eventCategory"
        const val EVENT_ACTION = "eventAction"
        const val EVENT_LABEL = "eventLabel"

        const val CURRENT_SITE = "currentSite"
        const val BUSINESS_UNIT = "businessUnit"

        const val PROMO_VIEW = "promoView"
        const val PROMO_CLICK = "promoClick"

        const val CLICK_SHOP_PAGE = "clickShopPage"
        const val VIEW_SHOP_PAGE_IRIS = "viewShopPageIris"

        const val CLICK = "click"

        const val SHOP_PAGE_SELLER = "shop page - seller"
        const val SHOP_PAGE_BUYER = "shop page - buyer"

        const val SHOP_ID = "shopId"
        const val USER_ID = "userId"
    }
}