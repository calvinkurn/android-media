package com.tokopedia.cartcommon.data.request.updatecart

import com.google.gson.annotations.SerializedName

data class UpdateCartRequest(
        @SerializedName("cart_id")
        var cartId: String = "0",
        @SerializedName("quantity")
        var quantity: Int = 0,
        @SerializedName("notes")
        var notes: String = "",
        @SerializedName("product_id")
        var productId: String = "0",
        @SerializedName("bundle_info")
        var bundleInfo: BundleInfo = BundleInfo()
)

data class BundleInfo(
        @SerializedName("bundle_id")
        var bundleId: String = "0",
        @SerializedName("bundle_group_id")
        var bundleGroupId: String = "",
        @SerializedName("bundle_qty")
        var bundleQty: Int = 0
)
