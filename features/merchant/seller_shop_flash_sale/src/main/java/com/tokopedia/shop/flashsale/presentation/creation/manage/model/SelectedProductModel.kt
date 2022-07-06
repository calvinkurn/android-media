package com.tokopedia.shop.flashsale.presentation.creation.manage.model

data class SelectedProductModel(
    var productId: String,
    var parentProductId: String?,
    var isProductPreviouslySubmitted: Boolean = false
)
