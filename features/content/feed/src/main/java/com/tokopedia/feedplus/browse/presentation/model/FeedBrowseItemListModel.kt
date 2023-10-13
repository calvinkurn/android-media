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
        val selectedId: String,
        val chips: List<WidgetMenuModel>,
    ) : FeedBrowseItemListModel
    data class HorizontalChannels(
        override val slotId: String,
        val channels: List<PlayWidgetChannelUiModel>,
    ) : FeedBrowseItemListModel
    data class Banner(
        override val slotId: String,
        val banner: BannerWidgetModel,
    ) : FeedBrowseItemListModel
}
