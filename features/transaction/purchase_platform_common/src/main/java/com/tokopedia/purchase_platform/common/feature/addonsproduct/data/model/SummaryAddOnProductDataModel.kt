package com.tokopedia.purchase_platform.common.feature.addonsproduct.data.model

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO

data class SummaryAddOnProductDataModel(
    val wording: String = String.EMPTY,
    val type: Int = Int.ZERO
)
