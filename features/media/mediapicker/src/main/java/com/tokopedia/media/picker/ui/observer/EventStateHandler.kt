package com.tokopedia.media.picker.ui.observer

import com.tokopedia.media.common.uimodel.MediaUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

fun statePublished(eventState: EventState) {
    EventFlowFactory.emit(eventState)
}

fun stateOnCameraCapturePublished(element: MediaUiModel) {
    statePublished(EventState.CameraCaptured(element))
}

fun stateOnChangePublished(elements: List<MediaUiModel>) {
    statePublished(EventState.SelectionChanged(elements))
}

fun stateOnAddPublished(element: MediaUiModel) {
    statePublished(EventState.SelectionAdded(element))
}

fun stateOnRemovePublished(element: MediaUiModel) {
    statePublished(EventState.SelectionRemoved(element))
}

suspend fun Flow<EventState>.observe(
    onChanged: (List<MediaUiModel>) -> Unit = {},
    onRemoved: (MediaUiModel) -> Unit = {},
    onAdded: (MediaUiModel) -> Unit = {},
    onCollection: () -> Unit = {}
) {
    this.collect {
        when (it) {
            is EventState.SelectionChanged -> onChanged(it.data)
            is EventState.SelectionRemoved -> onRemoved(it.media)

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