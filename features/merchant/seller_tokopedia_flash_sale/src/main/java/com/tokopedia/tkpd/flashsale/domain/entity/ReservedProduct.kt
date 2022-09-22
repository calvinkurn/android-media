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
    ) : Parcelable {

        fun getTotalLocation(): Int {
            return if (isParentProduct) {
                childProducts.sumOf {
                    filteredWarehouse(it.warehouses).size
                }
            } else {
                filteredWarehouse(warehouses).size
            }
        }

        fun getCampaignStock(): Long {
            return if (isParentProduct) {
                childProducts.sumOf {
                    getWarehouseDiscountedStock(it.warehouses)
                }
            } else {
                getWarehouseDiscountedStock(warehouses)
            }
        }

        private fun getWarehouseDiscountedStock(warehouses: List<Warehouse>): Long {
            return filteredWarehouse(warehouses).sumOf { it.discountSetup.stock }
        }

        private fun filteredWarehouse(warehouses: List<Warehouse>): List<Warehouse> {
            return warehouses.filter { !it.isDisabled }
        }

        fun isDiscounted(): Boolean {
            return if (isParentProduct) {
                childProducts.any {
                    isWarehouseDiscounted(it.warehouses)
                }
            } else {
                isWarehouseDiscounted(warehouses)
            }
        }

        private fun isWarehouseDiscounted(warehouses: List<Warehouse>): Boolean {
            return filteredWarehouse(warehouses).any { !it.discountSetup.discount.isZero() }
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
                        addAll(getDiscountedPriceData(it.warehouses))
                    }
                }
            } else {
                getDiscountedPriceData(warehouses)
            }
        }

        private fun getDiscountedPriceData(warehouses: List<Warehouse>): List<Long> {
            return filteredWarehouse(warehouses).map { it.discountSetup.price }
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
                        addAll(getListOfDiscountPercentage(it.warehouses))
                    }
                }
            } else {
                getListOfDiscountPercentage(warehouses)
            }
        }

        private fun getListOfDiscountPercentage(warehouses: List<Warehouse>): List<Int> {
            return filteredWarehouse(warehouses).map { it.discountSetup.discount }
        }

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
