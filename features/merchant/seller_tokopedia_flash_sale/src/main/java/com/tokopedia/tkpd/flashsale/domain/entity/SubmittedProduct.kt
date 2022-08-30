package com.tokopedia.tkpd.flashsale.domain.entity

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
    val warehouses: List<Warehouse>
) {
    data class Warehouse(
        val name: String,
        val price: Double,
        val rejectionReason: String,
        val statusId: Long,
        val statusText: String,
        val stock: Int,
        val warehouseId: Long
    )

    data class ProductCriteria(
        val criteriaId: Long
    )
}
