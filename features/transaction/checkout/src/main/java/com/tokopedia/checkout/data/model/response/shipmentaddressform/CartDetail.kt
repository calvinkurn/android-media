package com.tokopedia.checkout.data.model.response.shipmentaddressform

import com.google.gson.annotations.SerializedName

data class CartDetail(
        @SerializedName("bundle_detail")
        val bundleDetail: BundleDetail = BundleDetail(),
        @SerializedName("products")
        val products: List<Product> = emptyList()
)