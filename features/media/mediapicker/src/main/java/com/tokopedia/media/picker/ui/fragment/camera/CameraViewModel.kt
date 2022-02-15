package com.tokopedia.media.picker.ui.fragment.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.media.picker.ui.observer.EventFlowFactory
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CameraViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers
): ViewModel() {

    val uiEvent = EventFlowFactory
        .subscriber(viewModelScope)
        .flowOn(dispatchers.computation)

}