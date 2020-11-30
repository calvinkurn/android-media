package com.tokopedia.purchase_platform.common.feature.purchaseprotection.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PurchaseProtectionPlanDataResponse(
    @SerializedName("protection_available")
    @Expose
    val protectionAvailable: Boolean = false,

    @SerializedName("protection_type_id")
    @Expose
    val protectionTypeId: Int = 0,

    @SerializedName("protection_price_per_product")
    @Expose
    val protectionPricePerProduct: Int = 0,

    @SerializedName("protection_price")
    @Expose
    val protectionPrice: Int = 0,

    @SerializedName("protection_title")
    @Expose
    val protectionTitle: String = "",

    @SerializedName("protection_subtitle")
    @Expose
    val protectionSubtitle: String = "",

    @SerializedName("protection_link_text")
    @Expose
    val protectionLinkText: String = "",

    @SerializedName("protection_link_url")
    @Expose
    val protectionLinkUrl: String = "",

    @SerializedName("protection_opt_in")
    @Expose
    val protectionOptIn: Boolean = false,

    @SerializedName("protection_checkbox_disabled")
    @Expose
    val protectionCheckboxDisabled: Boolean = false
)