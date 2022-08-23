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
        return thresholdSize?: THRESHOLD_VIDEO_FILE_SIZE
    }

    fun chunkSizePerFileInBytes(): Int {
        return (largeChunkSize?: THRESHOLD_LARGE_CHUNK_SIZE).mbToBytes()
    }

    fun timeOutOfTranscode(): Int {
        return timeOutTranscode?: TIME_OUT_TRANSCODE
    }

    fun retryIntervalInSec(): Long {
        return (retryInterval?: THRESHOLD_RETRY_INTERVAL) * RETRY_INTERVAL_TO_SEC.toLong()
    }

    companion object {
        private const val THRESHOLD_VIDEO_FILE_SIZE = 10
        private const val THRESHOLD_LARGE_CHUNK_SIZE = 5
        private const val THRESHOLD_RETRY_INTERVAL = 5

        private const val RETRY_INTERVAL_TO_SEC = 1000
        private const val TIME_OUT_TRANSCODE = 600
    }

}