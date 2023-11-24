package com.tokopedia.checkout.data.model.response.shipmentaddressform

import com.google.gson.annotations.SerializedName
import com.tokopedia.purchase_platform.common.feature.bmgm.data.response.BmGmCartDetailInfo

data class CartDetail(
    @SerializedName("errors")
    val errors: List<String> = emptyList(),
    @SerializedName("bundle_detail")
    val bundleDetail: BundleDetail = BundleDetail(),
    @SerializedName("cart_detail_info")
    val cartDetailInfo: BmGmCartDetailInfo = BmGmCartDetailInfo(),
    @SerializedName("products")
    val products: List<Product> = emptyList()
)
