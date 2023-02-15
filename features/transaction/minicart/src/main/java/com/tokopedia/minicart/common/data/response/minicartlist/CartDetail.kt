package com.tokopedia.minicart.common.data.response.minicartlist

import com.google.gson.annotations.SerializedName

data class CartDetail(
    @SerializedName("errors")
    val errors: List<String> = emptyList(),
    @SerializedName("products")
    val products: List<Product> = emptyList(),
    @SerializedName("bundle_detail")
    val bundleDetail: BundleDetail = BundleDetail()
)
