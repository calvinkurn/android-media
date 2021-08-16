package com.tokopedia.play.data.interactive

import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 30/06/21
 */
data class PostInteractiveTapResponse(
        @SerializedName("playInteractiveUserTapSession")
        val data: Data = Data()
) {

    data class Data(
            @SerializedName("header")
            val header: Header = Header()
    )

    data class Header(
            @SerializedName("status")
            val status: Int = 0,

            @SerializedName("message")
            val message: String = ""
    )
}