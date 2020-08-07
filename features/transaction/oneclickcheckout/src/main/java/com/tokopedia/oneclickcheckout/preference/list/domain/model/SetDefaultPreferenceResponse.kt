package com.tokopedia.oneclickcheckout.preference.list.domain.model

import com.google.gson.annotations.SerializedName

data class SetDefaultPreferenceGqlResponse(
        @SerializedName("set_default_profile_occ")
        val response: SetDefaultPreferenceResponse = SetDefaultPreferenceResponse()
)

data class SetDefaultPreferenceResponse(
        @SerializedName("error_message")
        val errorMessage: List<String> = emptyList(),
        @SerializedName("status")
        val status: String = "",
        @SerializedName("data")
        val data: SetDefaultPreferenceData = SetDefaultPreferenceData()
)

data class SetDefaultPreferenceData(
        @SerializedName("success")
        val success: Int = 0,
        @SerializedName("messages")
        val messages: List<String> = emptyList()
)