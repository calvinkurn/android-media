package com.tokopedia.videoTabComponent.view

import com.tokopedia.videoTabComponent.domain.model.data.PlaySlotTabMenuUiModel

interface PlaySlotTabCallback {
    fun clickTabMenu(item: PlaySlotTabMenuUiModel.Item)

    fun impressTabMenu(item: PlaySlotTabMenuUiModel.Item)
}