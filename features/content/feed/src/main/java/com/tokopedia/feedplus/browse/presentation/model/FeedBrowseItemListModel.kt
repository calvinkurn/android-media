package com.tokopedia.feedplus.browse.presentation.model

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.browse.data.model.BannerWidgetModel
import com.tokopedia.feedplus.browse.data.model.WidgetMenuModel
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel

/**
 * Created by kenny.hadisaputra on 13/10/23
 */
internal sealed interface FeedBrowseItemListModel {

    val slotInfo: SlotInfo

    data class Title(
        override val slotInfo: SlotInfo,
        val title: String,
        val isLoading: Boolean = false,
    ) : FeedBrowseItemListModel {

        companion object {
            val Loading = Title(SlotInfo.Empty, "", true)
        }
    }

    sealed interface Chips : FeedBrowseItemListModel {
        data class Item(
            override val slotInfo: SlotInfo,
            val chips: List<ChipsModel>
        ) : Chips
        object Placeholder : Chips {
            override val slotInfo: SlotInfo = SlotInfo.Empty
        }

        companion object {
            val Loading = Placeholder
        }
    }

    data class HorizontalChannels(
        override val slotInfo: SlotInfo,
        val menu: WidgetMenuModel,
        val itemState: FeedBrowseChannelListState<PlayWidgetChannelUiModel>
    ) : FeedBrowseItemListModel {

        companion object {
            val Loading = HorizontalChannels(
                SlotInfo.Empty,
                WidgetMenuModel.Empty,
                FeedBrowseChannelListState.initLoading(),
            )
        }
    }

    data class Banner(
        override val slotInfo: SlotInfo,
        val banner: BannerWidgetModel
    ) : FeedBrowseItemListModel

    sealed interface InspirationCard : FeedBrowseItemListModel {
        data class Item(
            override val slotInfo: SlotInfo,
            val item: PlayWidgetChannelUiModel
        ) : InspirationCard

        object Placeholder : InspirationCard {
            override val slotInfo: SlotInfo = SlotInfo.Empty
        }
    }
}

internal data class ChipsModel(
    val menu: WidgetMenuModel,
    val isSelected: Boolean
)

internal data class SlotInfo(
    val id: String,
    val title: String,
    val position: Int
) {
    companion object {
        val Empty = SlotInfo(
            id = "",
            title = "",
            position = RecyclerView.NO_POSITION
        )
    }
}
