package com.tokopedia.smartbills.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RechargeMultipleSBMBill(
        @SerializedName("UserID")
        @Expose
        val userID: String = "",
        @SerializedName("Bills")
        @Expose
        val bills: List<RechargeBills> = emptyList()
) {
    data class Response(
            @SerializedName("RechargeMultipleSBMBill")
            @Expose
            val response: RechargeMultipleSBMBill? = null
    )
}