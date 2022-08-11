package com.tokopedia.media.picker.ui.observer

import com.tokopedia.picker.common.uimodel.MediaUiModel

interface OnMediaAddedEvent {
    val data: MediaUiModel?
}