package com.tokopedia.smartbills.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RechargeMultipleSBMBill(
        @SerializedName("userID")
        @Expose
        val userID: String = "",
        @SerializedName("bills")
        @Expose
        val bills: List<RechargeBills> = emptyList()
) {
    data class Response(
            @SerializedName("rechargeMultipleSBMBill")
            @Expose
            val response: RechargeMultipleSBMBill? = null
    )
}