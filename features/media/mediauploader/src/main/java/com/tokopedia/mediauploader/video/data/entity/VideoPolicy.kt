package com.tokopedia.mediauploader.video.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VideoPolicy(
    @Expose @SerializedName("max_file_size") var maxFileSize: Int = 0,
    @Expose @SerializedName("allowed_ext") var extension: String = "",
    @Expose @SerializedName("simple_upload_size_threshold_mb") var thresholdSize: Int? = null,
    @Expose @SerializedName("big_upload_chunk_size_mb") var largeChunkSize: Int? = null,
    @Expose @SerializedName("big_upload_max_concurrent") var largeMaxConcurrent: Int? = null,
) {

    fun thresholdSizeOfVideo(): Int {
        return thresholdSize?: 10
    }

}