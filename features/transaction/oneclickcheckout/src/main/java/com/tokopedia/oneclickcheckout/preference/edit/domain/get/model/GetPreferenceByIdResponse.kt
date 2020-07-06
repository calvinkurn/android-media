package com.tokopedia.oneclickcheckout.preference.edit.domain.get.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.oneclickcheckout.common.data.model.ProfilesItem

data class GetPreferenceByIdGqlResponse(
        @SerializedName("get_profile_by_id_occ")
        val response: GetPreferenceByIdResponse = GetPreferenceByIdResponse()
) {
        fun getErrorMessage(): String? {
                return response.data.messages.firstOrNull() ?: response.errorMessage.firstOrNull()
        }
}

data class GetPreferenceByIdResponse(
        @SerializedName("error_message")
        val errorMessage: List<String> = emptyList(),
        @SerializedName("status")
        val status: String = "",
        @SerializedName("data")
        val data: GetPreferenceByIdData = GetPreferenceByIdData()
)

data class GetPreferenceByIdData(
        @SerializedName("success")
        val success: Int = 0,
        @SerializedName("profile")
        val profile: ProfilesItem = ProfilesItem(),
        @SerializedName("messages")
        val messages: List<String> = emptyList()
)