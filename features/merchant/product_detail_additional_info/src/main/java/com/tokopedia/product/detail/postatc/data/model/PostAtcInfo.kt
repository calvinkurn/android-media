package com.tokopedia.product.detail.postatc.data.model

import com.tokopedia.product.detail.common.postatc.PostAtc

data class PostAtcInfo(
    val addons: Addons? = null,
    val cartId: String = "",
    val categoryName: String = "",
    val categoryId: String = "",
    val condition: String = "",
    val footer: Footer = Footer(),
    val layoutId: String = "",
    val layoutName: String = "",
    val originalPrice: Double = 0.0,
    val pageSource: String = "",
    val price: Double = 0.0,
    val productId: String = "",
    val session: String = "",
    val shopId: String = ""
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

    data class Addons(
        val isFulfillment: Boolean,
        val selectedAddonsIds: List<String>,
        val warehouseId: String,
        val quantity: Int
    ) {
        companion object {
            fun parse(addons: PostAtc.Addons) = Addons(
                isFulfillment = addons.isFulfillment,
                selectedAddonsIds = addons.selectedAddonsIds,
                warehouseId = addons.warehouseId,
                quantity = addons.quantity
            )
        }
    }
}
