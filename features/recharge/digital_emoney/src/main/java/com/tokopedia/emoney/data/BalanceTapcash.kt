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
        @SerializedName("attributes")
        @Expose
        val attributes: AttributesTapcash = AttributesTapcash(),
        @SerializedName("error")
        @Expose
        val error: TapcashError = TapcashError(),

)

class AttributesTapcash(
        @SerializedName("button_text")
        @Expose
        val buttonText: String = "",
        @SerializedName("card_number")
        @Expose
        val cardNumber: String = "",
        @SerializedName("image_issuer")
        @Expose
        val imageIssuer: String = "",
        @SerializedName("amount")
        @Expose
        val amount: Int = 0,
        @SerializedName("rrn")
        @Expose
        val rrn: Int = 0,
        @SerializedName("cryptogram")
        @Expose
        val cryptogram: String = ""
)

class TapcashError(
        @SerializedName("id")
        @Expose
        val id: Int = 0,
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("status")
        @Expose
        val status: Int = 0
)



