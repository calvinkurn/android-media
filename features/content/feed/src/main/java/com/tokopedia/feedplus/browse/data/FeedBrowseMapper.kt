package com.tokopedia.feedplus.browse.data

import com.tokopedia.content.common.model.Content
import com.tokopedia.content.common.model.FeedXHeaderResponse
import com.tokopedia.content.common.model.WidgetSlot
import com.tokopedia.feedplus.browse.presentation.model.ChannelUiState
import com.tokopedia.feedplus.browse.presentation.model.ChipUiState
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseChipUiModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemUiModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiModel
import com.tokopedia.feedplus.data.FeedXHomeEntity
import com.tokopedia.play.widget.ui.model.PlayGridType
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelTypeTransition
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetPartnerUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.model.PlayWidgetShareUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetTotalView
import com.tokopedia.play.widget.ui.model.PlayWidgetVideoUiModel
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.play.widget.ui.type.PlayWidgetPromoType
import javax.inject.Inject

/**
 * Created by meyta.taliti on 11/08/23.
 */
class FeedBrowseMapper @Inject constructor() {

    fun mapTitle(response: FeedXHeaderResponse): String {
        return response.feedXHeaderData.data.browse.title
    }

    fun mapSlots(response: FeedXHomeEntity): List<FeedBrowseUiModel> {
        return response.items.map { item ->
            FeedBrowseUiModel.Channel(
                id = item.id,
                title = item.title,
                extraParams = mapOf(
                    "group" to item.type
                ),
                chipUiState = ChipUiState.Placeholder,
                channelUiState = ChannelUiState.Placeholder
            )
        }
    }

    fun mapWidget(response: WidgetSlot): FeedBrowseItemUiModel {
        val data = response.playGetContentSlot.data
        val firstWidget = data.first()
        return when(firstWidget.type) {
            "tabMenu" -> {
                ChipUiState.Data(mapChips(firstWidget))
            }
            "channelBlock" -> {
                ChannelUiState.Data(mapChannel(firstWidget))
            }
            else -> ChannelUiState.Error(IllegalStateException())
        }
    }

    private fun mapChips(data: Content): List<FeedBrowseChipUiModel> {
        return data.items.map { item ->
            FeedBrowseChipUiModel(
                id = item.id,
                label = item.label,
                extraParams = mapOf(
                    "group" to item.group,
                    "source_type" to item.sourceType,
                    "source_id" to item.sourceId
                ),
                isSelected = false
            )
        }
    }

    private fun mapChannel(data: Content): List<PlayWidgetChannelUiModel> {
        return data.items.map { item ->
            mapChannel(
                channelType = PlayWidgetChannelType.getByValue(item.airTime),
                totalView = item.stats.view.formatted,
                coverUrl = item.coverUrl
            )
        }
    }

    private fun mapChannel(
        channelType: PlayWidgetChannelType,
        totalView: String,
        coverUrl: String,
    ): PlayWidgetChannelUiModel {
        return PlayWidgetChannelUiModel(
            channelId = "1",
            title = "",
            appLink = "",
            startTime = "",
            totalView = PlayWidgetTotalView(totalView, true),
            promoType = PlayWidgetPromoType.NoPromo,
            reminderType = PlayWidgetReminderType.NotReminded,
            partner = PlayWidgetPartnerUiModel.Empty,
            video = PlayWidgetVideoUiModel.Empty.copy(coverUrl = coverUrl),
            channelType = channelType,
            hasGame = false,
            share = PlayWidgetShareUiModel("", false),
            performanceSummaryLink = "",
            poolType = "",
            recommendationType = "",
            hasAction = false,
            products = emptyList(),
            shouldShowPerformanceDashboard = false,
            channelTypeTransition = PlayWidgetChannelTypeTransition(prevType = null, currentType = PlayWidgetChannelType.Unknown),
            gridType = PlayGridType.Unknown,
            extras = emptyMap()
        )
    }
}
