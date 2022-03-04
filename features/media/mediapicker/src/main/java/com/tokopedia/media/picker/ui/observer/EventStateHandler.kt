package com.tokopedia.media.picker.ui.observer

import com.tokopedia.media.common.observer.EventFlowFactory
import com.tokopedia.media.common.observer.EventState
import com.tokopedia.media.common.uimodel.MediaUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

fun statePublished(eventState: EventPickerState) {
    EventFlowFactory.emit(eventState)
}

fun stateOnCameraCapturePublished(element: MediaUiModel) {
    statePublished(EventPickerState.CameraCaptured(element))
}

fun stateOnChangePublished(elements: List<MediaUiModel>) {
    statePublished(EventPickerState.SelectionChanged(elements))
}

fun stateOnAddPublished(element: MediaUiModel) {
    statePublished(EventPickerState.SelectionAdded(element))
}

fun stateOnRemovePublished(element: MediaUiModel) {
    statePublished(EventPickerState.SelectionRemoved(element))
}

suspend fun Flow<EventState>.observe(
    onChanged: (List<MediaUiModel>) -> Unit = {},
    onRemoved: (MediaUiModel) -> Unit = {},
    onAdded: (MediaUiModel) -> Unit = {},
    onCollection: () -> Unit = {}
) {
    this.collect {
        when (it) {
            is EventPickerState.SelectionChanged -> onChanged(it.data)
            is EventPickerState.SelectionRemoved -> onRemoved(it.media)

            /*
             * the else statement covering the data from
             * [CameraCaptured] and [SelectionAdded] state event
             * */
            else -> {
                if (it is OnMediaAddedEvent) {
                    it.data?.let { media ->
                        onAdded(media)
                    }
                }
            }
        }

        onCollection()
    }
}