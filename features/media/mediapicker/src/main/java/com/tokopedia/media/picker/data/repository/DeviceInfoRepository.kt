package com.tokopedia.media.picker.data.repository

import android.os.Environment
import android.os.StatFs
import javax.inject.Inject

interface DeviceInfoRepository {
    fun isDeviceStorageAlmostFull(minStorageThreshold: Long): Boolean
}

open class DeviceInfoRepositoryImpl @Inject constructor() : DeviceInfoRepository {

    override fun isDeviceStorageAlmostFull(minStorageThreshold: Long): Boolean {
        val userData = Environment.getDataDirectory()
        if (!userData.exists()) return false

        val stats = StatFs(userData.path)
        val blockSize = stats.blockSizeLong
        val availableBlocks = stats.availableBlocksLong

        val result = availableBlocks * blockSize

        return result < minStorageThreshold
    }
}
