package com.tokopedia.cartrevamp.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName
import java.util.*

data class CartDetail(
    @SerializedName("bundle_detail")
    val bundleDetail: BundleDetail = BundleDetail(),
    @SerializedName("products")
    val products: List<Product> = emptyList()
)
