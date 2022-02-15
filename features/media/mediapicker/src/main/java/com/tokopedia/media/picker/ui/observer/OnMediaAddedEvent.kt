package com.tokopedia.media.picker.ui.observer

import com.tokopedia.media.common.uimodel.MediaUiModel

interface OnMediaAddedEvent {
    val data: MediaUiModel?
}