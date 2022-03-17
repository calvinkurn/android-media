package com.tokopedia.topchat.chatroom.domain.pojo.product_bundling

import com.google.gson.annotations.SerializedName

data class ProductBundlingData (
    @SerializedName("bundle_id")
    var bundleId: String = "",
    @SerializedName("bundle_type")
    var bundleType: Int = 0,
    @SerializedName("bundle_title")
    var bundleTitle: String = "",
    @SerializedName("bundle_status")
    var bundleStatus: Int = 0,
    @SerializedName("original_price")
    var originalPrice: String = "",
    @SerializedName("original_price_float")
    var originalPriceFloat: String = "",
    @SerializedName("bundle_price")
    var bundlePrice: String = "",
    @SerializedName("bundle_price_float")
    var bundlePriceFloat: String = "",
    @SerializedName("total_discount")
    var totalDiscount: String = "",
    @SerializedName("total_discount_float")
    var totalDiscountFloat: String = "",
    @SerializedName("button_text")
    var buttonText: String = "",
    @SerializedName("button_desktop_link")
    var buttonDesktopLink: String = "",
    @SerializedName("button_mobile_link")
    var buttonMobileLink: String = "",
    @SerializedName("button_android_link")
    var buttonAndroidLink: String = "",
    @SerializedName("button_ios_link")
    var buttonIOSLink: String = "",
    @SerializedName("bundle_item")
    var bundleItem: List<BundleItem> = listOf()
)