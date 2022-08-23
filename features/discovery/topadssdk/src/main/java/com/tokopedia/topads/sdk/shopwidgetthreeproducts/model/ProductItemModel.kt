package com.tokopedia.topads.sdk.shopwidgetthreeproducts.model

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.shopwidgetthreeproducts.factory.ShopWidgetFactory

class ProductItemModel(
    var cpmData: CpmData,
    var productCardModel: ProductCardModel,
    var applinks: String,
    var mUrl: String,
    var adsClickUrl: String,
    var productId: String,
    var productName: String,
    var productMinOrder: Int,
    var productCategory: String,
    var productPrice: String,
    var shopId: String
) : ImpressHolder(), ShopWidgetItem {

    override fun type(typeFactory: ShopWidgetFactory): Int {
        return typeFactory.type(this)
    }
}
