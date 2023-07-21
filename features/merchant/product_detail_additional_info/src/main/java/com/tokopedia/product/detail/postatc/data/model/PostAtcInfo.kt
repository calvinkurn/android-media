package com.tokopedia.product.detail.postatc.data.model

data class PostAtcInfo(
    val productId: String = "",
    val cartId: String = "",
    val categoryId: String = "",
    val categoryName: String = "",
    val footer: Footer = Footer(),
    val isFulfillment: Boolean = false,
    val layoutId: String = "",
    val layoutName: String = "",
    val pageSource: String = "",
    val selectedAddonsIds: List<String> = emptyList(),
    val shopId: String = "",
    val warehouseId: String = "",
    val quantity: Int = 0,
    val price: Double = 0.0,
    val originalPrice: Double = 0.0,
    val condition: String = ""
) {
    data class Footer(
        val image: String = "",
        val description: String = "",
        val info: String = "",
        val buttonText: String = "",
        val cartId: String = ""
    ) {
        val shouldShow: Boolean
            get() = image.isNotEmpty() && description.isNotEmpty()

    }
}
