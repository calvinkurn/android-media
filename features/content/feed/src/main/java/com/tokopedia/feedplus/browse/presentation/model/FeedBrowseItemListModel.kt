package com.tokopedia.feedplus.browse.presentation.model

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.browse.data.model.AuthorWidgetModel
import com.tokopedia.feedplus.browse.data.model.BannerWidgetModel
import com.tokopedia.feedplus.browse.data.model.WidgetMenuModel
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel

/**
 * Created by kenny.hadisaputra on 13/10/23
 */
internal sealed interface FeedBrowseItemListModel {

    val slotInfo: SlotInfo

    data class Title(
        override val slotInfo: SlotInfo,
        val title: String,
        val isLoading: Boolean = false
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
                FeedBrowseChannelListState.initLoading()
            )
        }
    }

    sealed interface Banner : FeedBrowseItemListModel {
        data class Item(
            override val slotInfo: SlotInfo,
            val banner: BannerWidgetModel,
            val index: Int
        ) : Banner

        object Placeholder : Banner {
            override val slotInfo: SlotInfo = SlotInfo.Empty
        }
    }

    sealed interface InspirationCard : FeedBrowseItemListModel {
        data class Item(
            override val slotInfo: SlotInfo,
            val item: PlayWidgetChannelUiModel,
            val config: PlayWidgetConfigUiModel,
            val index: Int
        ) : InspirationCard

        object Placeholder : InspirationCard {
            override val slotInfo: SlotInfo = SlotInfo.Empty
        }
    }

    data class HorizontalAuthors(
        override val slotInfo: SlotInfo,
        val items: List<AuthorWidgetModel>,
        val isLoading: Boolean = false
    ) : FeedBrowseItemListModel {

        companion object {
            val Loading: HorizontalAuthors
                get() = HorizontalAuthors(SlotInfo.Empty, emptyList(), true)
        }
    }

    object LoadingModel : FeedBrowseItemListModel {
        override val slotInfo: SlotInfo = SlotInfo.Empty
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
