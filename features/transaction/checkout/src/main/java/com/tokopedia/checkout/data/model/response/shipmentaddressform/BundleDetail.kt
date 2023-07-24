package com.tokopedia.checkout.data.model.response.shipmentaddressform

import com.google.gson.annotations.SerializedName

data class BundleDetail(
    @SerializedName("bundle_group_id")
    val bundleGroupId: String = "",
    @SerializedName("bundle_id")
    val bundleId: String = "",
    @SerializedName("bundle_name")
    val bundleName: String = "",
    @SerializedName("bundle_original_price")
    val bundleOriginalPrice: Double = 0.0,
    @SerializedName("bundle_price")
    val bundlePrice: Double = 0.0,
    @SerializedName("bundle_qty")
    val bundleQty: Int = 0,
    @SerializedName("bundle_type")
    val bundleType: String = "",
    @SerializedName("slash_price_label")
    val slashPriceLabel: String = "",
    @SerializedName("bundle_icon_url")
    val bundleIconUrl: String = ""
)
