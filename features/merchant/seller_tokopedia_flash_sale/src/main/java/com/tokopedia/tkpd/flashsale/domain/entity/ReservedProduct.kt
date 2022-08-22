package com.tokopedia.tkpd.flashsale.domain.entity

data class ReservedProduct(
    val products : List<Product>,
    val totalProduct: Int
) {
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
    ) {
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
        ) {
            data class Price(
                val lowerPrice: String,
                val price: String,
                val upperPrice: String
            )
        }
        data class Price(val lowerPrice: Long, val price: Long, val upperPrice: Long)
        data class ProductCriteria(
            val criteriaId: Long,
            val maxCustomStock: Int,
            val maxDiscount: Long,
            val maxFinalPrice: Long,
            val minCustomStock: Int,
            val minDiscount: Long,
            val minFinalPrice: Long,
        )
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
        ) {
            data class DiscountSetup(
                val discount: Int,
                val price: Long,
                val stock: Long
            )
        }
    }
}
