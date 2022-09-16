package com.tokopedia.mediauploader.common.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.mediauploader.image.data.entity.ImagePolicy
import com.tokopedia.mediauploader.video.data.entity.VideoPolicy

data class SourcePolicy(
    @Expose @SerializedName("source_type") val sourceType: String = "",
    @Expose @SerializedName("host") val host: String = "",
    @Expose @SerializedName("timeout") val timeOut: Int = 60,
    @Expose @SerializedName("image_policy") val imagePolicy: ImagePolicy? = ImagePolicy(),
    @Expose @SerializedName("vod_policy") val videoPolicy: VideoPolicy? = VideoPolicy()
)