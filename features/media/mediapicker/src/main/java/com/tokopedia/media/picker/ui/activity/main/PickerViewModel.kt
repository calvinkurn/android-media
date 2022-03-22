package com.tokopedia.media.picker.ui.activity.main

import android.os.Environment
import android.os.StatFs
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.picker.common.ParamCacheManager
import com.tokopedia.picker.common.observer.EventFlowFactory
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PickerViewModel @Inject constructor(
    private val param: ParamCacheManager,
    dispatchers: CoroutineDispatchers
): ViewModel() {

    val uiEvent = EventFlowFactory
        .subscriber(viewModelScope)
        .flowOn(dispatchers.computation)

    fun isDeviceStorageFull(): Boolean {
        val userData = Environment.getDataDirectory()
        if (!userData.exists()) return false

        val stats = StatFs(userData.path)
        val blockSize = stats.blockSizeLong
        val availableBlocks = stats.availableBlocksLong

        val result = availableBlocks * blockSize

        return result < param.get().minStorageThreshold()
    }

}