package com.tokopedia.mediauploader.video.data.state

enum class TranscodingState(val status: String) {
    UPLOADING("uploading"),
    TRANSCODING("transcoding"),
    COMPLETED("completed"),
    FAILED("failed"),
    ABORTED("aborted"),
    UNKNOWN("unknown"),
}