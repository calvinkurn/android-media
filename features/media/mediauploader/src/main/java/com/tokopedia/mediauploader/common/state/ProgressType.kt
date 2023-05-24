package com.tokopedia.mediauploader.common.state

sealed class ProgressType {
    object Compression: ProgressType() {
        override fun toString() = "Compress"
    }

    object Upload: ProgressType() {
        override fun toString() = "Upload"
    }
}
