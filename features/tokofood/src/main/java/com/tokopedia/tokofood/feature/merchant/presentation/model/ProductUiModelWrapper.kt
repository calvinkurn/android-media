package com.tokopedia.tokofood.feature.merchant.presentation.model

import com.tokopedia.kotlin.extensions.view.ZERO

data class ProductUiModelWrapper(
    val productUiModel: ProductUiModel? = null,
    val productPosition: Int = Int.ZERO
)