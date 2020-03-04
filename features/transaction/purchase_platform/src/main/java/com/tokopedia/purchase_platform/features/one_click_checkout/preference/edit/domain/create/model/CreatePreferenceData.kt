package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.domain.create.model

import com.google.gson.annotations.SerializedName

data class CreatePreferenceData(
        @SerializedName("success")
        val success: Int = 0,
        @SerializedName("messages")
        val messages: List<String> = emptyList()
)