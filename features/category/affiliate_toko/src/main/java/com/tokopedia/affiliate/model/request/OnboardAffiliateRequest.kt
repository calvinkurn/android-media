package com.tokopedia.affiliate.model.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class OnboardAffiliateRequest(
    @SerializedName("Channel")
    val channels: List<OnboardAffiliateChannelRequest>
) : Parcelable {
    @Parcelize
    data class OnboardAffiliateChannelRequest(
        @SerializedName("Name")
        val name: String? = "",
        @SerializedName("ChannelID")
        val channelID: Int? = 0,
        @SerializedName("ProfileID")
        val profileid: String? = ""
    ) : Parcelable
}
