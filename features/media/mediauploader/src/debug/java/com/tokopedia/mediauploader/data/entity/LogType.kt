package com.tokopedia.mediauploader.data.entity

sealed class LogType {
    object Welcome : LogType()
    object FileInfo : LogType()
    object CompressInfo : LogType()
    object UploadResult : LogType()

    companion object {
        fun map(type: LogType): String {
            return when (type) {
                is Welcome -> "Media Uploader!"
                is FileInfo -> "File Info"
                is CompressInfo -> "Compression Info"
                is UploadResult -> "Upload Result"
            }
        }
    }
}
