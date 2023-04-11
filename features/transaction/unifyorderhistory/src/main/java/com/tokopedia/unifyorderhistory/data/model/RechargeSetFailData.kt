package com.tokopedia.unifyorderhistory.data.model

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class RechargeSetFailData(
    @SerializedName("rechargeSetOrderToFailV2")
    val rechargeSetOrderToFail: RechargeSetOrderToFail = RechargeSetOrderToFail()
) {
    data class RechargeSetOrderToFail(
        @SerializedName("attributes")
        val attributes: Attributes = Attributes(),
        @SerializedName("error")
        val error: String = ""
    ) {
        data class Attributes(
            @SuppressLint("Invalid Data Type")
            @SerializedName("user_id")
            val userId: Int = -1,
            @SerializedName("order_status")
            val orderStatus: Int = -1,
            @SerializedName("is_success")
            val isSuccess: Boolean = false,
            @SerializedName("error_message")
            val errorMessage: String = ""
        )
    }
}
