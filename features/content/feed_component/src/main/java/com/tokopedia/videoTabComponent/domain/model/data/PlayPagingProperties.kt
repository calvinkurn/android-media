package com.tokopedia.play.widget.sample.data

import com.google.gson.annotations.SerializedName

data class PlayPagingProperties(
        @SerializedName("next_cursor")
        var next_cursor: String = "",
        @SerializedName("is_autoplay")
        var is_autoplay: Boolean = false,
        @SerializedName("max_autoplay_in_cell")
        var max_autoplay_in_cell: Int = 0,
)
