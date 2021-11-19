package com.tokopedia.affiliate.model.request

import com.google.gson.annotations.SerializedName

data class OnBoardingRequest(
    @SerializedName("channel")
    val channels: List<Channel>
) {
    data class Channel(
            @SerializedName("channelid")
            val id: Int? = 0,
            @SerializedName("profileid")
            val profileid: String? = ""
    )
}
