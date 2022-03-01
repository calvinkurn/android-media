package com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop_widget.mvc_locked_to_product.view.adapter.MvcLockedToProductTypeFactory

class MvcLockedToProductVoucherUiModel(
    val shopImage: String = "",
    val title: String = "",
    val baseCode: String = "",
    val expiredWording: String = "",
    val totalQuotaLeft: Int = 0,
    val totalQuotaLeftWording: String = "",
    val minPurchaseWording: String = ""
): Visitable<MvcLockedToProductTypeFactory> {
    override fun type(typeFactory: MvcLockedToProductTypeFactory): Int {
        return typeFactory.type(this)
    }
}