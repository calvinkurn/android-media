package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.domain.get.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.model.response.preference.ProfilesItem

data class GetPreferenceByIdData(
        @SerializedName("success")
        val success: Int = 0,
        @SerializedName("profile")
        val profile: ProfilesItem = ProfilesItem(),
        @SerializedName("messages")
        val messages: List<String> = emptyList()
)