package com.tokopedia.videoTabComponent.domain.model.data

import com.google.gson.annotations.SerializedName

data class PlayVideoStream(
    @SerializedName("id")
    var id: String = "",
    @SerializedName("orientation")
    var orientation: String = "",
    @SerializedName("type")
    var type: String = "",
    @SerializedName("cover_url")
    var cover_url: String = "",
    @SerializedName("stream_source")
    var stream_source: String = "",
    @SerializedName("autoplay")
    var autoplay: Boolean = true
)
