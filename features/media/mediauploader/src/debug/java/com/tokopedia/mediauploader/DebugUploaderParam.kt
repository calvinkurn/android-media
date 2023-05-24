package com.tokopedia.mediauploader

data class DebugUploaderParam(
    val sourceId: String,
    val shouldCompress: Boolean,
    val withTranscode: Boolean
) {

    companion object {
        fun default(): DebugUploaderParam {
            return DebugUploaderParam(
                sourceId = "exwbZW",
                shouldCompress = true,
                withTranscode = false
            )
        }
    }
}
