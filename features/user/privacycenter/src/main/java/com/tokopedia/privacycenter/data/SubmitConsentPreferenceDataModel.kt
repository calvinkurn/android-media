package com.tokopedia.privacycenter.data

import com.google.gson.annotations.SerializedName

data class SubmitConsentPreferenceDataModel(
    @SerializedName("SubmitConsentPreference")
    val data: SubmitConsentDataModel = SubmitConsentDataModel()
)

data class SubmitConsentDataModel(
    @SerializedName("refId")
    val refId: String = "",
    @SerializedName("isSuccess")
    val isSuccess: Boolean = false,
    @SerializedName("errorMessages")
    val errorMessages: List<String> = listOf(),
    @SerializedName("receipts")
    val receipts: List<ReceiptDataModel> = listOf()
)

data class ReceiptDataModel(
    @SerializedName("receiptId")
    val receiptId: String = "",
    @SerializedName("identifier")
    val identifier: String = "",
    @SerializedName("identifierType")
    val identifierType: String = "",
    @SerializedName("purposeId")
    val purposeId: String = "",
    @SerializedName("transactionType")
    val transactionType: String = "",
    @SerializedName("version")
    val version: String = ""
)
