package com.tokopedia.appdownloadmanager_common.domain.model

import com.google.gson.annotations.SerializedName

data class AppVersionBetaInfoModel(
    @SerializedName("versionName")
    val versionName: String,
    @SerializedName("versionCode")
    val versionCode: String
)
