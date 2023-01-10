package com.tokopedia.usercomponents.userconsent.domain.submission

import com.google.gson.annotations.SerializedName

data class ConsentSubmissionResponse(
    @SerializedName("SubmitConsentRes")
    var submitConsentRes: SubmitConsentDataModel = SubmitConsentDataModel(),
)

data class SubmitConsentDataModel(
    @SerializedName("refId")
    var refId: String = "",
    @SerializedName("errorMessages")
    var errorMessages: List<String> = listOf(),
    @SerializedName("isSuccess")
    var isSuccess: Boolean = false,
    @SerializedName("receipts")
    var receipts: List<Receipt> = listOf(),
)

data class Receipt(
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
