package com.tokopedia.purchase_platform.common.feature.ethicaldrug.data.response

import com.google.gson.annotations.SerializedName

class EpharmacyEnablerResponse(
    @SerializedName("label_name")
    val labelName: String = "",
    @SerializedName("show_label")
    val showLabel: Boolean = false
)
