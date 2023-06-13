package com.tokopedia.sellerorder.detail.data.model

data class SomDetailResolution(
    val title: String = "",
    val status: String = "",
    val description: String = "",
    val picture: String = "",
    val showDeadline: Boolean = false,
    val deadlineDateTime: String = "",
    val backgroundColor: String = "",
    val redirectPath: String = "",
    val resolutionStatusFontColor: String = ""
)