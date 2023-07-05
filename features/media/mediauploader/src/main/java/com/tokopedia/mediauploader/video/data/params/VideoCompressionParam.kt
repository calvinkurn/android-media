package com.tokopedia.mediauploader.video.data.params

data class VideoCompressionParam(
    val sourceId: String,
    val videoPath: String,
    val bitrate: Int,
    val resolution: Int
)
