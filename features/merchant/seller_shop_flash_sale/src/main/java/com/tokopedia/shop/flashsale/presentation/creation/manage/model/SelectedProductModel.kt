package com.tokopedia.shop.flashsale.presentation.creation.manage.model

data class SelectedProductModel(
    val productId: String,
    val parentProductId: String?,
    val isProductPreviouslySubmitted: Boolean = false,
    val hasChild: Boolean = false
)
