package com.tokopedia.media.picker.ui.observer

import com.tokopedia.picker.common.observer.EventState
import com.tokopedia.picker.common.uimodel.MediaUiModel

sealed class EventPickerState : EventState {
    class CameraCaptured(
        override val data: MediaUiModel?
    ) : EventPickerState(), OnMediaAddedEvent

    class SelectionAdded(
        override val data: MediaUiModel?
    ) : EventPickerState(), OnMediaAddedEvent

    class SelectionChanged(val data: List<MediaUiModel>): EventPickerState()

    class SelectionRemoved(val media: MediaUiModel): EventPickerState()
}