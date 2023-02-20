package com.tokopedia.purchase_platform.common.feature.coachmarkplus

import com.google.gson.annotations.SerializedName

data class CoachmarkPlusResponse(
    @SerializedName("Plus")
    val plus: CoachmarkPlusDataResponse = CoachmarkPlusDataResponse()
)

data class CoachmarkPlusDataResponse(
    @SerializedName("is_shown")
    val isShown: Boolean = false,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("content")
    val content: String = "",
)
