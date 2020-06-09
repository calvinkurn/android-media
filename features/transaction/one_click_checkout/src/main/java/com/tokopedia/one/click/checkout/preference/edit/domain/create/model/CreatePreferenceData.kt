package com.tokopedia.one.click.checkout.preference.edit.domain.create.model

import com.google.gson.annotations.SerializedName

data class CreatePreferenceData(
        @SerializedName("success")
        val success: Int = 0,
        @SerializedName("messages")
        val messages: List<String> = emptyList()
)