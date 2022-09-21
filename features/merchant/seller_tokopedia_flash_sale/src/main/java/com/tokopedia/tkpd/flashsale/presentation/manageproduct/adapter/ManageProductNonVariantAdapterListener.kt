package com.tokopedia.tkpd.flashsale.presentation.manageproduct.adapter

import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.uimodel.ValidationResult

interface ManageProductNonVariantAdapterListener {
    fun onDataInputChanged(
        index: Int,
        criteria: ReservedProduct.Product.ProductCriteria,
        discountSetup: ReservedProduct.Product.Warehouse.DiscountSetup
    ): ValidationResult

    fun calculatePrice(percentInput: Long, adapterPosition: Int): String

    fun calculatePercent(priceInput: Long, adapterPosition: Int): String
}