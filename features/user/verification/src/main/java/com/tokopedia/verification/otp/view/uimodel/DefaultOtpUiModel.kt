package com.tokopedia.verification.otp.view.uimodel

import com.google.gson.annotations.SerializedName
import com.tokopedia.verification.otp.domain.pojo.ModeListData

data class DefaultOtpUiModel(
    val footerText: String,
    val footerClickableSpan: String,
    val linkType: Int = 0,
    val footerAction: () -> Unit = {},
    @SerializedName("defaultMode")
    var defaultMode: Int = 0,
    @SerializedName("defaultBehaviorMode")
    var defaultBehaviorMode: Int = 0,
    val originalOtpModeList: ArrayList<ModeListData> = arrayListOf(),
    val displayedModeList: ArrayList<ModeListData> = arrayListOf()
)
