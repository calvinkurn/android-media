package com.tokopedia.cart.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName
import java.util.*

data class CartDetail(
    @SerializedName("origin_warehouse_ids")
    val originWarehouseIds: List<Int> = emptyList(),
    @SerializedName("bundle_detail")
    val bundleDetail: BundleDetail = BundleDetail(),
    @SerializedName("products")
    val products: List<Product> = emptyList()
)
