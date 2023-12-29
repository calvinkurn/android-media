package com.tokopedia.play.broadcaster.shorts.domain.model

import com.google.gson.annotations.SerializedName

data class BroadcasterCheckAffiliateResponseModel(
    @SerializedName("broadcasterCheckIsAffiliate")
    val data: BroadcasterCheckIsAffiliate = BroadcasterCheckIsAffiliate(),
) {
    data class BroadcasterCheckIsAffiliate(
        @SerializedName("affiliateName")
        val affiliateName: String = "",
        @SerializedName("isAffiliate")
        val isAffiliate: Boolean = false,
    )
}
