package com.tokopedia.shop.flashsale.domain.entity

data class HighlightableProduct(
    val id: Long,
    val parentId : Long,
    val name: String,
    val imageUrl: String,
    val originalPrice: Long,
    val discountedPrice: Long,
    val discountPercentage: Int,
    val customStock: Long,
    val warehouses: List<Warehouse>,
    val maxOrder: Int,
    val disabled: Boolean,
    val isSelected: Boolean,
    val position: Int,
    val disabledReason : DisabledReason,
    val highlightProductWording : String
) {
    data class Warehouse(
        val warehouseId: Long,
        val customStock: Long,
        val isSelected: Boolean
    )

    enum class DisabledReason {
        NOT_DISABLED,
        MAX_PRODUCT_REACHED,
        OTHER_PRODUCT_WITH_SAME_PARENT_ID_ALREADY_SELECTED
    }
}