package com.tokopedia.tkpd.flashsale.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class ReservedProduct(
    val products: List<Product>,
    val totalProduct: Int
) {
    @Parcelize
    data class Product(
        var childProducts: List<ChildProduct>,
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
    ) : Parcelable {
        @Parcelize
        data class ChildProduct(
            val disabledReason: String,
            val isDisabled: Boolean,
            val isMultiwarehouse: Boolean,
            var isToggleOn: Boolean,
            val name: String,
            val picture: String,
            val price: Price,
            val productCriteria: ProductCriteria,
            var discountSetup: Warehouse.DiscountSetup = Warehouse.DiscountSetup(
                0,
                0,
                0
            ),
            val productId: Long,
            val sku: String,
            val stock: Int,
            val url: String,
            val warehouses: List<Warehouse>
        ) : Parcelable

        @Parcelize
        data class Price(val lowerPrice: Long, val price: Long, val upperPrice: Long) : Parcelable

        @Parcelize
        data class ProductCriteria(
            val criteriaId: Long,
            val maxCustomStock: Int,
            val maxDiscount: Long,
            val maxFinalPrice: Long,
            val minCustomStock: Int,
            val minDiscount: Long,
            val minFinalPrice: Long,
        ) : Parcelable

        @Parcelize
        data class Warehouse(
            val warehouseId: Long,
            val name: String,
            val stock: Long,
            val price: Long,
            val discountSetup: DiscountSetup,
            val isDilayaniTokopedia: Boolean,
            var isToggleOn: Boolean,
            val isDisabled: Boolean,
            val disabledReason: String
        ) : Parcelable {
            @Parcelize
            data class DiscountSetup(
                var discount: Int,
                var price: Long,
                var stock: Long
            ) : Parcelable
        }
    }
}