package com.tokopedia.tkpd.flashsale.presentation.manageproduct.adapter

import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct.Product
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct.Product.Warehouse
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.uimodel.ValidationResult

interface ManageProductNonVariantAdapterListener {
    fun onDataInputChanged(
        index: Int,
        criteria: Product.ProductCriteria,
        discountSetup: Warehouse.DiscountSetup
    ): ValidationResult

    fun calculatePrice(percentInput: Long, adapterPosition: Int): String

    fun calculatePercent(priceInput: Long, adapterPosition: Int): String

    fun showDetailCriteria(position: Int, warehouse: Warehouse, product: Product)

    fun trackOnClickPrice(nominalInput : String)

    fun trackOnClickPercent(percentInput : String)
}
