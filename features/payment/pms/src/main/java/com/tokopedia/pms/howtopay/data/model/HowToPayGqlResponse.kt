package com.tokopedia.pms.howtopay.data.model

import com.google.gson.annotations.SerializedName

data class HowToPayGqlResponse(
        @SerializedName("howToPayData")
        val howToPayData: HowToPayData
)

data class HowToPayData(
        @SerializedName("expiredIn")
        val expiredIn: String,
        @SerializedName("nettAmountUnf")
        val netAmount: Double,
        @SerializedName("combineAmount")
        val combineAmount: Double,
        @SerializedName("gatewayImage")
        val gatewayImage : String,
        @SerializedName("gatewayName")
        val gatewayName: String,
        @SerializedName("gatewayCode")
        val gatewayCode: String,
        @SerializedName("paymentCodeHint")
        val paymentCodeHint: String,
        @SerializedName("transactionCode")
        val transactionCode: String,
        @SerializedName("hideCopyAmount")
        val hideCopyAmount: Boolean,
        @SerializedName("hideCopyAccountNum")
        val hideCopyAccountNum: Boolean,
        @SerializedName("isOfflineStore")
        val isOfflineStore: Boolean,
        @SerializedName("isManualTransfer")
        val isManualTransfer: Boolean,
        @SerializedName("destBankName")
        val destBankName: String,
        @SerializedName("destBankBranch")
        val destBankBranch: String,
        @SerializedName("destAccountName")
        val destAccountName: String,
        @SerializedName("helpPageJSON")
        val helpPageJSON: String,
        var helpPageData: HelpPageData? = null
)

data class HelpPageData(
   @SerializedName("gateway_code")
   val gatewayCode: String,
   @SerializedName("gateway_type")
   val gatewayType: String,
   @SerializedName("gateway_name")
   val gatewayName: String,
   @SerializedName("channel")
   val channelList: ArrayList<HtpPaymentChannel>
)

data class HtpPaymentChannel(
        @SerializedName("payment_channel")
        val channelTitle: String,
        @SerializedName("channel_steps")
        val channelSteps: ArrayList<String>,
        var isExpanded: Boolean
)
