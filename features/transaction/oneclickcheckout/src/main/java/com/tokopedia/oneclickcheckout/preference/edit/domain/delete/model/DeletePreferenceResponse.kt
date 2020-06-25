package com.tokopedia.oneclickcheckout.preference.edit.domain.delete.model

import com.google.gson.annotations.SerializedName

data class DeletePreferenceResponse(
        @SerializedName("error_message")
        val errorMessage: List<String> = emptyList(),
        @SerializedName("status")
        val status: String = "",
        @SerializedName("data")
        val data: DeletePreferenceData = DeletePreferenceData()
)