package com.tokopedia.videoTabComponent.domain.model.data

import com.tokopedia.play.widget.analytic.ImpressionableModel
import com.tokopedia.play.widget.ui.model.PlayFeedUiModel

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


