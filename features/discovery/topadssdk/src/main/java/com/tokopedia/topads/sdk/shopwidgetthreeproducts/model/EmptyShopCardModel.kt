package com.tokopedia.topads.sdk.shopwidgetthreeproducts.model

import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.shopwidgetthreeproducts.factory.ShopWidgetFactory

class EmptyShopCardModel(val cpmData: CpmData, val shopApplink: String, val adsClickUrl: String) :
    ShopWidgetItem {

    override fun type(typeFactory: ShopWidgetFactory): Int {
        return typeFactory.type(this)
    }
}
