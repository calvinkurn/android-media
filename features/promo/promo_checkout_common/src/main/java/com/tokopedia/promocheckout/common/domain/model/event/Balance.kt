package com.tokopedia.promocheckout.common.domain.model.event

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class Balance(
        @SerializedName("balance")
        @Expose
        val balance: Int = 0,
        @SerializedName("balance_text")
        @Expose
        val balanceText: String = "",
        @SerializedName("is_active")
        @Expose
        val isActive: Boolean = false,
        @SerializedName("limit")
        @Expose
        val limit: Int = 0,
        @SerializedName("limit_text")
        @Expose
        val limitText: String = "",
        @SerializedName("threshold")
        @Expose
        val threshold: Int = 0,
        @SerializedName("threshold_limit")
        @Expose
        val thresholdLimit: Int = 0,
        @SerializedName("threshold_limit_text")
        @Expose
        val thresholdLimitText: String = "",
        @SerializedName("threshold_text")
        @Expose
        val thresholdText: String = "",
        @SerializedName("useable_balance")
        @Expose
        val useableBalance: Int = 0,
        @SerializedName("useable_balance_text")
        @Expose
        val useableBalanceText: String = ""
)