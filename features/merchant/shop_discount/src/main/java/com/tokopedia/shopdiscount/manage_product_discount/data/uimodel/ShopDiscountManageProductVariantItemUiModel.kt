package com.tokopedia.shopdiscount.manage_product_discount.data.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shopdiscount.manage_product_discount.presentation.adapter.ShopDiscountManageProductVariantDiscountTypeFactory
import java.util.Date

data class ShopDiscountManageProductVariantItemUiModel(
    val variantId: String = "",
    val variantName: String = "",
    val variantMinOriginalPrice: Int = 0,
    val variantMaxOriginalPrice: Int = 0,
    val variantStock: Int = 0,
    val variantTotalLocation: Int = 0,
    var isEnabled: Boolean = false,
    var startDate: Date = Date(),
    var endDate: Date = Date(),
    var discountedPrice: Int = 0,
    var discountedPercentage: Int = 0,
    var maxOrder: String = "",
    val isMultiLoc: Boolean = false,
    val slashPriceStatusId: String = "",
    var valueErrorType: Int = 0,
    var isAbusive: Boolean = false,
    var selectedPeriodChip: Int = 0,
    val averageSoldPrice: Int = 0,
    var productErrorType: Int = 0
) : Visitable<ShopDiscountManageProductVariantDiscountTypeFactory> {

    override fun type(typeFactory: ShopDiscountManageProductVariantDiscountTypeFactory): Int {
        return typeFactory.type(this)
    }
}
