package com.tokopedia.videoTabComponent.domain.mapper

import com.tokopedia.play.widget.sample.data.PlayGetContentSlotResponse
import com.tokopedia.play.widget.ui.model.*
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.play.widget.ui.type.PlayWidgetPromoType
import com.tokopedia.videoTabComponent.domain.model.data.PlayPagingProperties
import com.tokopedia.videoTabComponent.domain.model.data.PlaySlot
import com.tokopedia.videoTabComponent.domain.model.data.PlaySlotItems

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
                    list.add(
                        PlayWidgetJumboUiModel(
                            getWidgetUiModel(playSlot, getWidgetItemUiModel(playSlot.items), meta)
                        )
                    )
                }
                FEED_TYPE_CHANNEL_BLOCK -> {
                    list.add(
                        PlayWidgetMediumUiModel(
                            getWidgetUiModel(playSlot, getWidgetItemUiModel(playSlot.items), meta)
                        )
                    )
                }
                FEED_TYPE_TAB_MENU -> {
                    list.add(
                        PlayWidgetSlotTabUiModel(
                            playSlot.items.mapIndexed { index, item -> item.label to (index == 0) }
                        )
                    )
                }
                FEED_TYPE_CHANNEL_RECOM -> {
                    list.add(
                        PlayWidgetLargeUiModel(
                            getWidgetUiModel(playSlot, getWidgetItemUiModel(playSlot.items), meta)
                        )

                    )
                }
                FEED_TYPE_CHANNEL_HIGHLIGHT -> {
                    list.add(
                            PlayWidgetMediumUiModel(
                                    getWidgetUiModel(playSlot, getWidgetItemUiModel(playSlot.items), meta)
                            )

                    )
                }
            }
        }

        return list
    }

    private fun getWidgetUiModel(
            playSlot: PlaySlot, playWidgetItemsUiModel: List<PlayWidgetItemUiModel>, meta: PlayPagingProperties,
    ): PlayWidgetUiModel {

        val item = playSlot.items.firstOrNull() ?: return PlayWidgetUiModel.Empty

        //check these values
        val appLink = playSlot.items.firstOrNull()?.appLink ?: ""
        val webLink = playSlot.items.firstOrNull()?.webLink ?: ""
        val autoRefresh = false
        val autoRefreshTimer: Long = 0
        val autoPlayAmount: Int = 0
        val maxAutoPlayWifiDuration: Int = 30
        val businessWidgetPosition: Int = 30
        //till here

        return PlayWidgetUiModel(
                title = playSlot.title,
                actionTitle = playSlot.lihat_semua.label,
                actionAppLink = appLink,
                isActionVisible = playSlot.lihat_semua.show,
                config = PlayWidgetConfigUiModel(
                        autoRefresh, autoRefreshTimer, meta.is_autoplay, autoPlayAmount,
                        meta.max_autoplay_in_cell, maxAutoPlayWifiDuration, businessWidgetPosition
                ),
                background = PlayWidgetBackgroundUiModel(
                        "", item.appLink, item.webLink, emptyList(), ""
                ),
                getWidgetItemUiModel(playSlot.items)
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
                    channelId = item.id,
                    title = item.title,
                    appLink = item.appLink,
                    startTime = item.start_time,
                    totalView = PlayWidgetTotalView(item.stats.view.formatted, isTotalViewVisible),
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
                    channelType = PlayWidgetChannelType.getByValue(item.display_type),
                    hasGiveaway= hasGiveaway,
                    share = PlayWidgetShareUiModel(item.share.text, item.share.is_show_button),
                    performanceSummaryLink = performanceSummaryLink,
                    poolType = poolType,
                    recommendationType = recommendationType,
                    hasAction = hasAction,
                    channelTypeTransition = PlayWidgetChannelTypeTransition(
                        PlayWidgetChannelType.getByValue(channelTypeTransitionPrev),
                        PlayWidgetChannelType.getByValue(channelTypeTransitionNext)
                    )
                )
            )
        }

        return list
    }
}