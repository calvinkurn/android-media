package com.tokopedia.shareexperience.domain.model.request.affiliate

import com.google.gson.annotations.SerializedName

data class ShareExAffiliateShopRequest(
    @SerializedName("ShopID")
    val shopID: String? = "",

    @SerializedName("ShopStatus")
    val shopStatus: Int? = 0,

    @SerializedName("IsOS")
    val isOS: Boolean = false,

    @SerializedName("IsPM")
    val isPM: Boolean = false
)
