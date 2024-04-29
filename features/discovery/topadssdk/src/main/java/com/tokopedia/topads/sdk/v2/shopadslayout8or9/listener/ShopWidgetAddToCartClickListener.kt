package com.tokopedia.topads.sdk.v2.shopadslayout8or9.listener

import com.tokopedia.topads.sdk.v2.shopadslayout8or9.model.ProductItemModel

interface ShopWidgetAddToCartClickListener {
    fun onAdToCartClicked(productItemModel: ProductItemModel)
}
