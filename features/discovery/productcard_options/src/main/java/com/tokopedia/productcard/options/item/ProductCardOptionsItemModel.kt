package com.tokopedia.productcard.options.item

internal data class ProductCardOptionsItemModel(
        val title: String = "",
        val onClick: () -> Unit = { }
)