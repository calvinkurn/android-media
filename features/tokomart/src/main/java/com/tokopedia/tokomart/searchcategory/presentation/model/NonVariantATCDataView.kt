package com.tokopedia.tokomart.searchcategory.presentation.model

data class NonVariantATCDataView(
        val minQuantity: Int = 0,
        val maxQuantity: Int = 0,
        var quantity: Int = 0,
)