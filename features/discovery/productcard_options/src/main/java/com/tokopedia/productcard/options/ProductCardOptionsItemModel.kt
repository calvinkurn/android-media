package com.tokopedia.productcard.options

internal data class ProductCardOptionsItemModel(
        val title: String = "",
        val onClick: () -> Unit = { }
)