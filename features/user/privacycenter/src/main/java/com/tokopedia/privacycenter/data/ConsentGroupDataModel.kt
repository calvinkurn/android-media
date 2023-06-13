package com.tokopedia.privacycenter.data

import com.google.gson.annotations.SerializedName

data class GetConsentGroupListDataModel(
    @SerializedName("GetConsentGroupList")
    val consentGroupList: ConsentGroupListDataModel = ConsentGroupListDataModel()
)

data class ConsentGroupListDataModel(
    @SerializedName("success")
    val success: Boolean = false,
    @SerializedName("refId")
    val refId: String = "",
    @SerializedName("errorMessages")
    val errorMessages: List<String> = listOf(),
    @SerializedName("groups")
    val groups: List<ConsentGroupDataModel> = listOf(),
    @SerializedName("ticker")
    val ticker: String = ""
)

data class ConsentGroupDataModel(
    @SerializedName("id")
    val id: String = "0",
    @SerializedName("groupTitle")
    val groupTitle: String = "",
    @SerializedName("groupSubtitle")
    val groupSubtitle: String = "",
    @SerializedName("groupImg")
    val groupImage: String = "",
    @SerializedName("priority")
    val priority: Int = 0
)
