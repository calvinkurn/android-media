package com.tokopedia.salam.umrah.checkout.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UmrahCheckoutSummaryEntity (
       @SerializedName("umrahCheckoutSummary")
       @Expose
       val umrahCheckoutSummary: UmrahCheckoutSummary = UmrahCheckoutSummary()
)
class UmrahCheckoutSummary(
        @SerializedName("checkoutDetails")
        @Expose
        val checkoutDetails : List<CheckoutDetail> = emptyList(),
        @SerializedName("total")
        @Expose
        val total : Int = 0
)
class CheckoutDetail(
        @SerializedName("header")
        @Expose
        val header: String = "",
        @SerializedName("subHeader")
        @Expose
        val subHeader : String = "",
        @SerializedName("amount")
        @Expose
        val amount : Int = 0
)