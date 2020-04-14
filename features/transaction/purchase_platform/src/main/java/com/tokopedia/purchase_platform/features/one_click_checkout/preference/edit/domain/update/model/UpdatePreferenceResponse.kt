package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.domain.update.model

import com.google.gson.annotations.SerializedName

data class UpdatePreferenceResponse (
        @SerializedName("error_message")
        val errorMessage: List<String> = emptyList(),
        @SerializedName("status")
        val status: String = "",
        @SerializedName("data")
        val data: UpdatePreferenceData = UpdatePreferenceData()
)