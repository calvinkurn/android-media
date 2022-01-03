package com.tokopedia.affiliate.model.response

import com.google.gson.annotations.SerializedName

data class AffiliateTransactionHistoryData(
        @SerializedName("getAffiliateTransactionHistory")
        var getAffiliateTransactionHistory: GetAffiliateTransactionHistory?
) {
    data class GetAffiliateTransactionHistory(
            @SerializedName("Data")
            var `data`: Data?
    ) {
        data class Data(
                @SerializedName("EndDate")
                var endDate: String,
                @SerializedName("Error")
                var error: Error,
                @SerializedName("HasNext")
                var hasNext: Boolean,
                @SerializedName("Limit")
                var limit: Int,
                @SerializedName("Page")
                var page: Int,
                @SerializedName("StartDate")
                var startDate: String,
                @SerializedName("Status")
                var status: Int,
                @SerializedName("Transaction")
                var transaction: List<Transaction>? = null
        ) {
            data class Error(
                    @SerializedName("CtaLink")
                    var ctaLink: CtaLink,
                    @SerializedName("CtaText")
                    var ctaText: String,
                    @SerializedName("ErrorType")
                    var errorType: Int,
                    @SerializedName("Message")
                    var message: String
            ) {
                data class CtaLink(
                        @SerializedName("AndroidURL")
                        var androidURL: String,
                        @SerializedName("DesktopURL")
                        var desktopURL: String,
                        @SerializedName("IosURL")
                        var iosURL: String,
                        @SerializedName("MobileURL")
                        var mobileURL: String
                )
            }

            data class Transaction(
                    @SerializedName("Amount")
                    var amount: String,
                    @SerializedName("AmountFormatted")
                    var amountFormatted: String,
                    @SerializedName("CreatedAt")
                    var createdAt: String,
                    @SerializedName("CreatedAtFormatted")
                    var createdAtFormatted: String,
                    @SerializedName("Label")
                    var label: Label,
                    @SerializedName("Notes")
                    var notes: String,
                    @SerializedName("Title")
                    var title: String,
                    @SerializedName("TransactionID")
                    var transactionID: String,
                    @SerializedName("TransactionStatus")
                    var transactionStatus: String,
                    @SerializedName("TransactionType")
                    var transactionType: String,
                    @SerializedName("UpdatedAt")
                    var updatedAt: String,
                    @SerializedName("UpdatedAtFormatted")
                    var updatedAtFormatted: String,
                    @SerializedName("WithdrawalID")
                    var withdrawalID: String,
                    @SerializedName("HasDetail")
                    var hasDetail: Boolean? = false
            ) {
                data class Label(
                        @SerializedName("LabelText")
                        var labelText: String,
                        @SerializedName("LabelType")
                        var labelType: String
                )
            }
        }
    }
}