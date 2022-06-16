package com.tokopedia.mediauploader.image.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ImagePolicy(
    @Expose @SerializedName("max_file_size") var maxFileSize: Int = 0,
    @Expose @SerializedName("max_res") var maximumRes: MediaRes = MediaRes(),
    @Expose @SerializedName("min_res") var minimumRes: MediaRes = MediaRes(),
    @Expose @SerializedName("allowed_ext") var extension: String = ""
)