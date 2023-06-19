package com.tokopedia.purchase_platform.common.feature.addonsproduct.data.response

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO

data class SummaryAddOnProductResponse(
    @SerializedName("wording")
    val wording: String = String.EMPTY,
    @SerializedName("type")
    val type: Int = Int.ZERO
)
