package com.tokopedia.tokofood.purchase.purchasepage.domain.model.param

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.tokofood.common.domain.TokoFoodCartUtil

data class CheckoutGeneralTokoFoodParam(
    @SerializedName("carts")
    @Expose
    val carts: CheckoutGeneralTokoFoodCartParam = CheckoutGeneralTokoFoodCartParam()
)

data class CheckoutGeneralTokoFoodCartParam(
    @SerializedName("business_type")
    @Expose
    val businessType: Int = TokoFoodCartUtil.TOKOFOOD_BUSINESS_TYPE,
    @SerializedName("cart_info")
    @Expose
    val cartInfo: List<CheckoutGeneralTokoFoodCartInfoParam> = listOf()
)

data class CheckoutGeneralTokoFoodCartInfoParam(
    @SerializedName("metadata")
    @Expose
    val metadata: String = "",
    @SerializedName("data_type")
    @Expose
    val dataType: String = TokoFoodCartUtil.TOKOFOOD_DATA_TYPE
)