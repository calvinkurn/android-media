package com.tokopedia.productcard.options

import com.tokopedia.productcard.options.item.ProductCardOptionsItemModel

internal interface ProductCardOptionsListener {
    fun onProductCardOptionsItemImpressed(
        option: ProductCardOptionsItemModel,
        adapterPosition: Int,
    )
}
