package com.tokopedia.payment.setting.detail.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DataResponseDeleteCC(
        @SerializedName("removeCreditCard")
        @Expose
        var removeCreditCard: RemoveCreditCard? = null
)


data class RemoveCreditCard(
        @SerializedName("message")
        @Expose
        var message: String? = null,
        @SerializedName("success")
        @Expose
        var isSuccess: Boolean = false
)
