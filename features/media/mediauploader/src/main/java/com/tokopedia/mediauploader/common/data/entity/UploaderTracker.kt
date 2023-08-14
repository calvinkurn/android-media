package com.tokopedia.mediauploader.common.data.entity

import com.tokopedia.mediauploader.video.data.entity.VideoInfo
import java.io.File

data class UploaderTracker(
    var originalVideoPath: String = "",
    var compressedVideoPath: String = "",
    var startCompressedTime: Long = 0,
    var endCompressedTime: Long = 0,
    var startUploadTime: Long = 0,
    var endUploadTime: Long = 0,
    var videoOriginalSize: String = "",
    var videoCompressedSize: String = "",
    var originalVideoMetadata: VideoInfo? = null,
    var compressedVideoMetadata: VideoInfo? = null
) {

    fun isCompressedFileExist(): Boolean {
        return File(compressedVideoPath).exists()
    }
}

