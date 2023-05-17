package com.tokopedia.mediauploader.common.state

sealed class ProgressType {
    object Compression: ProgressType() {
        override fun toString() = "Compression"
    }

    object Upload: ProgressType() {
        override fun toString() = "Upload"
    }
}
