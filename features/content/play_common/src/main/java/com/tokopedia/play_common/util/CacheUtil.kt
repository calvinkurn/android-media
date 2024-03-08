package com.tokopedia.play_common.util

import com.tokopedia.utils.file.FileUtil

/**
 * Created By : Jonathan Darwin on January 30, 2024
 */
object CacheUtil {

    fun deleteFileFromCache(filePath: String) {
        if (filePath.isNotBlank() && filePath.contains(FileUtil.getTokopediaInternalDirectory(null).path)) {
            FileUtil.deleteFile(filePath)
        }
    }
}
