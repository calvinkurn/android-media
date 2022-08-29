package com.tokopedia.topchat.chatroom.domain.pojo.product_bundling

import com.google.gson.annotations.SerializedName

data class ProductBundlingData (
    @SerializedName("bundle_id")
    var bundleId: String? = "",
    @SerializedName("bundle_type")
    var bundleType: Int? = 0,
    @SerializedName("bundle_title")
    var bundleTitle: String? = "",
    @SerializedName("bundle_status")
    var bundleStatus: Int? = 0,
    @SerializedName("original_price")
    var originalPrice: String? = "",
    @SerializedName("bundle_price")
    var bundlePrice: String? = "",
    @SerializedName("total_discount")
    var totalDiscount: String? = "",
    @SerializedName("cta_bundling")
    var ctaBundling: CtaBundling? = CtaBundling(),
    @SerializedName("bundle_item")
    var bundleItem: List<BundleItem>? = listOf()
)