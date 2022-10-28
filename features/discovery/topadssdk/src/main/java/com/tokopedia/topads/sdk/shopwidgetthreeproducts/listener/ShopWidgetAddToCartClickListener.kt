package com.tokopedia.topads.sdk.shopwidgetthreeproducts.listener

import com.tokopedia.topads.sdk.shopwidgetthreeproducts.model.ProductItemModel

interface ShopWidgetAddToCartClickListener {
    fun onAdToCartClicked(productItemModel: ProductItemModel)
}
