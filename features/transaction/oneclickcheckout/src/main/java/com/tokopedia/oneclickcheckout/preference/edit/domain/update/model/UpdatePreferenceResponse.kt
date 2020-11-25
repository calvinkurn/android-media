package com.tokopedia.oneclickcheckout.preference.edit.domain.update.model

import com.google.gson.annotations.SerializedName

data class UpdatePreferenceGqlResponse(
        @SerializedName("update_profile_occ")
        val response: UpdatePreferenceResponse
) {
        fun getErrorMessage(): String? {
                return response.data.messages.firstOrNull() ?: response.errorMessage.firstOrNull()
        }
}

data class UpdatePreferenceResponse (
        @SerializedName("error_message")
        val errorMessage: List<String> = emptyList(),
        @SerializedName("status")
        val status: String = "",
        @SerializedName("data")
        val data: UpdatePreferenceData = UpdatePreferenceData()
)

data class UpdatePreferenceData(
        @SerializedName("success")
        val success: Int = 0,
        @SerializedName("messages")
        val messages: List<String> = emptyList()
)