package com.tokopedia.officialstore.official.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Grid
import com.tokopedia.officialstore.official.presentation.dynamic_channel.OfficialStoreFlashSaleCardViewTypeFactory
import com.tokopedia.productcard.ProductCardModel

data class ProductFlashSaleDataModel(
        val productModel: ProductCardModel,
        val grid: Grid,
        val applink: String = ""
) : Visitable<OfficialStoreFlashSaleCardViewTypeFactory>, ImpressHolder() {
    override fun type(typeFactory: OfficialStoreFlashSaleCardViewTypeFactory): Int {
        return typeFactory.type(this)
    }
}