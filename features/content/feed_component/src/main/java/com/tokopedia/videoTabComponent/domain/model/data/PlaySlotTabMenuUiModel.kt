package com.tokopedia.videoTabComponent.domain.model.data

import com.tokopedia.play.widget.analytic.ImpressionableModel
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel

open class PlayFeedUiModel

data class PlayWidgetJumboUiModel(
    val model: PlayWidgetUiModel
): PlayFeedUiModel()

data class PlayWidgetLargeUiModel(
    var model: PlayWidgetUiModel
): PlayFeedUiModel()

data class PlayWidgetMediumUiModel(
    val model: PlayWidgetUiModel
): PlayFeedUiModel()


data class PlaySlotTabMenuUiModel(val items: List<Item>) : PlayFeedUiModel() {
    data class Item(
        val id: String,
        val label: String,
        val iconUrl: String,
        val group: String,
        val sourceType: String,
        val sourceId: String,
        val slugID: String,
        var isSelected: Boolean = false
    ) : ImpressionableModel {
        override val impressHolder: com.tokopedia.kotlin.model.ImpressHolder
            get() = com.tokopedia.kotlin.model.ImpressHolder()
    }
}


