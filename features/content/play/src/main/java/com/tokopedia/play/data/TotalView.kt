package com.tokopedia.play.data

import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 2019-12-05.
 */

data class TotalView(
        @SerializedName("total_view")
        val totalView: Long = 0,

        @SerializedName("total_view_formatted")
        val totalViewFormatted: String = "0"
)