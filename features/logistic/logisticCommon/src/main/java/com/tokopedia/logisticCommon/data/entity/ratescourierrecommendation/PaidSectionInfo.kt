package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation

import com.google.gson.annotations.SerializedName

data class PaidSectionInfo(
    @SerializedName("is_collapsed")
    val isCollapsed: Boolean = false,
    @SerializedName("label_text")
    val labelText: String = ""
)
