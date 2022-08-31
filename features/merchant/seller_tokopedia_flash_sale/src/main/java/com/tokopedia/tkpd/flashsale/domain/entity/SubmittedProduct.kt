package com.tokopedia.tkpd.flashsale.domain.entity

import com.google.gson.annotations.SerializedName

data class SubmittedProduct(
    val campaignStock: Int,
    val isMultiwarehouse: Boolean,
    val isParentProduct: Boolean,
    val mainStock: Int,
    val name: String,
    val picture: String,
    val productCriteria: ProductCriteria,
    val productId: Long,
    val url: String,
    val price: Price,
    val discount: Discount,
    val discountedPrice: DiscountedPrice,
    val warehouses: List<Warehouse>
) {

    data class Price(
        val price: Double,
        val lowerPrice: Double,
        val upper_price: Double
    )

    data class Discount(
        val discount: Long,
        val lowerDiscount: Long,
        val upperDiscount: Long
    )

    data class DiscountedPrice(
        val price: Double,
        val lowerPrice: Double,
        val upper_price: Double
    )

    data class Warehouse(
        val name: String,
        val price: Double,
        val rejectionReason: String,
        val statusId: Long,
        val statusText: String,
        val stock: Int,
        val discountSetup: DiscountSetup,
        val subsidy: Subsidy,
        val warehouseId: Long
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
