package com.tokopedia.privacycenter.data

import com.google.gson.annotations.SerializedName

data class PrivacyPolicyListResponse(
    @SerializedName("data")
    val data: List<PrivacyPolicyDataModel> = listOf(),
    @SerializedName("resp_code")
    val respCode: String = "",
    @SerializedName("resp_desc")
    val respDesc: String = ""
)

data class PrivacyPolicyDataModel(
    @SerializedName("section_id")
    val sectionId: String = "",
    @SerializedName("section_title")
    var sectionTitle: String = "",
    @SerializedName("topic_id")
    val topicId: String = "",
    @SerializedName("language")
    val language: String = "",
    @SerializedName("last_update")
    val lastUpdate: String = ""
)
