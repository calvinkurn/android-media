package com.tokopedia.play.data

import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 2019-12-10.
 */
data class IsLikedContent(
        @SerializedName("error")
        val error: String = "",
        @SerializedName("data")
        val data: Data? = null
) {
    data class Data(
            @SerializedName("isLike")
            val isLike: Boolean = false
    )

    data class Response(
            @SerializedName("feedGetIsLikePost")
            val isLikedContent: IsLikedContent = IsLikedContent()
    )
}