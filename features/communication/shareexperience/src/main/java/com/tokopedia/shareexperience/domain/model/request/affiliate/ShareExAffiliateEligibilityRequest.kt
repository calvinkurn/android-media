package com.tokopedia.shareexperience.domain.model.request.affiliate

import com.google.gson.annotations.SerializedName

data class ShareExAffiliateEligibilityRequest(
    @SerializedName("PageType")
    val pageType: String? = "",

    @SerializedName("Product")
    val product: ShareExAffiliateProductRequest? = null,

    @SerializedName("Shop")
    val shop: ShareExAffiliateShopRequest? = null,

    @SerializedName("PageDetail")
    val pageDetail: ShareExAffiliatePageDetailRequest? = null
)
