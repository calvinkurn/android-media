package com.tokopedia.media.picker.data.repository

import android.os.Environment
import android.os.StatFs
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.picker.common.base.BaseRepository

open class DeviceInfoRepository constructor(
    dispatcher: CoroutineDispatchers
) : BaseRepository<Long, Boolean>(dispatcher.main) {

    override fun execute(param: Long): Boolean {
        val userData = Environment.getDataDirectory()
        if (!userData.exists()) return false

        val stats = StatFs(userData.path)
        val blockSize = stats.blockSizeLong
        val availableBlocks = stats.availableBlocksLong

        val result = availableBlocks * blockSize

        return result < param
    }

}