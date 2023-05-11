package com.tokopedia.play.broadcaster.shorts.domain.model

import com.google.gson.annotations.SerializedName

data class GenerateChannelAffiliateLinkResponseModel(
    @SerializedName("broadcasterGenerateChannelAffiliateLink")
    val data: Data = Data()
) {
    data class Data(
        @SerializedName("success")
        val success: Boolean = false
    )
}
