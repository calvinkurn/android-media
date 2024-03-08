package com.tokopedia.shareexperience.domain.model.request.affiliate

import com.google.gson.annotations.SerializedName

data class ShareExAffiliateProductRequest(
    @SerializedName("ProductID")
    val productID: String? = "",

    @SerializedName("CatLevel1")
    val catLevel1: String? = "",

    @SerializedName("CatLevel2")
    val catLevel2: String? = "",

    @SerializedName("CatLevel3")
    val catLevel3: String? = "",

    @SerializedName("ProductPrice")
    val productPrice: String? = "",

    @SerializedName("MaxProductPrice")
    val maxProductPrice: String? = "",

    @SerializedName("ProductStatus")
    val productStatus: String? = "",

    @SerializedName("FormattedProductPrice")
    val formattedProductPrice: String? = ""
)
