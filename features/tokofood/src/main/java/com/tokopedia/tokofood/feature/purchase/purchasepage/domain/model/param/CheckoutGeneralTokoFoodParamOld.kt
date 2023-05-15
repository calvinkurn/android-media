package com.tokopedia.tokofood.feature.purchase.purchasepage.domain.model.param

import com.google.gson.annotations.SerializedName
import com.tokopedia.tokofood.common.domain.TokoFoodCartUtil

data class CheckoutGeneralTokoFoodParamOld(
    @SerializedName("carts")
    val carts: CheckoutGeneralTokoFoodCartParam = CheckoutGeneralTokoFoodCartParam()
)

data class CheckoutGeneralTokoFoodCartParam(
    @SerializedName("business_type")
    val businessType: Int = TokoFoodCartUtil.TOKOFOOD_BUSINESS_TYPE,
    @SerializedName("cart_info")
    val cartInfo: List<CheckoutGeneralTokoFoodCartInfoParam> = listOf()
)

data class CheckoutGeneralTokoFoodCartInfoParam(
    @SerializedName("metadata")
    val metadata: String = "",
    @SerializedName("data_type")
    val dataType: String = TokoFoodCartUtil.TOKOFOOD_DATA_TYPE
)
