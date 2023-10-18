package com.tokopedia.purchase_platform.common.feature.dynamicdatapassing.data.model

import com.google.gson.annotations.SerializedName

data class UpdateDynamicDataPassingUiModel(
    @SerializedName("dynamic_data")
    val dynamicData: String = ""
)
