package com.tokopedia.videoTabComponent.callback

import com.tokopedia.videoTabComponent.domain.model.data.PlaySlotTabMenuUiModel

interface PlaySlotTabCallback {
    fun clickTabMenu(item: PlaySlotTabMenuUiModel.Item,position: Int)

    fun impressTabMenu(item: PlaySlotTabMenuUiModel.Item)
}