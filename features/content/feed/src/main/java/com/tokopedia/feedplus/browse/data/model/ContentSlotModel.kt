package com.tokopedia.feedplus.browse.data.model

import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel

/**
 * Created by kenny.hadisaputra on 13/10/23
 */
internal sealed interface ContentSlotModel {

    data class TabMenus(
        val menus: List<WidgetMenuModel>
    ) : ContentSlotModel

    data class ChannelBlock(
        val title: String,
        val channels: List<PlayWidgetChannelUiModel>,
        val config: PlayWidgetConfigUiModel,
        val nextCursor: String
    ) : ContentSlotModel {
        val hasNextPage: Boolean = nextCursor.isNotBlank()
    }

    data class NoData(val nextCursor: String) : ContentSlotModel {
        val hasNextPage: Boolean = nextCursor.isNotBlank()
    }
}
