package com.tokopedia.videoTabComponent.domain.mapper

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalFeed
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.play.widget.ui.model.*
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.play.widget.ui.type.PlayWidgetPromoType
import com.tokopedia.videoTabComponent.domain.model.data.*
import com.tokopedia.videoTabComponent.domain.model.data.PlayFeedUiModel
import com.tokopedia.videoTabComponent.domain.model.data.PlayWidgetJumboUiModel
import com.tokopedia.videoTabComponent.domain.model.data.PlayWidgetLargeUiModel
import com.tokopedia.videoTabComponent.domain.model.data.PlayWidgetMediumUiModel
import com.tokopedia.videoTabComponent.util.PlayFeedDateTimeFormatter

private const val FEED_TYPE_PINNED_FEEDS = "pinnedFeeds"
private const val FEED_TYPE_CHANNEL_BLOCK = "channelBlock"
const val FEED_TYPE_TAB_MENU = "tabMenu"
private const val FEED_TYPE_CHANNEL_RECOM = "channelRecom"
private const val FEED_TYPE_CHANNEL_HIGHLIGHT = "channelHighlight"
private const val WIDGET_LIVE ="live"
const val WIDGET_UPCOMING ="upcoming"
private const val FEED_SEE_MORE_LIVE_APP_LINK = "${ApplinkConst.FEED_PlAY_LIVE_DETAIL}?${ApplinkConstInternalFeed.PLAY_LIVE_PARAM_WIDGET_TYPE}=${WIDGET_LIVE}"
private const val FEED_SEE_MORE_UPCOMING_APP_LINK = "${ApplinkConst.FEED_PlAY_LIVE_DETAIL}?${ApplinkConstInternalFeed.PLAY_LIVE_PARAM_WIDGET_TYPE}=${WIDGET_UPCOMING}"
private const val  UPCOMING_WIDGET_TITLE = "Nantikan live seru lainnya!"
private const val  LIVE_WIDGET_TITLE = "Lagi Live, nih!"
private const val GIVEAWAY = "GIVEAWAY"




object FeedPlayVideoTabMapper {
    fun getTabData(playGetContentSlotResponse: PlayGetContentSlotResponse): List<PlaySlot> {
        return playGetContentSlotResponse.data.filter {
            it.type == FEED_TYPE_TAB_MENU
        }
    }

    fun map(
        playSlotList: List<PlaySlot>,
        meta: PlayPagingProperties,
        position: Int = 0,
        shopId: String
    ): List<PlayFeedUiModel> {

        val list = mutableListOf<PlayFeedUiModel>()


        playSlotList.forEach { playSlot ->
            when (playSlot.type) {
                FEED_TYPE_PINNED_FEEDS -> {
                    list.add(
                        PlayWidgetJumboUiModel(
                            getWidgetUiModel(playSlot, meta, shopId)
                        )
                    )
                }
                FEED_TYPE_CHANNEL_BLOCK -> {
                    list.add(
                        PlayWidgetLargeUiModel(
                            getWidgetUiModel(playSlot, meta, shopId)
                        )
                    )
                }
                FEED_TYPE_TAB_MENU -> {
                    list.add(
                        PlaySlotTabMenuUiModel(
                            playSlot.items.mapIndexed { index, playSlotItem ->
                                PlaySlotTabMenuUiModel.Item(
                                    playSlotItem.id, playSlotItem.label, playSlotItem.icon_url,
                                    playSlotItem.group, playSlotItem.source_type,
                                    playSlotItem.source_id, playSlotItem.slug_id, index == position
                                )
                            }
                        )
                    )
                }
                FEED_TYPE_CHANNEL_RECOM -> {
                    list.add(
                        PlayWidgetLargeUiModel(
                            getWidgetUiModel(playSlot, meta, shopId)
                        )

                    )
                }
                FEED_TYPE_CHANNEL_HIGHLIGHT -> {
                    list.add(
                        PlayWidgetMediumUiModel(
                            getWidgetUiModel(playSlot, meta, shopId)
                        )

                    )
                }
            }
        }

        return list
    }

