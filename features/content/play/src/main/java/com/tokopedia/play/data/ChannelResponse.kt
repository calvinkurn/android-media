package com.tokopedia.play.data

import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 2019-12-03.
 */

data class ChannelResponse(

        @SerializedName("header")
        var header: Header = Header(),

        @SerializedName("data")
        var data: Data = Data()

        ) {
    data class Header(
            @SerializedName("")
            var process_time: String = "",
            @SerializedName("messages")
            var messages: List<String> = emptyList(),
            @SerializedName("reason")
            var reason: String = "",
            @SerializedName("error_code")
            var error_code: String = ""
    )

    data class Data(
            @SerializedName("channel")
            var channel: Channel = Channel()
    )
}