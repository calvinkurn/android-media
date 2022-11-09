package com.tokopedia.tkpd.flashsale.presentation.manageproduct.variant.singlelocation.adapter

import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.uimodel.ValidationResult

interface ManageProductVariantListener {
    fun onDataInputChanged(
        index: Int,
        product: ReservedProduct.Product,
        discountSetup: ReservedProduct.Product.Warehouse.DiscountSetup
    ): ValidationResult

    fun onToggleSwitch(index: Int, isChecked: Boolean)

    fun onDiscountChange(index: Int, priceValue: Long, discountValue: Int)

    fun onStockChange(index: Int, stockValue: Long)

    fun calculatePrice(percentInput: Long, adapterPosition: Int): String

    fun calculatePercent(priceInput: Long, adapterPosition: Int): String

    fun onMultiWarehouseClicked(
        adapterPosition: Int,
        selectedProduct: ReservedProduct.Product.ChildProduct
    )

    fun onIneligibleProductWithSingleWarehouseClicked(
        index: Int,
        selectedProduct: ReservedProduct.Product.ChildProduct,
        productCriteria: ReservedProduct.Product.ProductCriteria
    )
}
