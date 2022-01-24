package com.tokopedia.picker.ui.fragment.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.picker.ui.uimodel.MediaUiModel
import com.tokopedia.picker.utils.EventBusFactory
import com.tokopedia.picker.utils.EventState
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CameraViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers
): ViewModel() {

    val uiEvent = EventBusFactory
        .subscriber(viewModelScope)
        .flowOn(dispatchers.computation)

    fun send(mediaUiModel: MediaUiModel) {
        EventBusFactory.emit(EventState.CameraCaptured(mediaUiModel))
    }

}