package com.tokopedia.media.picker.common.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.media.picker.data.repository.DeviceInfoRepository

class TestDeviceInfoRepository constructor(
    dispatcher: CoroutineDispatchers
) : DeviceInfoRepository(dispatcher) {

    var isStorageAlmostFull = false

    override fun execute(param: Long): Boolean {
        return isStorageAlmostFull
    }

}