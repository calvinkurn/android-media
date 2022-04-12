package com.tokopedia.tokofood.common.domain.param

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.tokofood.common.domain.TokoFoodCartUtil

data class CheckoutTokoFoodParam(
    // TODO: Create correct object for parsed chosenAddress
    @SerializedName("additional_attributes")
    @Expose
    val additionalAttributes: String = "",
    @SuppressLint("Invalid Data Type")
    @SerializedName("business_id")
    @Expose
    val businessId: List<Long> = listOf(TokoFoodCartUtil.TOKOFOOD_BUSINESS_ID)
)