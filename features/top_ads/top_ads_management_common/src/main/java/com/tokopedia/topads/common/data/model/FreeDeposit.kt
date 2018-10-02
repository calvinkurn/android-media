package com.tokopedia.topads.common.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FreeDeposit(
        @SerializedName("deposit_id")
        @Expose
        val depositId: Int = 0,
        @SerializedName("nominal")
        @Expose
        val nominal: Double = 0.00,
        @SerializedName("nominal_fmt")
        @Expose
        val nominalFmt: String = "",
        @SerializedName("remaining_days")
        @Expose
        val remainingDays: Int = 0,
        @SerializedName("status")
        @Expose
        val status: Int = 0,
        @SerializedName("usage")
        @Expose
        val usage: Double = 0.00,
        @SerializedName("usage_fmt")
        @Expose
        val usageFmt: String = ""
)