package com.tokopedia.purchase_platform.features.one_click_checkout.preference.list.domain.model

import com.google.gson.annotations.SerializedName

data class SetDefaultPreferenceData(
        @SerializedName("success")
        val success: Int = 0,
        @SerializedName("messages")
        val messages: List<String> = emptyList()
)