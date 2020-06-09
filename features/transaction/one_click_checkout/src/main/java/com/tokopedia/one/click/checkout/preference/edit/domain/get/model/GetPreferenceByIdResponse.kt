package com.tokopedia.one.click.checkout.preference.edit.domain.get.model

import com.google.gson.annotations.SerializedName

data class GetPreferenceByIdResponse(
        @SerializedName("error_message")
        val errorMessage: List<String> = emptyList(),
        @SerializedName("status")
        val status: String = "",
        @SerializedName("data")
        val data: GetPreferenceByIdData = GetPreferenceByIdData()
)