package com.tokopedia.play.data

import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 2019-12-10.
 */
data class FollowShop(
        @SerializedName("success")
        val success: Boolean = false,
        @SerializedName("message")
        val message: String = "",
        @SerializedName("isFirstTime")
        val isFirstTime: Boolean = false
) {
    data class Data(
            @SerializedName("data")
            val data: Response = Response()
    )

    data class Response(
            @SerializedName("followShop")
            val followShop: FollowShop = FollowShop()
    )

}