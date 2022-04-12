package com.tokopedia.affiliate.model.request

import com.google.gson.annotations.SerializedName

data class OnboardAffiliateRequest(
    @SerializedName("Channel")
    val channels: List<OnboardAffiliateChannelRequest>
) {
    data class OnboardAffiliateChannelRequest(
            @SerializedName("Name")
            val name: String? = "",
            @SerializedName("ChannelID")
            val channelID: Int? = 0,
            @SerializedName("ProfileID")
            val profileid: String? = ""
    )
}
