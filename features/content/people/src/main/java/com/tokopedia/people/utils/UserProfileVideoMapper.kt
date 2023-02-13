package com.tokopedia.people.utils

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalFeed
import com.tokopedia.people.model.PlayPostContentItem
import com.tokopedia.people.model.PostPromoLabel
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelTypeTransition
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetPartnerUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetShareUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetTotalView
import com.tokopedia.play.widget.ui.model.PlayWidgetVideoUiModel
import com.tokopedia.play.widget.ui.model.getReminderType
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.play.widget.ui.type.PlayWidgetPromoType

private const val FEED_TYPE_PINNED_FEEDS = "pinnedFeeds"
private const val FEED_TYPE_CHANNEL_BLOCK = "channelBlock"
const val FEED_TYPE_TAB_MENU = "tabMenu"
private const val FEED_TYPE_CHANNEL_RECOM = "channelRecom"
private const val FEED_TYPE_CHANNEL_HIGHLIGHT = "channelHighlight"
private const val WIDGET_LIVE = "live"
const val WIDGET_UPCOMING = "upcoming"
private const val FEED_SEE_MORE_LIVE_APP_LINK = "${ApplinkConst.FEED_PlAY_LIVE_DETAIL}?${ApplinkConstInternalFeed.PLAY_LIVE_PARAM_WIDGET_TYPE}=$WIDGET_LIVE"
private const val FEED_SEE_MORE_UPCOMING_APP_LINK = "${ApplinkConst.FEED_PlAY_LIVE_DETAIL}?${ApplinkConstInternalFeed.PLAY_LIVE_PARAM_WIDGET_TYPE}=$WIDGET_UPCOMING"
private const val UPCOMING_WIDGET_TITLE = "Nantikan live seru lainnya!"
private const val LIVE_WIDGET_TITLE = "Lagi Live, nih!"
private const val HADIAH = "Hadiah"

object UserProfileVideoMapper {

    fun map(
        playSlotList: PlayPostContentItem,
        userId: String,
    ): PlayWidgetChannelUiModel {
        return getWidgetItemUiModel(playSlotList, userId)
    }

    private fun getWidgetItemUiModel(item: PlayPostContentItem, userId: String): PlayWidgetChannelUiModel {
        // check PlayWidgetShareUiModel(item.share.text -> is it be `item.share.text for "fullShareContent"`
        val performanceSummaryLink = ""
        val poolType = ""
        val recommendationType = ""

        val channelTypeTransitionPrev = ""
        val channelTypeTransitionNext = ""
        val channelType = PlayWidgetChannelType.getByValue(item.displayType)
        return PlayWidgetChannelUiModel(
            channelId = item.id,
            title = item.title,
            appLink = item.appLink,
            startTime = UserProfileDateTimeFormatter.formatDate(item.startTime),
            totalView = PlayWidgetTotalView(
                item.stats.view.formatted,
                channelType != PlayWidgetChannelType.Upcoming,
            ),
            promoType = PlayWidgetPromoType.getByType(
                getPromoType(item.configurations.promoLabels).type,
                getPromoType(item.configurations.promoLabels).text,
            ),
            reminderType = getReminderType(item.configurations.reminder.isSet),
            partner = PlayWidgetPartnerUiModel(item.partner.id, item.partner.name),
            video = PlayWidgetVideoUiModel(item.id, item.isLive, item.coverUrl, item.webLink),
            channelType = channelType,
            hasGame = mapHasGame(item.configurations.promoLabels),
            // TODO later
            share = PlayWidgetShareUiModel("", false),
            performanceSummaryLink = performanceSummaryLink,
            poolType = poolType,
            recommendationType = recommendationType,
            hasAction = shouldHaveActionMenu(channelType, item.partner.id, userId),
            channelTypeTransition = PlayWidgetChannelTypeTransition(
                PlayWidgetChannelType.getByValue(channelTypeTransitionPrev),
                PlayWidgetChannelType.getByValue(channelTypeTransitionNext),
            ),
        )
    }
}

private fun mapHasGame(promoLabels: List<PostPromoLabel>): Boolean {
    return promoLabels.firstOrNull { it.text == HADIAH } != null
}

private fun getPromoType(promoLabels: List<PostPromoLabel>): PostPromoLabel {
    promoLabels.firstOrNull()?.let {
        return it
    }
    return PostPromoLabel("", "")
}

private fun shouldHaveActionMenu(channelType: PlayWidgetChannelType, partnerId: String, userId: String): Boolean {
    return channelType == PlayWidgetChannelType.Vod &&
        userId == partnerId
}
