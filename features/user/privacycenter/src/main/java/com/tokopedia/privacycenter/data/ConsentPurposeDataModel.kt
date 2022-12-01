package com.tokopedia.privacycenter.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.privacycenter.ui.consentwithdrawal.adapter.uimodel.ConsentWithdrawalUiModel

data class GetConsentPurposeDataModel(
    @SerializedName("GetPurposesByGroup")
    val consentGroup: ConsentPurposeGroupDataModel = ConsentPurposeGroupDataModel()
)

data class ConsentPurposeGroupDataModel(
    @SerializedName("isSuccess")
    val isSuccess: Boolean = false,
    @SerializedName("refId")
    val refId: String = "",
    @SerializedName("errorMessages")
    val errorMessages: List<String> = listOf(),
    @SerializedName("groupId")
    val groupId: String = "",
    @SerializedName("consents")
    val consents: ConsentPurposeDataModel = ConsentPurposeDataModel()
)

data class ConsentPurposeDataModel(
    @SerializedName("mandatory")
    val mandatory: List<ConsentPurposeItemDataModel> = listOf(),
    @SerializedName("optional")
    val optional: List<ConsentPurposeItemDataModel> = listOf()
)

data class ConsentPurposeItemDataModel(
    @SerializedName("consentTitle")
    val consentTitle: String = "",
    @SerializedName("consentSubtitle")
    val consentSubtitle: String = "",
    @SerializedName("consentStatus")
    var consentStatus: String = "",
    @SerializedName("purposeId")
    val purposeId: String = "",
    @SerializedName("optInUrl")
    val optInUrl: String = "",
    @SerializedName("optOutUrl")
    val optOutUrl: String = "",
    @SerializedName("optIntAppLink")
    val optIntAppLink: String = "",
    @SerializedName("optOutAppLink")
    val optOutAppLink: String = "",
    @SerializedName("priority")
    val priority: Int = 0
) : ConsentWithdrawalUiModel
