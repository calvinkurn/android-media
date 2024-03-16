package com.tokopedia.topads.sdk.v2.shopadslayout8or9.model

import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.v2.shopadslayout8or9.factory.ShopWidgetFactory

class EmptyShopCardModel(val cpmData: CpmData, val shopApplink: String, val adsClickUrl: String) :
    ShopWidgetItem {

    override fun type(typeFactory: ShopWidgetFactory): Int {
        return typeFactory.type(this)
    }
}
