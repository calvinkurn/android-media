package com.tokopedia.play.widget.sample.data

import com.google.gson.annotations.SerializedName

data class PlayPinnedMessage(
        @SerializedName("id")
        var id: String = "",
        @SerializedName("title")
        var title: String = "",
        @SerializedName("message")
        var message: String = "",
        @SerializedName("image_url")
        var image_url: String = "",
        @SerializedName("redirect_url")
        var redirect_url: String = "",





)
