package com.tokopedia.product.detail.common.data.model.variant.uimodel

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.common.data.model.variant.Variant
import com.tokopedia.product.detail.common.data.model.variant.VariantOption

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
    val hasCustomImages: Boolean = false, // If one of all the child dont have image, it will return false. If all of the child have custom image then will return true
    val impressHolder: ImpressHolder = ImpressHolder()
) {

    companion object {

        fun create(
            variantOption: VariantOption,
            variant: Variant,
            state: Int,
            stock: Int,
            hasCustomImages: Boolean,
            isFlashSale: Boolean,
            level: Int
        ) = VariantOptionWithAttribute(
            variantName = variantOption.value.orEmpty(),
            variantId = variantOption.id.orEmpty(),
            image100 = variantOption.picture?.url100.orEmpty(),
            imageOriginal = variantOption.picture?.original.orEmpty(),
            variantHex = variantOption.hex.orEmpty(),
            currentState = state,
            stock = stock,
            hasCustomImages = hasCustomImages,
            level = level,
            variantOptionIdentifier = variant.identifier.orEmpty(),
            variantCategoryKey = variant.pv.toString(),
            flashSale = isFlashSale
        )
    }
}
