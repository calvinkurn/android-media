package com.tokopedia.media.picker.ui.publisher

import com.tokopedia.picker.common.uimodel.MediaUiModel

interface EventState {
    object Idle : EventState
}

interface MediaAddState {
    val data: MediaUiModel?
}

sealed class EventPickerState : EventState {
    class CameraCaptured(override val data: MediaUiModel?) : EventPickerState(), MediaAddState
    class SelectionAdded(override val data: MediaUiModel?) : EventPickerState(), MediaAddState
    class SelectionChanged(val data: List<MediaUiModel>) : EventPickerState()
    class SelectionRemoved(val media: MediaUiModel) : EventPickerState()
}
