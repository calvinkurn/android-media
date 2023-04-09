package com.tokopedia.otp.verification.view.uimodel

import com.google.gson.annotations.SerializedName
import com.tokopedia.otp.verification.domain.pojo.ModeListData

data class DefaultOtpUiModel(
    val footerText: String,
    val footerClickableSpan: String,
    val linkType: Int,
    val footerAction: () -> Unit = {},
    @SerializedName("defaultMode")
    var defaultMode: Int = 0,
    @SerializedName("defaultBehaviorMode")
    var defaultBehaviorMode: Int = 0,
    val originalOtpModeList: ArrayList<ModeListData> = arrayListOf(),
    val displayedModeList: ArrayList<ModeListData> = arrayListOf()
)
