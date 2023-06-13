package com.tokopedia.affiliate.model.response

import com.google.gson.annotations.SerializedName

data class AffiliateSSEToken(
    @SerializedName("getAffiliateToken")
    var data: Data?
) {
    data class Data(
        @SerializedName("Token")
        var token: String?
    )
}
