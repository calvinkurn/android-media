package com.tokopedia.oneclickcheckout.preference.edit.domain.delete.model

import com.google.gson.annotations.SerializedName

data class DeletePreferenceData(
        @SerializedName("success")
        val success: Int = 0,
        @SerializedName("messages")
        val messages: List<String> = emptyList()
)