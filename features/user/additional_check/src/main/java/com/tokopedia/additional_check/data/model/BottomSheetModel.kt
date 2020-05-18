package com.tokopedia.additional_check.data.pojo

data class BottomSheetModel(
        @SerializedName("image")
        val status: String = "",
        @SerializedName("text")
        val text: String = "",
        @SerializedName("applink")
        val applink: String = ""
)