    private fun getWidgetUiModel(
        playSlot: PlaySlot, meta: PlayPagingProperties,
        shopId: String
    ): PlayWidgetUiModel {

        val item = playSlot.items.firstOrNull() ?: return PlayWidgetUiModel.Empty

        //check these values

        val autoRefresh = false
        val autoRefreshTimer: Long = 0
        val autoPlayAmount: Int = 0
        val maxAutoPlayWifiDuration: Int = 30
        val businessWidgetPosition: Int = 30
        //till here
        val actionLink = if (PlayWidgetChannelType.getByValue(playSlot.items.first().air_time) == PlayWidgetChannelType.Upcoming) FEED_SEE_MORE_UPCOMING_APP_LINK else FEED_SEE_MORE_LIVE_APP_LINK


        return PlayWidgetUiModel(
            title = playSlot.title,
            actionTitle = playSlot.lihat_semua.label,
            actionAppLink = actionLink,
            isActionVisible = playSlot.lihat_semua.show,
            config = PlayWidgetConfigUiModel(
                autoRefresh, autoRefreshTimer, meta.is_autoplay, autoPlayAmount,
                meta.max_autoplay_in_cell, maxAutoPlayWifiDuration, businessWidgetPosition
            ),
            background = PlayWidgetBackgroundUiModel(
                "", item.appLink, item.webLink, emptyList(), ""
            ),
            getWidgetItemUiModel(playSlot.items, shopId)
        )
    }

    private fun getWidgetItemUiModel(items: List<PlaySlotItems>, shopId: String): List<PlayWidgetItemUiModel> {

        //check PlayWidgetShareUiModel(item.share.text -> is it be `item.share.text for "fullShareContent"`
        val performanceSummaryLink = ""
        val poolType = ""
        val recommendationType = ""

        val channelTypeTransitionPrev = ""
        val channelTypeTransitionNext = ""
        //till here

        val list = mutableListOf<PlayWidgetItemUiModel>()

        items.forEach { item ->
            val channelType = PlayWidgetChannelType.getByValue(item.air_time)
            list.add(
                PlayWidgetChannelUiModel(
                    channelId = item.id,
                    title = item.title,
                    appLink = item.appLink,
                    startTime = PlayFeedDateTimeFormatter.formatDate(item.start_time),
                    totalView = PlayWidgetTotalView(item.stats.view.formatted, channelType != PlayWidgetChannelType.Upcoming),
                    promoType = PlayWidgetPromoType.getByType(
                        item.configurations.promoLabels.firstOrNull()?.type ?: "",
                        item.configurations.promoLabels.firstOrNull()?.text ?: ""
                    ),
                    reminderType = getReminderType(item.configurations.reminder.isSet),
                    partner = PlayWidgetPartnerUiModel(item.partner.id, item.partner.name),
                    video = PlayWidgetVideoUiModel(
                        item.video.id, item.is_live,
                        item.video.cover_url, item.video.stream_source
                    ),
                    channelType = channelType,
                    hasGiveaway = mapHasGiveaway(item.configurations.promoLabels),
                    share = PlayWidgetShareUiModel(item.share.text, item.share.is_show_button),
                    performanceSummaryLink = performanceSummaryLink,
                    poolType = poolType,
                    recommendationType = recommendationType,
                    hasAction = shouldHaveActionMenu(channelType,item.partner.id, shopId),
                    channelTypeTransition = PlayWidgetChannelTypeTransition(
                        PlayWidgetChannelType.getByValue(channelTypeTransitionPrev),
                        PlayWidgetChannelType.getByValue(channelTypeTransitionNext)
                    )
                )
            )
        }

        return list
    }
    private fun mapHasGiveaway(promoLabels: List<Configurations.PromoLabel>): Boolean {
        return promoLabels.firstOrNull { it.type == GIVEAWAY } != null
    }

    private fun shouldHaveActionMenu(channelType: PlayWidgetChannelType, partnerId: String, shopId: String): Boolean {
        return channelType == PlayWidgetChannelType.Vod &&
                shopId == partnerId
    }
}