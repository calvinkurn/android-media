package com.tokopedia.media.picker.ui.activity.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.media.picker.utils.EventBusFactory
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PickerViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers
): ViewModel() {

    val uiEvent = EventBusFactory
        .subscriber(viewModelScope)
        .flowOn(dispatchers.computation)

}