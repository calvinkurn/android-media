package com.tokopedia.media.picker.data.repository

import android.os.Environment
import android.os.StatFs

interface DeviceInfoRepository {
    fun isStorageFullWithThreshold(threshold: Long): Boolean
}

class DeviceInfoRepositoryImpl : DeviceInfoRepository {

    override fun isStorageFullWithThreshold(threshold: Long): Boolean {
        val userData = Environment.getDataDirectory()
        if (!userData.exists()) return false

        val stats = StatFs(userData.path)
        val blockSize = stats.blockSizeLong
        val availableBlocks = stats.availableBlocksLong

        val result = availableBlocks * blockSize

        return result < threshold
    }

}