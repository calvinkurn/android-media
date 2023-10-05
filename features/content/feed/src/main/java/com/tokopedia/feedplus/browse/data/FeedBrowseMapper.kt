package com.tokopedia.feedplus.browse.data

import com.tokopedia.content.common.model.Content
import com.tokopedia.content.common.model.ContentItem
import com.tokopedia.content.common.model.ContentSlotMeta
import com.tokopedia.content.common.model.FeedXHeaderResponse
import com.tokopedia.content.common.model.WidgetSlot
import com.tokopedia.feedplus.browse.data.model.WidgetRequestModel
import com.tokopedia.feedplus.browse.presentation.model.ChannelUiState
import com.tokopedia.feedplus.browse.presentation.model.ChipUiState
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseChipUiModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseConfigUiModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemUiModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiModel
import com.tokopedia.feedplus.data.FeedXCard
import com.tokopedia.feedplus.data.FeedXHomeEntity
import com.tokopedia.play.widget.ui.model.PlayGridType
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelTypeTransition
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetPartnerUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.model.PlayWidgetShareUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetTotalView
import com.tokopedia.play.widget.ui.model.PlayWidgetVideoUiModel
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.play.widget.ui.type.PlayWidgetPromoType
import com.tokopedia.videoTabComponent.domain.mapper.FEED_TYPE_CHANNEL_BLOCK
import com.tokopedia.videoTabComponent.domain.mapper.FEED_TYPE_TAB_MENU
import javax.inject.Inject

/**
 * Created by meyta.taliti on 11/08/23.
 */
class FeedBrowseMapper @Inject constructor() {

    fun mapTitle(response: FeedXHeaderResponse): String {
        return response.feedXHeaderData.data.browse.title
    }

    fun mapSlots(response: FeedXHomeEntity): List<FeedBrowseUiModel> {
        return response.items.mapNotNull { item ->
            if (item.typename == FeedXCard.TYPE_FEED_X_CARD_PLACEHOLDER) {
                FeedBrowseUiModel.Channel(
                    id = item.id,
                    title = item.title,
                    extraParam = WidgetRequestModel(group = item.type),
                    chipUiState = ChipUiState.Placeholder,
                    channelUiState = ChannelUiState.Placeholder
                )
            } else {
                null
            }
        }
    }

    fun mapWidget(response: WidgetSlot): FeedBrowseItemUiModel {
        val data = response.playGetContentSlot.data
        val firstWidget = data.first()
        return when (firstWidget.type) {
            FEED_TYPE_TAB_MENU -> {
                ChipUiState.Data(mapChips(firstWidget))
            }
            FEED_TYPE_CHANNEL_BLOCK -> {
                ChannelUiState.Data(
                    mapChannel(firstWidget),
                    mapConfig(response.playGetContentSlot.playGetContentSlot)
                )
            }
            else -> ChannelUiState.Error(IllegalStateException())
        }
    }

    private fun mapChips(data: Content): List<FeedBrowseChipUiModel> {
        return data.items.mapIndexed { index, item ->
            FeedBrowseChipUiModel(
                id = item.id,
                label = item.label,
                extraParam = WidgetRequestModel(
                    group = item.group,
                    sourceType = item.sourceType,
                    sourceId = item.sourceId
                ),
                isSelected = index == 0 // select first chip
            )
        }
    }

    private fun mapChannel(data: Content): List<PlayWidgetChannelUiModel> {
        return data.items.map { item ->
            mapChannel(item)
        }
    }

    private fun mapConfig(data: ContentSlotMeta): FeedBrowseConfigUiModel {
        return FeedBrowseConfigUiModel(
            data = PlayWidgetConfigUiModel.Empty.copy(
                autoRefresh = data.autoRefresh,
                autoRefreshTimer = data.autoRefreshTimer,
                autoPlay = data.isAutoplay,
                autoPlayAmount = data.maxAutoplayInCell
            ),
            lastUpdated = System.currentTimeMillis()
        )
    }

    private fun mapChannel(
        item: ContentItem
    ): PlayWidgetChannelUiModel {
        val channelType = PlayWidgetChannelType.getByValue(item.airTime)
        val totalView = item.stats.view.formatted
        val coverUrl = item.coverUrl
        return PlayWidgetChannelUiModel(
            channelId = item.id,
            title = item.title,
            appLink = item.appLink,
            startTime = item.startTime,
            totalView = PlayWidgetTotalView(totalView, true),
            promoType = PlayWidgetPromoType.NoPromo,
            reminderType = PlayWidgetReminderType.NotReminded,
            partner = PlayWidgetPartnerUiModel.Empty.copy(id = item.partner.id),
            video = PlayWidgetVideoUiModel.Empty.copy(coverUrl = coverUrl),
            channelType = channelType,
            hasGame = false,
            share = PlayWidgetShareUiModel.Empty,
            performanceSummaryLink = "",
            poolType = "",
            recommendationType = item.recommendationType,
            hasAction = false,
            products = emptyList(),
            shouldShowPerformanceDashboard = false,
            channelTypeTransition = PlayWidgetChannelTypeTransition(prevType = null, currentType = PlayWidgetChannelType.Unknown),
            gridType = PlayGridType.Unknown,
            extras = emptyMap()
        )
    }
}
