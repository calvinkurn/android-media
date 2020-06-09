package com.tokopedia.one.click.checkout.common.data.model.response.preference

import com.google.gson.annotations.SerializedName

data class PreferenceListResponse(
        @SerializedName("error_message")
        val errorMessage: List<String> = emptyList(),
        @SerializedName("status")
        val status: String = "",
        @SerializedName("data")
        val data: PreferenceListData = PreferenceListData()
)