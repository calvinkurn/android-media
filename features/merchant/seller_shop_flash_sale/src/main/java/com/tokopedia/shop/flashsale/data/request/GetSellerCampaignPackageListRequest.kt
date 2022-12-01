package com.tokopedia.shop.flashsale.data.request

import com.google.gson.annotations.SerializedName

data class GetSellerCampaignPackageListRequest(
    @SerializedName("request_header")
    val requestHeader: RequestHeader,
    @SerializedName("thematic_id")
    val thematicId: String,
    @SerializedName("thematic_sub_id")
    val thematicSubId: String
)