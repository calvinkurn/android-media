package com.tokopedia.mediauploader

import com.tokopedia.mediauploader.common.state.ProgressUploader
import com.tokopedia.mediauploader.common.util.fileExtension

interface UploaderManager {

    fun setProgressUploader(progress: ProgressUploader?)

    fun allowedExt(filePath: String, allowedExtensions: String): Boolean {
        val fileExt = filePath
            .fileExtension()
            .lowercase()

        val allowed = allowedExtensions
            .split(",")
            .map { it.drop(NUM_EXT_DROP_FIRST) }

        return allowed.contains(fileExt)
    }

    companion object {
        private const val NUM_EXT_DROP_FIRST = 1
    }
}
