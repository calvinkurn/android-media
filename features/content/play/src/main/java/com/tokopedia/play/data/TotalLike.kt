package com.tokopedia.play.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.play.data.multiplelikes.MultipleLikeConfig


/**
 * Created by mzennis on 2019-12-05.
 */

data class TotalLike(
        @SerializedName("total_like")
        val totalLike: Long = 0,

        @SerializedName("total_like_formatted")
        var totalLikeFormatted: String = "",
)