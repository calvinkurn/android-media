package com.tokopedia.media.picker.ui.activity.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.media.common.utils.ParamCacheManager
import com.tokopedia.media.picker.data.repository.DeviceInfoRepository
import com.tokopedia.picker.common.observer.EventFlowFactory
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PickerViewModel @Inject constructor(
    private val deviceInfo: DeviceInfoRepository,
    private val param: ParamCacheManager,
    dispatchers: CoroutineDispatchers
): ViewModel() {

    val uiEvent = EventFlowFactory
        .subscriber(viewModelScope)
        .flowOn(dispatchers.computation)

    fun isDeviceStorageFull(): Boolean {
        return deviceInfo.isStorageFullWithThreshold(
            param.get().minStorageThreshold()
        )
    }

}