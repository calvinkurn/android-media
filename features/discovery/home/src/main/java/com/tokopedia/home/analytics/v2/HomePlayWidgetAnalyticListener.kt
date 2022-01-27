package com.tokopedia.home.analytics.v2

import com.tokopedia.home.analytics.v2.BaseTracking.BusinessUnit.ADS_SOLUTION
import com.tokopedia.home.analytics.v2.BaseTracking.Event.CLICK_HOMEPAGE
import com.tokopedia.play.widget.analytic.PlayWidgetAnalyticListener
import com.tokopedia.play.widget.ui.PlayWidgetMediumView
import com.tokopedia.play.widget.ui.model.PlayWidgetBannerUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetBackgroundUiModel
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst
import com.tokopedia.trackingoptimizer.TrackingQueue

/**
 * https://docs.google.com/spreadsheets/d/1NR4Cfq5S4MjY_i4WqWRTqV0kguAKsi74N29q6rdb2K8/edit#gid=359560973 row 12-16, 20-21
 * https://mynakama.tokopedia.com/datatracker/requestdetail/view/61
 */
class HomePlayWidgetAnalyticListener(
        private val trackingQueue: TrackingQueue?,
        private val userId: String
) : BaseTracking(), PlayWidgetAnalyticListener {

    var widgetId: String = ""
    var widgetName: String = ""

    private var mBusinessWidgetPosition: Int = 0

    fun setBusinessWidgetPosition(businessWidgetPosition: Int) {
        this.mBusinessWidgetPosition = businessWidgetPosition
    }

    override fun onClickViewAll(view: PlayWidgetMediumView) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                mapOf(
                        Event.KEY to CLICK_HOMEPAGE,
                        Category.KEY to "homepage-cmp",
                        Action.KEY to "click view all",
                        Label.KEY to "0 - Tokopedia Play - $mBusinessWidgetPosition",
                        BusinessUnit.KEY to ADS_SOLUTION,
                        CurrentSite.KEY to CurrentSite.DEFAULT,
                        UserId.KEY to userId
                )
        )
    }

    override fun onImpressOverlayCard(view: PlayWidgetMediumView, item: PlayWidgetBackgroundUiModel, channelPositionInList: Int) {
        val trackerMap = BaseTrackerBuilder().constructBasicPromotionView(
                event = Event.PROMO_VIEW,
                eventCategory = "homepage-cmp",
                eventAction = "impression on play sgc banner",
                eventLabel = "${item.overlayImageUrl} - $mBusinessWidgetPosition",
                promotions = listOf(
                        BaseTrackerConst.Promotion(
                                id = widgetId,
                                name = "/ - p$channelPositionInList - play sgc banner - $widgetName",
                                creative = item.overlayImageUrl,
                                position = channelPositionInList.toString()
                        )
                )
        )
                .appendUserId(userId)
                .appendChannelId(widgetId)
                .build()

        if (trackerMap is HashMap<String, Any>) trackingQueue?.putEETracking(trackerMap)
    }

    override fun onClickOverlayCard(view: PlayWidgetMediumView, item: PlayWidgetBackgroundUiModel, channelPositionInList: Int) {
        val trackerMap = BaseTrackerBuilder().constructBasicPromotionClick(
                event = Event.PROMO_CLICK,
                eventCategory = "homepage-cmp",
                eventAction = Event.CLICK,
                eventLabel = "click on banner play - ${item.overlayImageUrl} - $mBusinessWidgetPosition",
                promotions = listOf(
                        BaseTrackerConst.Promotion(
                                id = widgetId,
                                name = "/ - p$channelPositionInList - play sgc banner - $widgetName",
                                creative = item.overlayImageUrl,
                                position = channelPositionInList.toString()
                        )
                )
        )
                .appendUserId(userId)
                .appendChannelId(widgetId)
                .build()

        if (trackerMap is HashMap<String, Any>) trackingQueue?.putEETracking(trackerMap)
    }

    override fun onImpressChannelCard(view: PlayWidgetMediumView, item: PlayWidgetChannelUiModel, channelPositionInList: Int, isAutoPlay: Boolean) {
        val finalChannelPositionInList = channelPositionInList + 1
        val promoText = if (item.promoType.promoText.isNotBlank()) item.promoType.promoText else "no promo"
        val trackerMap = BaseTrackerBuilder().constructBasicPromotionView(
                event = Event.PROMO_VIEW,
                eventCategory = "homepage-cmp",
                eventAction = "impression on play sgc channel",
                eventLabel = "${item.partner.id} - " +
                        "${item.channelId} - " +
                        "$finalChannelPositionInList - " +
                        "$mBusinessWidgetPosition - " +
                        "$isAutoPlay - " +
                        "${item.poolType} - " +
                        "$promoText - " +
                        item.recommendationType,
                promotions = listOf(
                        BaseTrackerConst.Promotion(
                                id = widgetId,
                                name = "/ - p$finalChannelPositionInList - play sgc channel - ${item.title}",
                                creative = item.video.coverUrl,
                                position = finalChannelPositionInList.toString()
                        )
                )
        )
                .appendUserId(userId)
                .appendChannelId(item.channelId)
                .appendBusinessUnit("play")
                .appendCurrentSite("tokopediamarketplace")
                .build()

        if (trackerMap is HashMap<String, Any>) trackingQueue?.putEETracking(trackerMap)
    }

    override fun onClickChannelCard(view: PlayWidgetMediumView, item: PlayWidgetChannelUiModel, channelPositionInList: Int, isAutoPlay: Boolean) {
        val finalChannelPositionInList = channelPositionInList + 1
        val promoText = if (item.promoType.promoText.isNotBlank()) item.promoType.promoText else "no promo"
        val trackerMap = BaseTrackerBuilder().constructBasicPromotionClick(
                event = Event.PROMO_CLICK,
                eventCategory = "homepage-cmp",
                eventAction = Event.CLICK,
                eventLabel = "click channel - " +
                        "${item.partner.id} - " +
                        "${item.channelId} - " +
                        "$finalChannelPositionInList - " +
                        "$mBusinessWidgetPosition - " +
                        "$isAutoPlay - " +
                        "${item.poolType} - " +
                        "$promoText - " +
                        item.recommendationType,
                promotions = listOf(
                        BaseTrackerConst.Promotion(
                                id = widgetId,
                                name = "/ - p$finalChannelPositionInList - play sgc channel - ${item.title}",
                                creative = item.video.coverUrl,
                                position = finalChannelPositionInList.toString()
                        )
                )
        )
                .appendChannelId(item.channelId)
                .appendUserId(userId)
                .appendBusinessUnit("play")
                .appendCurrentSite("tokopediamarketplace")
                .build()

        if (trackerMap is HashMap<String, Any>) trackingQueue?.putEETracking(trackerMap)
    }

    override fun onClickToggleReminderChannel(view: PlayWidgetMediumView, item: PlayWidgetChannelUiModel, channelPositionInList: Int, isRemindMe: Boolean) {
        val finalChannelPositionInList = channelPositionInList + 1
        TrackApp.getInstance().gtm.sendGeneralEvent(
                mapOf(
                        Event.KEY to CLICK_HOMEPAGE,
                        Category.KEY to "homepage-cmp",
                        Action.KEY to "click ${if (!isRemindMe && userId.isNotBlank()) "on remove " else ""}remind me",
                        Label.KEY to "${item.channelId} - " +
                                "$finalChannelPositionInList - " +
                                "${item.poolType} - " +
                                if (item.promoType.promoText.isNotBlank()) item.promoType.promoText else "no promo",
                        UserId.KEY to userId,
                        BusinessUnit.KEY to "play",
                        CurrentSite.KEY to "tokopediamarketplace"
                )
        )
    }

    override fun onClickBannerCard(view: PlayWidgetMediumView, item: PlayWidgetBannerUiModel, channelPositionInList: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                mapOf(
                        Event.KEY to CLICK_HOMEPAGE,
                        Category.KEY to "homepage-cmp",
                        Action.KEY to "click other content",
                        Label.KEY to "${item.imageUrl} - $mBusinessWidgetPosition",
                        BusinessUnit.KEY to ADS_SOLUTION,
                        CurrentSite.KEY to CurrentSite.DEFAULT,
                        UserId.KEY to userId
                )
        )
    }
}
