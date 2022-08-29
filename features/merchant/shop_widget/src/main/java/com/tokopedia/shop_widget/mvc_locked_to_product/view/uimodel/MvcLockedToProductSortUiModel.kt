package com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop_widget.mvc_locked_to_product.view.adapter.MvcLockedToProductSortListBottomSheetTypeFactory

data class MvcLockedToProductSortUiModel(
    val name: String = "",
    val value: String = "",
    var isSelected: Boolean = false
) : Visitable<MvcLockedToProductSortListBottomSheetTypeFactory> {

    override fun type(typeFactory: MvcLockedToProductSortListBottomSheetTypeFactory): Int {
        return typeFactory.type(this)
    }
}