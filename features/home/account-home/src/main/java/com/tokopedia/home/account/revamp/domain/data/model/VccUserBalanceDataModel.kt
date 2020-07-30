package com.tokopedia.home.account.revamp.domain.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VccUserBalanceDataModel (
    @SerializedName("vcc_id")
    @Expose
    var vccId: Int = 0,

    @SerializedName("vcc_number")
    @Expose
    var vccNumber: Int = 0,

    @SerializedName("available_balance")
    @Expose
    var availableBalanc: Int = 0,

    @SerializedName("credit_limit")
    @Expose
    var creditLimit: Int = 0,

    @SerializedName("outstanding_balance")
    @Expose
    var outstandingBalance: Int = 0,

    @SerializedName("available_balance_text")
    @Expose
    var availableBalanceText: String = "",

    @SerializedName("credit_limit_text")
    @Expose
    var creditLimitText: String = "",

    @SerializedName("currency")
    @Expose
    var currency: String = "",

    @SerializedName("lender_name")
    @Expose
    var lenderName: String = "",

    @SerializedName("vcc_expire_at")
    @Expose
    var vccExpireAt: String = ""
)