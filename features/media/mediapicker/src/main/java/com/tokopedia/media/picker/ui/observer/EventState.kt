package com.tokopedia.media.picker.ui.observer

import com.tokopedia.media.common.uimodel.MediaUiModel

sealed class EventState {
    object Idle: EventState()

    class CameraCaptured(
        override val data: MediaUiModel?
    ) : EventState(), OnMediaAddedEvent

    class SelectionAdded(
        override val data: MediaUiModel?
    ) : EventState(), OnMediaAddedEvent

    class SelectionChanged(val data: List<MediaUiModel>): EventState()

    class SelectionRemoved(val media: MediaUiModel): EventState()
}
