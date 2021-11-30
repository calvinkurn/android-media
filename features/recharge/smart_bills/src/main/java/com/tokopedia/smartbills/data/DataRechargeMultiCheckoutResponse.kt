package com.tokopedia.smartbills.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DataRechargeMultiCheckoutResponse(
        @SerializedName("data")
        @Expose
        val data: RechargeMultiCheckoutResponse = RechargeMultiCheckoutResponse()
)