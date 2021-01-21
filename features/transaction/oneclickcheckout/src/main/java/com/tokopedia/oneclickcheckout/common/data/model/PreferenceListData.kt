package com.tokopedia.oneclickcheckout.common.data.model

import com.google.gson.annotations.SerializedName

data class PreferenceListData(
        @SerializedName("success")
        val success: Int = 0,
        @SerializedName("profiles")
        val profiles: List<ProfilesItem> = emptyList(),
        @SerializedName("messages")
        val messages: List<String> = emptyList(),
        @SerializedName("max_profile")
        val maxProfile: Int = 0,
        @SerializedName("tickers")
        val tickers: List<String> = emptyList(),
        @SerializedName("enable_occ_revamp")
        val enableOccRevamp: Boolean = false
)