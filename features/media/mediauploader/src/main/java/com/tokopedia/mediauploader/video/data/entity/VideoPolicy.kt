package com.tokopedia.mediauploader.video.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VideoPolicy(
    @Expose @SerializedName("max_file_size") var maxFileSize: Int = 0,
    @Expose @SerializedName("allowed_ext") var extension: String = ""
)