package com.tokopedia.mediauploader

data class DebugUploaderParam(
    val sourceId: String,
    val shouldCompress: Boolean,
    val withTranscode: Boolean
) {

    companion object {
        fun default(): DebugUploaderParam {
            return DebugUploaderParam(
                sourceId = "exwbZW", // video upload
                shouldCompress = true,
                withTranscode = false
            )
        }
    }
}
