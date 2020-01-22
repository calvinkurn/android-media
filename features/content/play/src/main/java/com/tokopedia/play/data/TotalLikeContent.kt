package com.tokopedia.play.data

import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 2019-12-10.
 */
data class TotalLikeContent(
        @SerializedName("error")
        val error: String = "",
        @SerializedName("data")
        val data: Data? = null
) {
    data class Data(
            @SerializedName("like")
            val like: Like = Like()
    )

    data class Like(
            @SerializedName("fmt")
            val fmt: String = "",

            @SerializedName("value")
            val value: Int = 0
    )

    data class Response(
            @SerializedName("feedGetLikeContent")
            val totalLikeContent: TotalLikeContent = TotalLikeContent()
    )
}