package com.tokopedia.home.beranda.domain.gql

import com.google.gson.annotations.SerializedName

data class CloseChannel(
        @SerializedName("success")
        val success: Boolean = false,
        @SerializedName("message")
        val message: String = ""
)

data class CloseChannelMutation(
        @SerializedName("close_channel")
        val closeChannel: CloseChannel = CloseChannel()
)