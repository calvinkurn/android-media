package com.tokopedia.tkpd.flashsale.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class ReservedProduct(
    val products : List<Product>,
    val totalProduct: Int
) {
    @Parcelize
    data class Product(
        val childProducts: List<ChildProduct>,
        val isMultiWarehouse: Boolean,
        val isParentProduct: Boolean,
        val name: String,
        val picture: String,
        val price: Price,
        val productCriteria: ProductCriteria,
        val productId: Long,
        val sku: String,
        val stock: Int,
        val url: String,
        val warehouses: List<Warehouse>
    ):Parcelable {
        @Parcelize
        data class ChildProduct(
            val disabledReason: String,
            val isDisabled: Boolean,
            val isMultiwarehouse: Boolean,
            val isToggleOn: Boolean,
            val name: String,
            val picture: String,
            val price: Price,
            val productId: Long,
            val sku: String,
            val stock: Int,
            val url: String,
            val warehouses: List<Warehouse>
        ): Parcelable {
            @Parcelize
            data class Price(
                val lowerPrice: String,
                val price: String,
                val upperPrice: String
            ):Parcelable
        }
        @Parcelize
        data class Price(val lowerPrice: Long, val price: Long, val upperPrice: Long): Parcelable
        @Parcelize
        data class ProductCriteria(
            val criteriaId: Long,
            val maxCustomStock: Int,
            val maxDiscount: Long,
            val maxFinalPrice: Long,
            val minCustomStock: Int,
            val minDiscount: Long,
            val minFinalPrice: Long,
        ):Parcelable
        @Parcelize
        data class Warehouse(
            val warehouseId: Long,
            val name: String,
            val stock: Long,
            val price: Long,
            val discountSetup: DiscountSetup,
            val isDilayaniTokopedia: Boolean,
            val isToggleOn: Boolean,
            val isDisabled: Boolean,
            val disabledReason: String
        ):Parcelable {
            @Parcelize
            data class DiscountSetup(
                val discount: Int,
                val price: Long,
                val stock: Long
            ):Parcelable
        }
    }
}
