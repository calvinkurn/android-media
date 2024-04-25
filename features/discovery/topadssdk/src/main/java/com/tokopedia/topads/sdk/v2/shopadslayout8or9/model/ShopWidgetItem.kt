package com.tokopedia.topads.sdk.v2.shopadslayout8or9.model

import com.tokopedia.topads.sdk.v2.shopadslayout8or9.factory.ShopWidgetFactory

interface ShopWidgetItem {
    fun type(typeFactory: ShopWidgetFactory): Int
}
