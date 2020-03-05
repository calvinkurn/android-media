package com.tokopedia.product.detail.data.model.variant

/**
 * Created by Yehezkiel on 2020-02-26
 */
data class VariantOptionWithAttribute(
        var variantId: Int = 0,
        var currentState: Int = 0,
        var variantHex: String = "",
        var variantName: String = "",
        var image: String = "",
        var isBuyable: Boolean = false,
        var stock: Int = 0,
        var variantOptionIdentifier: String = "",
        var selectedStockWording: String = "",
        var level: Int = -1,
        var hasCustomImages: Boolean = false // If one of all the child dont have image, it will return false. If all of the child have custom image then will return true
)