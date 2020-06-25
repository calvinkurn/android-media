package com.tokopedia.oneclickcheckout.preference.list.domain.model

import com.google.gson.annotations.SerializedName

data class SetDefaultPreferenceResponse(
        @SerializedName("error_message")
        val errorMessage: List<String> = emptyList(),
        @SerializedName("status")
        val status: String = "",
        @SerializedName("data")
        val data: SetDefaultPreferenceData = SetDefaultPreferenceData()
)