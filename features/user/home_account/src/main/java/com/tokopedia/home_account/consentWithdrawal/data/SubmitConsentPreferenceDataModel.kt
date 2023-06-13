package com.tokopedia.home_account.consentWithdrawal.data

import com.google.gson.annotations.SerializedName

data class SubmitConsentPreferenceDataModel(
    @SerializedName("SubmitConsentPreference")
    var data: SubmitConsentDataModel = SubmitConsentDataModel()
)

data class SubmitConsentDataModel(
    var position: Int = 0,
    @SerializedName("refId")
    var refId: String = "",
    @SerializedName("isSuccess")
    var isSuccess: Boolean = false,
    @SerializedName("errorMessages")
    var errorMessages: List<String> = listOf(),
    @SerializedName("receipts")
    var receipts: List<ReceiptDataModel> = listOf(),
)

data class ReceiptDataModel(
    @SerializedName("receiptId")
    var receiptId: String = "",
    @SerializedName("identifier")
    var identifier: String = "",
    @SerializedName("identifierType")
    var identifierType: String = "",
    @SerializedName("purposeId")
    var purposeId: String = "",
    @SerializedName("transactionType")
    var transactionType: String = "",
    @SerializedName("version")
    var version: String = "",
)
