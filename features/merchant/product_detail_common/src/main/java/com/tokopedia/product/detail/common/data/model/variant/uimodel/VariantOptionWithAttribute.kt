package com.tokopedia.product.detail.common.data.model.variant.uimodel

/**
 * Created by Yehezkiel on 08/03/20
 */
data class VariantOptionWithAttribute(
        val variantId: String = "",
        val currentState: Int = 0,
        val variantHex: String = "",
        val variantName: String = "",
        val image100: String = "",
        val imageOriginal: String = "",
        val isBuyable: Boolean = false,
        val stock: Int = 0,
        val variantOptionIdentifier: String = "",
        val variantCategoryKey: String = "",
        val selectedStockWording: String = "",
        val level: Int = -1,
        val flashSale: Boolean = false,
        val hasCustomImages: Boolean = false // If one of all the child dont have image, it will return false. If all of the child have custom image then will return true
)