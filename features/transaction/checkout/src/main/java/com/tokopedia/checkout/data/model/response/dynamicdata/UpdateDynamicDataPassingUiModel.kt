package com.tokopedia.checkout.data.model.response.dynamicdata

import com.google.gson.annotations.SerializedName

data class UpdateDynamicDataPassingUiModel(
    @SerializedName("dynamic_data")
    val dynamicData: String = ""
)
