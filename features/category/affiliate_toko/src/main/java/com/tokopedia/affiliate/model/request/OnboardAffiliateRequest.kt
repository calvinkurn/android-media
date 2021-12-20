package com.tokopedia.affiliate.model.request

import com.google.gson.annotations.SerializedName

data class OnboardAffiliateRequest(
    @SerializedName("Channel")
    val channels: List<OnboardAffiliateChannelRequest>
) {
    data class OnboardAffiliateChannelRequest(
            @SerializedName("Name")
            val name: String? = "",
            @SerializedName("ProfileID")
            val profileid: String? = ""
    )
}
