package com.tokopedia.videoTabComponent.domain.mapper

import com.tokopedia.play.widget.sample.data.PlayGetContentSlotResponse
import com.tokopedia.play.widget.sample.data.PlayPagingProperties
import com.tokopedia.play.widget.sample.data.PlaySlot
import com.tokopedia.play.widget.sample.data.PlaySlotItems
import com.tokopedia.play.widget.ui.model.*
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.play.widget.ui.type.PlayWidgetPromoType

private const val FEED_TYPE_PINNED_FEEDS = "pinnedFeeds"
private const val FEED_TYPE_CHANNEL_BLOCK = "channelBlock"
private const val FEED_TYPE_TAB_MENU = "tabMenu"
private const val FEED_TYPE_CHANNEL_RECOM = "channelRecom"
private const val FEED_TYPE_CHANNEL_HIGHLIGHT = "channelHighlight"
private const val LIVE = "live"

object FeedPlayVideoTabMapper {
    fun map(
        playGetContentSlotResponse: PlayGetContentSlotResponse, cursor: String
    ): List<PlayFeedUiModel> {

        val list = mutableListOf<PlayFeedUiModel>()
        val meta = playGetContentSlotResponse.meta

        playGetContentSlotResponse.data.forEach { playSlot ->
            when (playSlot.type) {
                FEED_TYPE_PINNED_FEEDS -> {
                    list.add(PlayWidgetJumboUiModel(getWidgetUiModel(playSlot, meta)))
                }
                FEED_TYPE_CHANNEL_BLOCK -> {
                    list.add(PlayWidgetMediumUiModel(getWidgetUiModel(playSlot, meta)))
                }
                FEED_TYPE_TAB_MENU -> {
                    list.add(
                        PlayWidgetSlotTabUiModel(
                            playSlot.mods.mapIndexed { index, item -> item.label to (index == 0) }
                        )
                    )
                }
                FEED_TYPE_CHANNEL_RECOM -> {
                    list.add(PlayWidgetLargeUiModel(getWidgetUiModel(playSlot, meta)))
                }
            }
        }

        return list
    }

    private fun getWidgetUiModel(
        playSlot: PlaySlot, meta: PlayPagingProperties,
    ): PlayWidgetUiModel {

        //check these values
        val appLink = playSlot.mods.firstOrNull()?.appLink ?: ""
        val webLink = playSlot.mods.firstOrNull()?.webLink ?: ""
        val autoRefresh = false
        val autoRefreshTimer: Long = 0
        val autoPlayAmount: Int = 0
        val maxAutoPlayWifiDuration: Int = 30
        val businessWidgetPosition: Int = 30
        //till here

        return PlayWidgetUiModel(
            playSlot.title,
            playSlot.lihat_semua.label,
            appLink,
            playSlot.lihat_semua.show,
            PlayWidgetConfigUiModel(
                autoRefresh, autoRefreshTimer, meta.is_autoplay, autoPlayAmount,
                meta.max_autoplay_in_cell, maxAutoPlayWifiDuration, businessWidgetPosition
            ),
            PlayWidgetBackgroundUiModel(
                "", appLink, webLink, emptyList(), ""
            ),
            getWidgetItemUiModel(playSlot.mods)
        )
    }

    private fun getWidgetItemUiModel(items: List<PlaySlotItems>): List<PlayWidgetItemUiModel> {

        //check these values
        val isTotalViewVisible = true
        val hasGiveaway = false
        //check PlayWidgetShareUiModel(item.share.text -> is it be `item.share.text for "fullShareContent"`
        val performanceSummaryLink = ""
        val poolType = ""
        val recommendationType = ""
        val hasAction = false
        val channelTypeTransitionPrev = ""
        val channelTypeTransitionNext = ""
        //till here

        val list = mutableListOf<PlayWidgetItemUiModel>()

        items.forEach { item ->
            list.add(
                PlayWidgetChannelUiModel(
                    item.id, item.title, item.appLink, item.start_time,
                    PlayWidgetTotalView(item.stats.view.formatted, isTotalViewVisible),
                    PlayWidgetPromoType.getByType(
                        item.configurations.promoLabels.firstOrNull()?.type ?: "",
                        item.configurations.promoLabels.firstOrNull()?.text ?: ""
                    ),
                    getReminderType(item.configurations.reminder.isSet),
                    PlayWidgetPartnerUiModel(item.partner.id, item.partner.name),
                    PlayWidgetVideoUiModel(
                        item.video.id, item.is_live,
                        item.video.cover_url, item.video.stream_source
                    ),
                    PlayWidgetChannelType.getByValue(item.display_type),
                    hasGiveaway,
                    PlayWidgetShareUiModel(item.share.text, item.share.is_show_button),
                    performanceSummaryLink,
                    poolType,
                    recommendationType,
                    hasAction,
                    PlayWidgetChannelTypeTransition(
                        PlayWidgetChannelType.getByValue(channelTypeTransitionPrev),
                        PlayWidgetChannelType.getByValue(channelTypeTransitionNext)
                    )
                )
            )
        }

        return list
    }
}