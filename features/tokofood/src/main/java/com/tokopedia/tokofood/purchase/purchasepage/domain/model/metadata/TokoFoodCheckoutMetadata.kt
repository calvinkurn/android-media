package com.tokopedia.tokofood.purchase.purchasepage.domain.model.metadata

import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodAvailabilitySection
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodShipping
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodShop
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodShoppingSummary
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodUserAddress

data class TokoFoodCheckoutMetadata(
    @SerializedName("shop")
    @Expose
    val shop: CheckoutTokoFoodShop = CheckoutTokoFoodShop(),
    @SerializedName("user_address")
    @Expose
    val userAddress: CheckoutTokoFoodUserAddress = CheckoutTokoFoodUserAddress(),
    @SerializedName("available_section")
    @Expose
    val availableSection: CheckoutTokoFoodAvailabilitySection = CheckoutTokoFoodAvailabilitySection(),
    @SerializedName("unavailable_section")
    @Expose
    val unavailableSection: CheckoutTokoFoodAvailabilitySection = CheckoutTokoFoodAvailabilitySection(),
    @SerializedName("shipping")
    @Expose
    val shipping: CheckoutTokoFoodShipping = CheckoutTokoFoodShipping(),
    @SerializedName("shopping_summary")
    @Expose
    val shoppingSummary: CheckoutTokoFoodShoppingSummary = CheckoutTokoFoodShoppingSummary()
) {

    fun generateString(): String = Gson().toJson(this)

}