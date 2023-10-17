package com.tokopedia.feedplus.data

import com.tokopedia.feedplus.browse.data.model.WidgetRequestModel
import com.tokopedia.feedplus.browse.presentation.model.ChannelUiState
import com.tokopedia.feedplus.browse.presentation.model.ChipUiState
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseChipUiModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseConfigUiModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiModel
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

/**
 * Created by meyta.taliti on 08/09/23.
 */
class FeedBrowseModelBuilder {

    fun buildTitle(): String = "this is title"

    fun buildWidgets(
        extraParam: WidgetRequestModel = WidgetRequestModel.Empty,
        chipUiState: ChipUiState = ChipUiState.Placeholder,
        channelUiState: ChannelUiState = ChannelUiState.Placeholder
    ): List<FeedBrowseUiModel.Channel> {
        return List(5) { index ->
            FeedBrowseUiModel.Channel(
                id = "$index",
                title = "sample title widget",
                extraParam = extraParam,
                chipUiState = chipUiState,
                channelUiState = channelUiState
            )
        }
    }

    fun buildExtraParam() = WidgetRequestModel.Empty

    fun buildWidgetChipItemModel(
        items: List<FeedBrowseChipUiModel> = buildChips()
    ) = ChipUiState.Data(
        items = items
    )

    fun buildChips(selectedIndex: Int = 0) = List(5) { index ->
        FeedBrowseChipUiModel(
            extraParam = WidgetRequestModel.Empty,
            id = "$index",
            label = "label $index",
            isSelected = index == selectedIndex
        )
    }

    fun buildWidgetChannelItemModel(
        items: List<PlayWidgetChannelUiModel> = buildChannelList()
    ) = ChannelUiState.Data(
        items = items,
        config = FeedBrowseConfigUiModel(
            data = PlayWidgetConfigUiModel.Empty,
            lastUpdated = 100000
        )
    )

    private fun buildChannelList() = List(5) { index ->
        PlayWidgetChannelUiModel(
            channelId = "$index",
            title = "title $index",
            appLink = "",
            startTime = "",
            totalView = PlayWidgetTotalView("10", true),
            promoType = PlayWidgetPromoType.NoPromo,
            reminderType = PlayWidgetReminderType.NotReminded,
            partner = PlayWidgetPartnerUiModel.Empty.copy(id = "1"),
            video = PlayWidgetVideoUiModel.Empty.copy(coverUrl = ""),
            channelType = PlayWidgetChannelType.Vod,
            hasGame = false,
            share = PlayWidgetShareUiModel.Empty,
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
