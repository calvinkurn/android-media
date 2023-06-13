package com.tokopedia.media.picker.ui.publisher

import com.tokopedia.picker.common.uimodel.MediaUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

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
                if (it is MediaAddState) {
                    it.data?.let { media ->
                        onAdded(media)
                    }
                }
            }
        }

        onCollection()
    }
}
