package com.tokopedia.feedplus.browse.data.model

import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel

/**
 * Created by kenny.hadisaputra on 13/10/23
 */
internal sealed interface ContentSlotModel {

    data class TabMenus(
        val menu: List<WidgetMenuModel>
    ) : ContentSlotModel

    data class ChannelBlock(
        val channels: List<PlayWidgetChannelUiModel>,
        val nextCursor: String,
    ) : ContentSlotModel {
        val hasNextPage: Boolean = nextCursor.isNotBlank()
    }

    data class NoData(val nextCursor: String) : ContentSlotModel {
        val hasNextPage: Boolean = nextCursor.isNotBlank()
    }
}
