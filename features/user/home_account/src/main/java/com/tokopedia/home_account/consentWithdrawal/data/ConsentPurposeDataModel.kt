package com.tokopedia.home_account.consentWithdrawal.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.home_account.consentWithdrawal.ui.adapter.uimodel.ConsentWithdrawalUiModel

data class GetConsentPurposeDataModel(
    @SerializedName("GetPurposesByGroup")
    var consentGroup: ConsentPurposeGroupDataModel = ConsentPurposeGroupDataModel()
)

data class ConsentPurposeGroupDataModel(
    @SerializedName("isSuccess")
    var isSuccess: Boolean = false,
    @SerializedName("refId")
    var refId: String = "",
    @SerializedName("errorMessages")
    var errorMessages: List<String> = listOf(),
    @SerializedName("groupId")
    var groupId: String = "",
    @SerializedName("consents")
    var consents: ConsentPurposeDataModel = ConsentPurposeDataModel()
)

data class ConsentPurposeDataModel(
    @SerializedName("mandatory")
    var mandatory: List<ConsentPurposeItemDataModel> = listOf(),
    @SerializedName("optional")
    var optional: List<ConsentPurposeItemDataModel> = listOf(),
)

data class ConsentPurposeItemDataModel(
    @SerializedName("consentTitle")
    var consentTitle: String = "",
    @SerializedName("consentSubtitle")
    var consentSubtitle: String = "",
    @SerializedName("consentStatus")
    var consentStatus: String = "",
    @SerializedName("purposeId")
    var purposeId: String = "",
    @SerializedName("optInUrl")
    var optInUrl: String = "",
    @SerializedName("optOutUrl")
    var optOutUrl: String = "",
    @SerializedName("optIntAppLink")
    var optIntAppLink: String = "",
    @SerializedName("optOutAppLink")
    var optOutAppLink: String = "",
    @SerializedName("priority")
    var priority: Int = 0,
) : ConsentWithdrawalUiModel
