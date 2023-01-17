package com.tokopedia.topads.sdk.shopwidgetthreeproducts.model

import com.tokopedia.topads.sdk.shopwidgetthreeproducts.factory.ShopWidgetFactory

class ShopWidgetShimmerUiModel: ShopWidgetItem {

    override fun type(typeFactory: ShopWidgetFactory): Int {
        return typeFactory.type(this)
    }
}