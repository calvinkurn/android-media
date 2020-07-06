package com.tokopedia.oneclickcheckout.common.data.model

import com.google.gson.annotations.SerializedName

data class PreferenceListGqlResponse (
        @SerializedName("get_all_profiles_occ")
        val response: PreferenceListResponse = PreferenceListResponse()
) {
        fun getErrorMessage(): String? {
                return response.data.messages.firstOrNull() ?: response.errorMessage.firstOrNull()
        }
}

data class PreferenceListResponse(
        @SerializedName("error_message")
        val errorMessage: List<String> = emptyList(),
        @SerializedName("status")
        val status: String = "",
        @SerializedName("data")
        val data: PreferenceListData = PreferenceListData()
)