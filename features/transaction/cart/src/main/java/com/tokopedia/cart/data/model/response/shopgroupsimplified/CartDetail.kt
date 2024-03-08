package com.tokopedia.cart.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName

data class CartDetail(
    @SerializedName("bundle_detail")
    val bundleDetail: BundleDetail = BundleDetail(),
    @SerializedName("products")
    val products: List<Product> = emptyList(),
    @SerializedName("cart_detail_info")
    val cartDetailInfo: BmGmCartDetailInfo = BmGmCartDetailInfo()
)
