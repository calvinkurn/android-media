package com.tokopedia.feedplus.browse.data.model

import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseChannelListState
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel

/**
 * Created by kenny.hadisaputra on 13/10/23
 */
internal sealed interface FeedBrowseSlotUiModel {

    val slotId: String
    val title: String

    data class ChannelsWithMenus(
        override val slotId: String,
        override val title: String,
        val group: String,
        val menus: Map<WidgetMenuModel, FeedBrowseChannelListState<PlayWidgetChannelUiModel>>,
        val selectedMenuId: String
    ) : FeedBrowseSlotUiModel {

        companion object {
            val Empty = ChannelsWithMenus(
                slotId = "",
                title = "",
                group = "",
                menus = emptyMap(),
                selectedMenuId = ""
            )
        }
    }

    data class InspirationBanner(
        override val slotId: String,
        override val title: String,
        val identifier: String,
        val bannerList: List<BannerWidgetModel>,
        val isLoading: Boolean,
    ) : FeedBrowseSlotUiModel

    data class Creators(
        override val slotId: String,
        override val title: String,
        val identifier: String,
        val creatorList: FeedBrowseChannelListState<PlayWidgetChannelUiModel>
    ) : FeedBrowseSlotUiModel
}

internal data class WidgetMenuModel(
    val id: String,
    val label: String,
    val group: String,
    val sourceType: String,
    val sourceId: String
) {

    val isValid: Boolean = id.isNotBlank()
    companion object {
        val Empty = WidgetMenuModel(
            id = "",
            label = "",
            group = "",
            sourceType = "",
            sourceId = ""
        )
    }
}

internal data class BannerWidgetModel(
    val title: String,
    val imageUrl: String,
    val appLink: String
)
