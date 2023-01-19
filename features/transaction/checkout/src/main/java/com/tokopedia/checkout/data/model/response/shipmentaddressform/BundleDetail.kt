package com.tokopedia.checkout.data.model.response.shipmentaddressform

import com.google.gson.annotations.SerializedName

data class BundleDetail(
        @SerializedName("bundle_description")
        val bundleDescription: String = "",
        @SerializedName("bundle_end_time_unix")
        val bundleEndTimeUnix: String = "",
        @SerializedName("bundle_group_id")
        val bundleGroupId: String = "",
        @SerializedName("bundle_id")
        val bundleId: String = "",
        @SerializedName("bundle_max_order")
        val bundleMaxOrder: Int = 0,
        @SerializedName("bundle_min_order")
        val bundleMinOrder: Int = 0,
        @SerializedName("bundle_name")
        val bundleName: String = "",
        @SerializedName("bundle_original_price")
        val bundleOriginalPrice: Double = 0.0,
        @SerializedName("bundle_original_price_fmt")
        val bundleOriginalPriceFmt: String = "",
        @SerializedName("bundle_price")
        val bundlePrice: Double = 0.0,
        @SerializedName("bundle_price_fmt")
        val bundlePriceFmt: String = "",
        @SerializedName("bundle_qty")
        val bundleQty: Int = 0,
        @SerializedName("bundle_quota")
        val bundleQuota: Int = 0,
        @SerializedName("bundle_start_time_unix")
        val bundleStartTimeUnix: String = "",
        @SerializedName("bundle_status")
        val bundleStatus: String = "",
        @SerializedName("bundle_type")
        val bundleType: String = "",
        @SerializedName("edit_bundle_applink")
        val editBundleApplink: String = "",
        @SerializedName("slash_price_label")
        val slashPriceLabel: String = "",
        @SerializedName("bundle_icon_url")
        val bundleIconUrl: String = ""
)
