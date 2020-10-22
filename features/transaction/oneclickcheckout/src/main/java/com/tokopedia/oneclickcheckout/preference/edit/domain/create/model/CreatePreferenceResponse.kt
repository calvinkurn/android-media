package com.tokopedia.oneclickcheckout.preference.edit.domain.create.model

import com.google.gson.annotations.SerializedName

data class CreatePreferenceGqlResponse(
        @SerializedName("insert_profile_occ")
        val response: CreatePreferenceResponse
) {
        fun getErrorMessage(): String? {
                return response.data.messages.firstOrNull() ?: response.errorMessage.firstOrNull()
        }
}

data class CreatePreferenceResponse(
        @SerializedName("error_message")
        val errorMessage: List<String> = emptyList(),
        @SerializedName("status")
        val status: String = "",
        @SerializedName("data")
        val data: CreatePreferenceData
)

data class CreatePreferenceData(
        @SerializedName("success")
        val success: Int = 0,
        @SerializedName("messages")
        val messages: List<String> = emptyList()
)