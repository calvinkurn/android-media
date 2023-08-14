package com.tokopedia.cartrevamp.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName
import com.tokopedia.purchase_platform.common.feature.bmgm.data.response.BmGmCartDetailInfo
import java.util.*

data class CartDetail(
    @SerializedName("bundle_detail")
    val bundleDetail: BundleDetail = BundleDetail(),
    @SerializedName("products")
    val products: List<Product> = emptyList(),
    @SerializedName("cart_detail_info")
    val cartDetailInfo: BmGmCartDetailInfo = BmGmCartDetailInfo()
)
