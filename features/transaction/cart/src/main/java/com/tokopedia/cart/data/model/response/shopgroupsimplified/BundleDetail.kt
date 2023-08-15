package com.tokopedia.cart.data.model.response.shopgroupsimplified

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BundleDetail(
    @Expose
    @SerializedName("bundle_group_id")
    val bundleGroupId: String = "",
    @Expose
    @SerializedName("bundle_id")
    val bundleId: String = "",
    @Expose
    @SerializedName("bundle_max_order")
    val bundleMaxOrder: Int = 0,
    @Expose
    @SerializedName("bundle_min_order")
    val bundleMinOrder: Int = 0,
    @Expose
    @SerializedName("bundle_name")
    val bundleName: String = "",
    @Expose
    @SerializedName("bundle_original_price")
    val bundleOriginalPrice: Double = 0.0,
    @Expose
    @SerializedName("bundle_price")
    val bundlePrice: Double = 0.0,
    @Expose
    @SerializedName("bundle_price_fmt")
    val bundlePriceFmt: String = "",
    @Expose
    @SerializedName("bundle_qty")
    val bundleQty: Int = 0,
    @Expose
    @SerializedName("bundle_quota")
    val bundleQuota: Int = 0,
    @Expose
    @SerializedName("bundle_type")
    val bundleType: String = "",
    @Expose
    @SerializedName("edit_app_link")
    val editBundleApplink: String = "",
    @Expose
    @SerializedName("slash_price_label")
    val slashPriceLabel: String = "",
    @Expose
    @SerializedName("bundle_icon_url")
    val bundleIconUrl: String = "",
    @Expose
    @SerializedName("bundle_grayscale_icon_url")
    val bundleGrayscaleIconUrl: String = ""
)
