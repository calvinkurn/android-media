package com.tokopedia.oneclickcheckout.preference.edit.domain.delete.model

import com.google.gson.annotations.SerializedName

data class DeletePreferenceGqlResponse(
        @SerializedName("delete_profile_occ")
        val response: DeletePreferenceResponse = DeletePreferenceResponse()
) {
        fun getErrorMessage(): String? {
                return response.data.messages.firstOrNull() ?: response.errorMessage.firstOrNull()
        }
}

data class DeletePreferenceResponse(
        @SerializedName("error_message")
        val errorMessage: List<String> = emptyList(),
        @SerializedName("status")
        val status: String = "",
        @SerializedName("data")
        val data: DeletePreferenceData = DeletePreferenceData()
)

data class DeletePreferenceData(
        @SerializedName("success")
        val success: Int = 0,
        @SerializedName("messages")
        val messages: List<String> = emptyList()
)