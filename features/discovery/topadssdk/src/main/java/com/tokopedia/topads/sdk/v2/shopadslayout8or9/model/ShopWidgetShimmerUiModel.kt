package com.tokopedia.topads.sdk.v2.shopadslayout8or9.model

import com.tokopedia.topads.sdk.v2.shopadslayout8or9.factory.ShopWidgetFactory

class ShopWidgetShimmerUiModel: ShopWidgetItem {

    override fun type(typeFactory: ShopWidgetFactory): Int {
        return typeFactory.type(this)
    }
}
