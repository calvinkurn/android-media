package com.tokopedia.play.widget.sample.data

import com.google.gson.annotations.SerializedName

data class PlayChannelShare(
        @SerializedName("text")
        var text: String = "",
        @SerializedName("redirect_url")
        var redirect_url: String = "",
        @SerializedName("use_short_url")
        var use_short_url: Boolean = false,
        @SerializedName("meta_title")
        var meta_title: Boolean = false,
        @SerializedName("meta_description")
        var meta_description: Boolean = false,
        @SerializedName("is_show_button")
        var is_show_button: Boolean = false,

)
