package com.tokopedia.tkpd.flashsale.presentation.manageproduct.variant.multilocation.varian.adapter

import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.uimodel.ValidationResult

interface ManageProductVariantAdapterListener {
    fun onDataInputChanged(
        index: Int,
        criteria: ReservedProduct.Product.ProductCriteria,
        discountSetup: ReservedProduct.Product.Warehouse.DiscountSetup
    ): ValidationResult

    fun validationItem(
        criteria: ReservedProduct.Product.ProductCriteria,
        discountSetup: ReservedProduct.Product.Warehouse.DiscountSetup
    ): ValidationResult

    fun calculatePrice(percentInput: Long, adapterPosition: Int): String

    fun calculatePercent(priceInput: Long, adapterPosition: Int): String

    fun showDetailCriteria(selectedWarehouse: ReservedProduct.Product.Warehouse)

    fun trackOnClickPrice(nominalInput : String)

    fun trackOnClickPercent(nominalInput : String)

    fun trackOnToggle()
}
