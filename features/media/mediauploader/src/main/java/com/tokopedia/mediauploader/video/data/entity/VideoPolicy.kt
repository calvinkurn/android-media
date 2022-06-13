package com.tokopedia.mediauploader.video.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.mediauploader.common.util.mbToBytes

data class VideoPolicy(
    @Expose @SerializedName("max_file_size") var maxFileSize: Int = 0,
    @Expose @SerializedName("allowed_ext") var extension: String = "",
    @Expose @SerializedName("simple_upload_size_threshold_mb") var thresholdSize: Int? = null,
    @Expose @SerializedName("big_upload_chunk_size_mb") var largeChunkSize: Int? = null,
    @Expose @SerializedName("big_upload_max_concurrent") var largeMaxConcurrent: Int? = null,
    @Expose @SerializedName("timeout_transcode") var timeOutTranscode: Int? = null,
    @Expose @SerializedName("retry_interval") var retryInterval: Int? = null,
) {

    fun thresholdSizeOfVideo(): Int {
        return thresholdSize?: 10
    }

    fun chunkSizePerFileInBytes(): Int {
        return (largeChunkSize?: 5).mbToBytes()
    }

    fun timeOutOfTranscode(): Int {
        return timeOutTranscode?: 600
    }

    fun retryIntervalInSec(): Long {
        return (retryInterval?: 5) * 1000.toLong()
    }

}