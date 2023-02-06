package com.tokopedia.media.picker.ui.observer

import com.tokopedia.picker.common.observer.EventFlowFactory
import com.tokopedia.picker.common.observer.EventState
import com.tokopedia.picker.common.uimodel.MediaUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

fun statePublished(eventState: EventPickerState) {
    EventFlowFactory.emit(eventState)
}

fun stateOnCameraCapturePublished(element: MediaUiModel, key: String) {
    statePublished(EventPickerState.CameraCaptured(element, key))
}

fun stateOnChangePublished(elements: List<MediaUiModel>, key: String) {
    statePublished(EventPickerState.SelectionChanged(elements, key))
}

fun stateOnAddPublished(element: MediaUiModel, key: String) {
    if (element.file?.exists() == false) return
    statePublished(EventPickerState.SelectionAdded(element, key))
}

fun stateOnRemovePublished(element: MediaUiModel, key: String) {
    statePublished(EventPickerState.SelectionRemoved(element, key))
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
