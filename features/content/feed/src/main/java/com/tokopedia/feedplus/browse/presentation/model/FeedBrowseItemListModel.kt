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
    data class Chips(
        override val slotId: String,
        val chips: List<Model>,
    ) : FeedBrowseItemListModel {

        data class Model(
            val menu: WidgetMenuModel,
            val isSelected: Boolean,
        )
    }
    data class HorizontalChannels(
        override val slotId: String,
        val itemState: ItemListState<PlayWidgetChannelUiModel>,
    ) : FeedBrowseItemListModel

    data class Banner(
        override val slotId: String,
        val banner: BannerWidgetModel,
    ) : FeedBrowseItemListModel

    data class InspirationCard(
        override val slotId: String,
        val item: PlayWidgetChannelUiModel,
    ) : FeedBrowseItemListModel
}
