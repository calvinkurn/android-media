package com.tokopedia.play.data

import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 2019-12-10.
 */
data class LikeContent(
        @SerializedName("error")
        val error: String = "",
        @SerializedName("data")
        val data: Data? = null
) {
    data class Data(
            @SerializedName("success")
            val success: Int = 0
    )

    data class Response(
            @SerializedName("do_like_kol_post")
            val doLikeKolPost: LikeContent = LikeContent()
    )

    data class DataResponse(
            @SerializedName("data")
            val response: Response = Response()
    )

}