package com.tokopedia.purchase_platform.features.one_click_checkout.common.data.model.response.preference

import com.google.gson.annotations.SerializedName

data class PreferenceListData(
        @SerializedName("success")
        val success: Int = 0,
        @SerializedName("profiles")
        val profiles: List<ProfilesItem> = emptyList(),
        @SerializedName("messages")
        val messages: List<String> = emptyList(),
        @SerializedName("max_profile")
        val maxProfile: Int = 0

)