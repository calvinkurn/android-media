package com.tokopedia.play.data

import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 2019-12-05.
 */

data class TotalLike(
        @SerializedName("total_click")
        val totalClick: String = ""
)