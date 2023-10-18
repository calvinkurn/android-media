package com.tokopedia.tokofood.feature.purchase.purchasepage.domain.model.param

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.tokofood.common.domain.response.CartListBusinessDataCartGroup
import com.tokopedia.tokofood.common.domain.response.CartListBusinessDataShipping
import com.tokopedia.tokofood.common.domain.response.CartListBusinessDataShop
import com.tokopedia.tokofood.common.domain.response.CartListBusinessDataShoppingSummary
import com.tokopedia.tokofood.common.domain.response.CartListBusinessDataUserAddress

data class CheckoutGeneralTokoFoodParam(
    @SerializedName("transaction")
    val transaction: CheckoutGeneralTokofoodParamTransaction = CheckoutGeneralTokofoodParamTransaction()
)

data class CheckoutGeneralTokofoodParamTransaction(
    @SerializedName("flow_type")
    val flowType: String = String.EMPTY,
    @SerializedName("business_data")
    val businessData: List<CheckoutGeneralTokofoodParamBusinessData> = listOf()
)

data class CheckoutGeneralTokofoodParamBusinessData(
    @SerializedName("checkout_business_type")
    val checkoutBusinessType: Int = Int.ZERO,
    @SerializedName("checkout_data_type")
    val checkoutDataType: String = String.EMPTY,
    @SerializedName("business_id")
    val businessId: String = String.EMPTY,
    @SerializedName("business_checkout_state")
    val businessCheckoutState: CheckoutGeneralTokofoodParamBusinessCheckoutState = CheckoutGeneralTokofoodParamBusinessCheckoutState(),
    @SerializedName("promo_codes")
    val promoCodes: List<String> = listOf(),
    @SerializedName("cart_groups")
    val cartGroups: List<CartListBusinessDataCartGroup> = listOf()
)

data class CheckoutGeneralTokofoodParamBusinessCheckoutState(
    @SerializedName("user_address")
    val userAddress: CartListBusinessDataUserAddress = CartListBusinessDataUserAddress(),
    @SerializedName("shop")
    val shop: CartListBusinessDataShop = CartListBusinessDataShop(),
    @SerializedName("shipping")
    val shipping: CartListBusinessDataShipping = CartListBusinessDataShipping(),
    @SerializedName("shopping_summary")
    val shoppingSummary: CartListBusinessDataShoppingSummary = CartListBusinessDataShoppingSummary(),
)
