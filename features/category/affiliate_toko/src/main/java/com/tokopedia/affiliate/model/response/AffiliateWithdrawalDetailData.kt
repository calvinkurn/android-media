package com.tokopedia.affiliate.model.response

import com.google.gson.annotations.SerializedName

data class AffiliateWithdrawalDetailData(
    @SerializedName("getAffiliateWithdrawalDetail")
    val data: WithdrawalInfoResult?
)

data class WithdrawalInfoResult(
        @SerializedName("Data")
    val withdrawalData: WithdrawalInfoData?,
)

data class WithdrawalInfoData(
        @SerializedName("Status")
        val isSuccess: Int,
        @SerializedName("Error")
        val error: Error?,
        @SerializedName("withdrawal_id")
    val withdrawalId: String?,
        @SerializedName("Label")
    val statusLabel : Label?,
        @SerializedName("Ticker")
    val ticker : Ticker?,
        @SerializedName("WithdrawalAmount")
    val amount: Double?,
        @SerializedName("WithdrawalAmountFormatted")
    val amountFormatted: String?,
        @SerializedName("WithdrawalFee")
    val fee: Double?,
        @SerializedName("WithdrawalFeeFormatted")
    val feeFormatted: String?,
        @SerializedName("FinalTransferred")
    val transferredAmount: Double?,
        @SerializedName("FinalTransferredFormatted")
    val transferredAmountFormatted: String?,
        @SerializedName("CreatedAtFormatted")
    val createdTime: String?,
        @SerializedName("BankName")
    val bankName: String?,
        @SerializedName("AccountName")
    val accountName: String?,
        @SerializedName("Detail")
    val withdrawalInfoHistory: ArrayList<WithdrawalInfoHistory>,
        var feeDetailData: ArrayList<FeeDetailData>
){

    data class Ticker(
            @SerializedName("TickerTitle")
            val tickerTitle: String?,
            @SerializedName("TickerDescription")
            val tickerDescription: String?
    )

    data class Label(
            @SerializedName("LabelType")
            val labelType: String?,
            @SerializedName("LabelText")
            val labelText: String?
    )

    data class Error(
            @SerializedName("CtaLink")
            var ctaLink: CtaLink?,
            @SerializedName("CtaText")
            var ctaText: String?,
            @SerializedName("ErrorType")
            var errorType: Int?,
            @SerializedName("Message")
            var message: String?
    ) {
        data class CtaLink(
                @SerializedName("AndroidURL")
                var androidURL: String?,
                @SerializedName("DesktopURL")
                var desktopURL: String?,
                @SerializedName("IosURL")
                var iosURL: String?,
                @SerializedName("MobileURL")
                var mobileURL: String?
        )
    }
}

data class FeeDetailData(
    @SerializedName("type_description")
    val feeType: String,
    @SerializedName("amount")
    val amountFormatted : String?
)

data class WithdrawalInfoHistory(
    @SerializedName("DetailTitle")
    val historyTitle: String,
    @SerializedName("DetailDescription")
    val description: String,
    @SerializedName("CreatedAtFormatted")
    val createdTime: String
)
