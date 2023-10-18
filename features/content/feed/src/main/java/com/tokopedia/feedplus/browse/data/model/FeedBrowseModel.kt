package com.tokopedia.feedplus.browse.data.model

import com.tokopedia.feedplus.browse.presentation.model.ItemListState
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel

/**
 * Created by kenny.hadisaputra on 13/10/23
 */
internal sealed interface FeedBrowseModel {

    val slotId: String
    val title: String

    data class ChannelsWithMenus(
        override val slotId: String,
        override val title: String,
        val group: String,
        val menus: Map<WidgetMenuModel, ItemListState<PlayWidgetChannelUiModel>>,
        val selectedMenuId: String,
        val type: Type,
    ) : FeedBrowseModel {

        enum class Type {
            ChannelBlock,
            ChannelRecommendation,
            Unknown,
        }
    }

    data class InspirationBanner(
        override val slotId: String,
        override val title: String,
        val identifier: String,
        val bannerList: List<BannerWidgetModel>,
    ) : FeedBrowseModel
}

internal data class WidgetMenuModel(
    val id: String,
    val label: String,
    val group: String,
    val sourceType: String,
    val sourceId: String,
) {

    val isValid: Boolean = id.isNotBlank()
    companion object {
        val Default: WidgetMenuModel
            get() = WidgetMenuModel(
                id = "",
                label = "",
                group = "",
                sourceType = "",
                sourceId = "",
            )
    }
}

internal data class BannerWidgetModel(
    val title: String,
    val imageUrl: String,
    val appLink: String,
)
