package com.tokopedia.mediauploader.video.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VideoCompression(
    @Expose @SerializedName("enable_compression") var isCompressionEnabled: Boolean,
    @Expose @SerializedName("compress_threshold_mb") var thresholdInMb: Int,
    @Expose @SerializedName("max_bitrate_bps") var maxBitrateBps: Int,
    @Expose @SerializedName("max_resolution") var maxResolution: Int,
    @Expose @SerializedName("max_fps") var maxFps: Int,
)
