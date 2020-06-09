package com.tokopedia.one.click.checkout.preference.edit.domain.get.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.one.click.checkout.common.data.model.response.preference.ProfilesItem

data class GetPreferenceByIdData(
        @SerializedName("success")
        val success: Int = 0,
        @SerializedName("profile")
        val profile: ProfilesItem = ProfilesItem(),
        @SerializedName("messages")
        val messages: List<String> = emptyList()
)