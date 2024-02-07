package com.tokopedia.cartcommon.data.response.bmgm

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class BmGmTierProduct(
    @SerializedName("tier_id")
    val tierId: Long = 0L,
    @SerializedName("tier_name")
    val tierName: String = "",
    @SerializedName("tier_message")
    val tierMessage: String = "",
    @SerializedName("tier_discount_text")
    val tierDiscountText: String = "",
    @SerializedName("tier_discount_amount")
    val tierDiscountAmount: String = "",
    @SerializedName("price_before_benefit")
    val priceBeforeBenefit: Double = 0.0,
    @SerializedName("price_after_benefit")
    val priceAfterBenefit: Double = 0.0,
    @SerializedName("list_product")
    val listProduct: List<BmGmProduct> = emptyList(),
    @SerializedName("benefit_wording")
    val benefitWording: String = "",
    @SerializedName("action_wording")
    val actionWording: String = "",
    @SerializedName("benefit_quantity")
    val benefitQuantity: Int = 0,
    @SerializedName("products_benefit")
    val productsBenefit: List<BmGmProductBenefit> = emptyList()
) : Parcelable
