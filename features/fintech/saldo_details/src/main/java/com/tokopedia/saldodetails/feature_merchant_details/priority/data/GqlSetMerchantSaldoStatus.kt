package com.tokopedia.saldodetails.feature_merchant_details.priority.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GqlSetMerchantSaldoStatus(
        @SerializedName("sp_togglesaldoprioritas")
        @Expose
        var merchantSaldoStatus: MerchantSaldoStatus? = null
)

data class MerchantSaldoStatus(
        @SerializedName("success")
        @Expose
        var isSuccess: Boolean = false,
        var value: Boolean = false
)
