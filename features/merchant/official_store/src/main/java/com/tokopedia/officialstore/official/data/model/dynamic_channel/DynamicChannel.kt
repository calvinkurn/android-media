package com.tokopedia.officialstore.official.data.model.dynamic_channel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DynamicChannel(
        @Expose @SerializedName("channels")
        val channels: MutableList<Channel> = mutableListOf()
) {
    data class Response(
            @Expose @SerializedName("dynamicHomeChannel")
            val dynamicHomeChannel: DynamicChannel = DynamicChannel()
    )
}
