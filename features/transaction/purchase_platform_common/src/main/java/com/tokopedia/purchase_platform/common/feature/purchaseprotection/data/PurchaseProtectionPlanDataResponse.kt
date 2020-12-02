package com.tokopedia.purchase_platform.common.feature.purchaseprotection.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PurchaseProtectionPlanDataResponse(
    @SerializedName("protection_available")
    val protectionAvailable: Boolean = false,

    @SerializedName("protection_type_id")
    val protectionTypeId: Int = 0,

    @SerializedName("protection_price_per_product")
    val protectionPricePerProduct: Int = 0,

    @SerializedName("protection_price")
    val protectionPrice: Int = 0,

    @SerializedName("protection_title")
    val protectionTitle: String = "",

    @SerializedName("protection_subtitle")
    val protectionSubtitle: String = "",

    @SerializedName("protection_link_text")
    val protectionLinkText: String = "",

    @SerializedName("protection_link_url")
    val protectionLinkUrl: String = "",

    @SerializedName("protection_opt_in")
    val protectionOptIn: Boolean = false,

    @SerializedName("protection_checkbox_disabled")
    val protectionCheckboxDisabled: Boolean = false,

    @SerializedName("unit")
    val unit: String = ""
)