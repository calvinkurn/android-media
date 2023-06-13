package com.tokopedia.home_account.consentWithdrawal.data

import com.google.gson.annotations.SerializedName

data class GetConsentGroupListDataModel(
    @SerializedName("GetConsentGroupList")
    var consentGroupList: ConsentGroupListDataModel = ConsentGroupListDataModel()
)

data class ConsentGroupListDataModel(
    @SerializedName("success")
    var success: Boolean = false,
    @SerializedName("refId")
    var refId: String = "",
    @SerializedName("errorMessages")
    var errorMessages: List<String> = listOf(),
    @SerializedName("groups")
    var groups: List<ConsentGroupDataModel> = listOf()
)

data class ConsentGroupDataModel(
    @SerializedName("id")
    var id: String = "0",
    @SerializedName("groupTitle")
    var groupTitle: String = "",
    @SerializedName("groupSubtitle")
    var groupSubtitle: String = "",
    @SerializedName("groupImg")
    var groupImage: String = "",
    @SerializedName("priority")
    var priority: Int = 0
)
