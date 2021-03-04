package com.tokopedia.purchase_platform.common.feature.purchaseprotection.data

import com.google.gson.annotations.SerializedName

data class PurchaseProtectionPlanDataResponse(
    @SerializedName("protection_available")
    var protectionAvailable: Boolean = false,
    @SerializedName("protection_type_id")
    var protectionTypeId: Int = 0,
    @SerializedName("protection_price_per_product")
    var protectionPricePerProduct: Int = 0,
    @SerializedName("protection_price")
    var protectionPrice: Int = 0,
    @SerializedName("protection_title")
    var protectionTitle: String = "",
    @SerializedName("protection_subtitle")
    var protectionSubtitle: String = "",
    @SerializedName("protection_link_text")
    var protectionLinkText: String = "",
    @SerializedName("protection_link_url")
    var protectionLinkUrl: String = "",
    @SerializedName("protection_opt_in")
    var protectionOptIn: Boolean = false,
    @SerializedName("protection_checkbox_disabled")
    var protectionCheckboxDisabled: Boolean = false,
    @SerializedName("unit")
    val unit: String = "",
    @SerializedName("source")
    val source: String = ""
)