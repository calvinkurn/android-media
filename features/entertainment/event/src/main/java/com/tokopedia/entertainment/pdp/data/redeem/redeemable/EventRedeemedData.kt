package com.tokopedia.entertainment.pdp.data.redeem.redeemable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class EventRedeemedData(
        @SerializedName("data")
        @Expose
        val data: DataRedeemed = DataRedeemed(),
        @SerializedName("status")
        @Expose
        val status: String = ""
)

data class DataRedeemed(
        @SerializedName("invoice")
        @Expose
        val invoice: Invoice = Invoice()
)

data class Invoice(
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("provider_invoice_status")
        @Expose
        val providerInvoiceStatus : String = ""
)