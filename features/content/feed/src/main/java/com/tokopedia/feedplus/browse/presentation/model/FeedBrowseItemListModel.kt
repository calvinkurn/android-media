package com.tokopedia.feedplus.browse.presentation.model

import com.tokopedia.feedplus.browse.data.model.BannerWidgetModel
import com.tokopedia.feedplus.browse.data.model.WidgetMenuModel
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel

/**
 * Created by kenny.hadisaputra on 13/10/23
 */
internal sealed interface FeedBrowseItemListModel {

    val slotId: String

    data class Title(
        override val slotId: String,
        val title: String,
    ) : FeedBrowseItemListModel

    sealed interface Chips : FeedBrowseItemListModel {
        data class Item(override val slotId: String, val chips: List<ChipsModel>) : Chips
        object Placeholder : Chips {
            override val slotId: String = ""
        }
    }

    data class HorizontalChannels(
        override val slotId: String,
        val itemState: ItemListState<PlayWidgetChannelUiModel>,
    ) : FeedBrowseItemListModel

    data class Banner(
        override val slotId: String,
        val banner: BannerWidgetModel,
    ) : FeedBrowseItemListModel

    sealed interface InspirationCard : FeedBrowseItemListModel {
        data class Item(
            override val slotId: String,
            val item: PlayWidgetChannelUiModel,
        ) : InspirationCard

        object Placeholder : InspirationCard {
            override val slotId: String = ""
        }
    }
}

internal data class ChipsModel(
    val menu: WidgetMenuModel,
    val isSelected: Boolean,
)
