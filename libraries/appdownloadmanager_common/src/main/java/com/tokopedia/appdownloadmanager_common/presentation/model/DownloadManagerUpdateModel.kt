package com.tokopedia.appdownloadmanager_common.presentation.model

import com.google.gson.annotations.SerializedName

data class DownloadManagerUpdateModel(
    @SerializedName("dialog_button_negative")
    val dialogButtonNegative: String = "",
    @SerializedName("dialog_button_positive")
    val dialogButtonPositive: String = "",
    @SerializedName("dialog_text")
    val dialogText: String = "",
    @SerializedName("dialog_title")
    val dialogTitle: String = "",
    @SerializedName("expire_time")
    val expireTime: Int = 0,
    @SerializedName("is_enabled")
    val isEnabled: Boolean = false
)
