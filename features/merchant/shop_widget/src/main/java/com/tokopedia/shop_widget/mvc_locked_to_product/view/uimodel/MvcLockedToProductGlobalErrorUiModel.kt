package com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop_widget.mvc_locked_to_product.view.adapter.MvcLockedToProductTypeFactory

class MvcLockedToProductGlobalErrorUiModel(
    val errorTitle: String = "",
    val errorDescription: String = "",
    val globalErrorType: Int = 0
): Visitable<MvcLockedToProductTypeFactory> {
    override fun type(typeFactory: MvcLockedToProductTypeFactory): Int {
        return typeFactory.type(this)
    }
}