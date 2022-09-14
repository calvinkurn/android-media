package com.tokopedia.promocheckout.common.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CheckUmrahPromoCodeData(
        @SerializedName("success")
        @Expose
        var success: Boolean = false,
        @SerializedName("message")
        @Expose
        var message: Message = Message(),
        @SerializedName("codes")
        @Expose
        var codes: String = "",
        @SerializedName("promoCodeId")
        @Expose
        var promoCodeId : String = "0",
        @SerializedName("titleDescription")
        @Expose
        var titleDescription : String = "",
        @SerializedName("discountAmount")
        @Expose
        var discountAmount : Long = 0L,
        @SerializedName("cashbackWalletAmount")
        @Expose
        var cashbackWalletAmount : Long = 0L,
        @SerializedName("cashbackAdvocateReferralAmount")
        @Expose
        var cashbackAdvocateReferralAmount : Long = 0L,
        @SerializedName("invoiceDescription")
        @Expose
        var invoiceDescription : String = "",
        @SerializedName("isCoupon")
        @Expose
        var isCoupon : Boolean = false,
        @SerializedName("gatewayId")
        @Expose
        var gatewayId : String = "0",
        @SerializedName("tickerInfo")
        @Expose
        var tickerInfo: TickerInfo = TickerInfo()
)

data class TickerInfo(
        @SerializedName("uniqueId")
        @Expose
        var uniqueId : String = "0",
        @SerializedName("statusCode")
        @Expose
        var statusCode : Int = 0,
        @SerializedName("message")
        @Expose
        var message : String = ""
)
