package com.tokopedia.one.click.checkout.preference.edit.domain.create.model

import com.google.gson.annotations.SerializedName

data class CreatePreferenceResponse(
        @SerializedName("error_message")
        val errorMessage: List<String> = emptyList(),
        @SerializedName("status")
        val status: String = "",
        @SerializedName("data")
        val data: CreatePreferenceData
)