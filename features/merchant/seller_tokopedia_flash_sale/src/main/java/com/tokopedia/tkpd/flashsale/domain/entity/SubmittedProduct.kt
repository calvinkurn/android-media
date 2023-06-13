package com.tokopedia.tkpd.flashsale.domain.entity

import com.tokopedia.tkpd.flashsale.domain.entity.enums.ProductStockStatus

data class SubmittedProduct(
    val campaignStock: Int,
    val isMultiwarehouse: Boolean,
    val isParentProduct: Boolean,
    val totalChild: Int,
    val soldCount: Int,
    val mainStock: Int,
    val name: String,
    val picture: String,
    val productCriteria: ProductCriteria,
    val productId: Long,
    val url: String,
    val price: Price,
    val discount: Discount,
    val discountedPrice: DiscountedPrice,
    val submittedProductStockStatus: ProductStockStatus,
    val warehouses: List<Warehouse>,
    val totalSubsidy: Long,
    val statusText: String,
    val countLocation: Int
) {

    data class Price(
        val price: Double,
        val lowerPrice: Double,
        val upperPrice: Double
    )

    data class Discount(
        val discount: Long,
        val lowerDiscount: Long,
        val upperDiscount: Long
    )

    data class DiscountedPrice(
        val price: Double,
        val lowerPrice: Double,
        val upperPrice: Double
    )

    data class Warehouse(
        val name: String,
        val price: Double,
        val rejectionReason: String,
        val statusId: Long,
        val statusText: String,
        val stock: Int,
        val discountSetup: DiscountSetup?,
        val subsidy: Subsidy,
        val warehouseId: Long,
        val soldCount: Int
    )

    data class DiscountSetup(
        val price: Double,
        val stock: Long,
        val discount: Long
    )

    data class Subsidy(
        val hasSubsidy: Boolean,
        val subsidyAmount: Long
    )

    data class ProductCriteria(
        val criteriaId: Long
    )
}
