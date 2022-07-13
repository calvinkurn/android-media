package com.tokopedia.topads.sdk.shopwidgetthreeproducts.model

import com.tokopedia.topads.sdk.shopwidgetthreeproducts.factory.ShopWidgetFactory

interface ShopWidgetItem {
    fun type(typeFactory : ShopWidgetFactory):Int
}
