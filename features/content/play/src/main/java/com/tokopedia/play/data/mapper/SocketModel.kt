package com.tokopedia.play.data.mapper

import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 2019-12-12.
 */
data class SocketModel(
        @SerializedName("socket_type")
        val type: String = ""
)