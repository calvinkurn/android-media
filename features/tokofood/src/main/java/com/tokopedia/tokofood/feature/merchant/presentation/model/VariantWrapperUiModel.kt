package com.tokopedia.tokofood.feature.merchant.presentation.model

import com.tokopedia.kotlin.extensions.view.ZERO

data class VariantWrapperUiModel(
    val productListItem: ProductListItem? = null,
    val merchantId: String = "",
    val merchantName: String = "",
    val position: Int = Int.ZERO
)