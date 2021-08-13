package com.tokopedia.play.data

import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 2019-12-05.
 */

data class TotalLike(
        @SerializedName("total_like")
        val totalLike: Long = 0,

        @SerializedName("total_like_formatted")
        var totalLikeFormatted: String = ""
)