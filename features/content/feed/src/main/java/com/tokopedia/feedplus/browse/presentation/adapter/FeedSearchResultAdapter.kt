package com.tokopedia.feedplus.browse.presentation.adapter

import com.tokopedia.feedplus.browse.presentation.SearchTempDataModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemListModel
import com.tokopedia.feedplus.browse.presentation.model.SlotInfo
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelTypeTransition
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetPartnerUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetProduct
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.model.PlayWidgetShareUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetTotalView
import com.tokopedia.play.widget.ui.model.PlayWidgetVideoUiModel
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.play.widget.ui.type.PlayWidgetPromoType
import kotlinx.coroutines.CoroutineScope

internal class FeedSearchResultAdapter(
    coroutineScope: CoroutineScope
): FeedBrowseItemAdapter<SearchTempDataModel>(
    coroutineScope
) {
    fun setList(
        items: SearchTempDataModel,
        onCommit: () -> Unit = {}
    ) {
        submitList(
            items.mapToItems(),
            onCommit
        )
    }

    fun setLoadingState() {
        val placeholder = listOf<FeedBrowseItemListModel>(
            FeedBrowseItemListModel.InspirationCard.Placeholder,
            FeedBrowseItemListModel.InspirationCard.Placeholder,
            FeedBrowseItemListModel.InspirationCard.Placeholder,
            FeedBrowseItemListModel.InspirationCard.Placeholder
        )

        submitList(
            placeholder,
            {}
        )
    }

    override fun SearchTempDataModel.mapToItems(): List<FeedBrowseItemListModel> {
        val mapList = mutableListOf<FeedBrowseItemListModel>()

        val dummyPlayConfig = PlayWidgetConfigUiModel(
            false,
            0L,
            false,
            100, // maximum card with auto play
            100, // maximum video duration, only used for non-wifi user
            1000, // maximum video duration,
            10
        )

        resultList?.forEach { data ->
            val feedBrowseItem = FeedBrowseItemListModel.InspirationCard.Item(
                slotInfo = SlotInfo.Empty,
                item = getPlayWidgetVideo(data),
                config = dummyPlayConfig,
                0
            )
            mapList.add(feedBrowseItem)
        }

        return mapList
    }

    // Todo: will remove later
    private fun getPlayWidgetVideo(data: SearchTempDataModel.CardDetail): PlayWidgetChannelUiModel {
        val playWidgetVideo  = PlayWidgetVideoUiModel(
            id = "",
            true,
            data.imgUrl,
            ""
        )

        return PlayWidgetChannelUiModel(
            "channel id",
            data.title,
            "applink",
            "start time",
            PlayWidgetTotalView("10", true),
            PlayWidgetPromoType.Unknown,
            PlayWidgetReminderType.NotReminded,
            PlayWidgetPartnerUiModel.Empty,
            playWidgetVideo,
            PlayWidgetChannelType.Live,
            false,
            PlayWidgetShareUiModel.Empty,
            "performanceSummaryLink",
            "poolType",
            "recommendationType",
            false,
            listOf(PlayWidgetProduct("aa","bb","cc","dd","ee","ff")),
            false,
            PlayWidgetChannelTypeTransition(null, PlayWidgetChannelType.Live),
        )
    }
}
