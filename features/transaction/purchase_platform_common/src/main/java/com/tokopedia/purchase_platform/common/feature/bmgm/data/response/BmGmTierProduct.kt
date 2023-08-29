package com.tokopedia.purchase_platform.common.feature.bmgm.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BmGmTierProduct(
    @Expose
    @SerializedName("tier_id")
    val tierId: Long = 0L,
    @Expose
    @SerializedName("tier_name")
    val tierName: String = "",
    @Expose
    @SerializedName("tier_message")
    val tierMessage: String = "",
    @Expose
    @SerializedName("tier_discount_text")
    val tierDiscountText: String = "",
    @Expose
    @SerializedName("tier_discount_amount")
    val tierDiscountAmount: String = "",
    @Expose
    @SerializedName("price_before_benefit")
    val priceBeforeBenefit: Double = 0.0,
    @Expose
    @SerializedName("price_after_benefit")
    val priceAfterBenefit: Double = 0.0,
    @Expose
    @SerializedName("list_product")
    val listProduct: List<BmGmProduct> = emptyList()
)
