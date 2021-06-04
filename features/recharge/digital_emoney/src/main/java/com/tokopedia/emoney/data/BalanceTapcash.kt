package com.tokopedia.emoney.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BalanceTapcash(
        @SerializedName("rechargeUpdateBalanceEmoneyBniTapcash")
        @Expose
        val rechargeUpdateBalance: RechargeUpdateBalanceEmoneyBniTapcash =
                RechargeUpdateBalanceEmoneyBniTapcash()
)

data class RechargeUpdateBalanceEmoneyBniTapcash(
        @SerializedName("criptogram")
        @Expose
        val criptogram: String = ""
)

