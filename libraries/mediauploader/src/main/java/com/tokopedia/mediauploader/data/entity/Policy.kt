package com.tokopedia.mediauploader.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Policy(
        @Expose @SerializedName("max_file_size") val maxFileSize: Int = 0,
        @Expose @SerializedName("max_res") val maximumRes: MediaRes = MediaRes(),
        @Expose @SerializedName("min_res") val minimumRes: MediaRes = MediaRes(),
        @Expose @SerializedName("allowed_ext") val extension: String = ""
)