package com.tokopedia.tkpd.flashsale.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero

data class ReservedProduct(
    val products: List<Product>,
    val totalProduct: Int
) {
    @Parcelize
    data class Product(
        var childProducts: List<ChildProduct> = listOf(),
        val isMultiWarehouse: Boolean = false,
        val isParentProduct: Boolean = false,
        val name: String = "",
        val picture: String = "",
        val price: Price = Price(),
        val productCriteria: ProductCriteria = ProductCriteria(),
        val productId: Long = 0,
        val sku: String = "",
        val stock: Int = 0,
        val url: String = "",
        val warehouses: List<Warehouse> = listOf()
    ) : Parcelable {

        fun getTotalDiscountedLocation(): Int {
            return if (isParentProduct) {
                childProducts.map { childProduct ->
                    childProduct.warehouses.filteredWarehouse().map { warehouse ->
                        warehouse
                    }
                }.flatten().distinctBy { it.warehouseId }.size
            } else {
                warehouses.filteredWarehouse().distinctBy { it.warehouseId }.size
            }
        }

        fun getDiscountedProductCampaignStock(): Long {
            return if (isParentProduct) {
                childProducts.sumOf {
                    it.warehouses.filteredWarehouse().getWarehouseDiscountedStock()
                }
            } else {
                warehouses.filteredWarehouse().getWarehouseDiscountedStock()
            }
        }

        private fun List<Warehouse>.getWarehouseDiscountedStock(): Long {
            return sumOf { it.discountSetup.stock }
        }

        fun isDiscounted(): Boolean {
            return if (isParentProduct) {
                childProducts.any {
                    it.warehouses.filteredWarehouse().isNotEmpty()
                }
            } else {
                warehouses.filteredWarehouse().isNotEmpty()
            }
        }

        fun getMinDiscountedPrice(): Long {
            return getListOfDiscountedPrice().minOrNull().orZero()
        }

        fun getMaxDiscountedPrice(): Long {
            return getListOfDiscountedPrice().maxOrNull().orZero()
        }

        private fun getListOfDiscountedPrice(): List<Long> {
            return if (isParentProduct) {
                mutableListOf<Long>().apply {
                    childProducts.minOf {
                        addAll(it.warehouses.filteredWarehouse().getDiscountedPriceData())
                    }
                }
            } else {
                warehouses.filteredWarehouse().getDiscountedPriceData()
            }
        }

        private fun List<Warehouse>.getDiscountedPriceData(): List<Long> {
            return map { it.discountSetup.price }
        }

        fun getMinDiscountPercentage(): Int {
            return getListOfDiscountPercentage().minOrNull().orZero()
        }

        fun getMaxDiscountPercentage(): Int {
            return getListOfDiscountPercentage().maxOrNull().orZero()
        }

        private fun getListOfDiscountPercentage(): List<Int> {
            return if (isParentProduct) {
                mutableListOf<Int>().apply {
                    childProducts.minOf {
                        addAll(it.warehouses.filteredWarehouse().getListOfDiscountPercentage())
                    }
                }
            } else {
                warehouses.filteredWarehouse().getListOfDiscountPercentage()
            }
        }

        private fun List<Warehouse>.getListOfDiscountPercentage(): List<Int> {
            return map { it.discountSetup.discount }
        }

        fun List<Warehouse>.filteredWarehouse(): List<Warehouse> {
            return filter { !it.isDisabled && !it.discountSetup.stock.isZero() && it.isToggleOn}
        }

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
            var discountedPrice: Long = 0,
            var discount: Long = 0,
            val productId: Long,
            val sku: String,
            val stock: Int,
            val url: String,
            var warehouses: List<Warehouse>
        ) : Parcelable

        @Parcelize
        data class Price(
            val lowerPrice: Long = 0,
            val price: Long = 0,
            val upperPrice: Long = 0) : Parcelable

        @Parcelize
        data class ProductCriteria(
            val criteriaId: Long = 0,
            val maxCustomStock: Int = 0 ,
            val maxDiscount: Long = 0,
            val maxFinalPrice: Long = 0,
            val minCustomStock: Int = 0,
            val minDiscount: Long = 0,
            val minFinalPrice: Long = 0,
        ):Parcelable
        @Parcelize
        data class Warehouse(
            val warehouseId: Long,
            val name: String,
            var stock: Long,
            var price: Long,
            var discountSetup: DiscountSetup,
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
