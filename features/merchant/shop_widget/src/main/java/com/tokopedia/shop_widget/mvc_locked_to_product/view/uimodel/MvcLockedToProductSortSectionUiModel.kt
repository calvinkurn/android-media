package com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop_widget.mvc_locked_to_product.view.adapter.MvcLockedToProductTypeFactory

data class MvcLockedToProductSortSectionUiModel(
    val totalProduct: Int = 0,
    val totalProductWording: String = "",
    var selectedSortData: MvcLockedToProductSortUiModel = MvcLockedToProductSortUiModel()
) : Visitable<MvcLockedToProductTypeFactory> {
    override fun type(typeFactory: MvcLockedToProductTypeFactory): Int {
        return typeFactory.type(this)
    }
}