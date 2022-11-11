package com.tokopedia.privacycenter.main.section.privacypolicy.domain.data

import com.google.gson.annotations.SerializedName

abstract class PrivacyPolicyBaseResponseDataModel(
    @SerializedName("resp_code")
    val respCode: String = "",
    @SerializedName("resp_desc")
    val respDesc: String = ""
)

data class PrivacyPolicyListResponse(
    @SerializedName("data")
    val data: List<PrivacyPolicyDataModel> = listOf()
): PrivacyPolicyBaseResponseDataModel()

data class PrivacyPolicyDetailResponse(
    @SerializedName("data")
    val data: PrivacyPolicyDetailDataModel = PrivacyPolicyDetailDataModel()
): PrivacyPolicyBaseResponseDataModel()

data class PrivacyPolicyDataModel(
    @SerializedName("section_id")
    val sectionId: String = "",
    @SerializedName("section_title")
    val sectionTitle: String = "",
    @SerializedName("topic_id")
    val topicId: String = "",
    @SerializedName("language")
    val language: String = "",
    @SerializedName("last_update")
    val lastUpdate: String = "",
    var isLoading: Boolean = false
)

data class PrivacyPolicyDetailDataModel(
    @SerializedName("section_id")
    val sectionId: String = "",
    @SerializedName("section_title")
    val sectionTitle: String = "",
    @SerializedName("section_content")
    val sectionContent: String = ""
)
