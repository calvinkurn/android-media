package com.tokopedia.videoTabComponent.domain.mapper

import com.tokopedia.play.widget.ui.model.*
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.play.widget.ui.type.PlayWidgetPromoType
import com.tokopedia.videoTabComponent.domain.model.data.*
import com.tokopedia.videoTabComponent.domain.model.data.PlayFeedUiModel
import com.tokopedia.videoTabComponent.domain.model.data.PlayWidgetJumboUiModel
import com.tokopedia.videoTabComponent.domain.model.data.PlayWidgetLargeUiModel
import com.tokopedia.videoTabComponent.domain.model.data.PlayWidgetMediumUiModel

private const val FEED_TYPE_PINNED_FEEDS = "pinnedFeeds"
private const val FEED_TYPE_CHANNEL_BLOCK = "channelBlock"
const val FEED_TYPE_TAB_MENU = "tabMenu"
private const val FEED_TYPE_CHANNEL_RECOM = "channelRecom"
private const val FEED_TYPE_CHANNEL_HIGHLIGHT = "channelHighlight"
private const val FEED_SEE_MORE_APP_LINK = "tokopedia://feedplaylivedetail"


object FeedPlayVideoTabMapper {
    fun getTabData(playGetContentSlotResponse: PlayGetContentSlotResponse): List<PlaySlot> {
        return playGetContentSlotResponse.data.filter {
            it.type == FEED_TYPE_TAB_MENU
        }
    }

    fun map(
        playSlotList: List<PlaySlot>,
        meta: PlayPagingProperties,
        position: Int = 0
    ): List<PlayFeedUiModel> {

        val list = mutableListOf<PlayFeedUiModel>()


        playSlotList.forEach { playSlot ->
            when (playSlot.type) {
                FEED_TYPE_PINNED_FEEDS -> {
                    list.add(
                        PlayWidgetJumboUiModel(
                            getWidgetUiModel(playSlot, meta)
                        )
                    )
                }
                FEED_TYPE_CHANNEL_BLOCK -> {
                    list.add(
                        PlayWidgetLargeUiModel(
                            getWidgetUiModel(playSlot, meta)
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
                            getWidgetUiModel(playSlot, meta)
                        )

                    )
                }
                FEED_TYPE_CHANNEL_HIGHLIGHT -> {
                    list.add(
                        PlayWidgetMediumUiModel(
                            getWidgetUiModel(playSlot, meta)
                        )

                    )
                }
            }
        }

        return list
    }

    private fun getWidgetUiModel(
        playSlot: PlaySlot, meta: PlayPagingProperties,
    ): PlayWidgetUiModel {

        val item = playSlot.items.firstOrNull() ?: return PlayWidgetUiModel.Empty

        //check these values
        val appLink = playSlot.items.firstOrNull()?.appLink ?: ""
        val autoRefresh = false
        val autoRefreshTimer: Long = 0
        val autoPlayAmount: Int = 0
        val maxAutoPlayWifiDuration: Int = 30
        val businessWidgetPosition: Int = 30
        //till here

        return PlayWidgetUiModel(
            title = playSlot.title,
            actionTitle = playSlot.lihat_semua.label,
            actionAppLink = FEED_SEE_MORE_APP_LINK,
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
                    hasGiveaway = hasGiveaway,
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