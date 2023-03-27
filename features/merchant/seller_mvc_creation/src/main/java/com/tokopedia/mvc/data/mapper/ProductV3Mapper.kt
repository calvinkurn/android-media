package com.tokopedia.mvc.data.mapper


import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.mvc.data.response.ProductV3Response
import com.tokopedia.mvc.domain.entity.Variant
import com.tokopedia.mvc.domain.entity.VariantResult
import javax.inject.Inject

class ProductV3Mapper @Inject constructor() {

    fun map(response: ProductV3Response): VariantResult {
        val variant = response.getProductV3.variant

        val selection = variant.selections.map {
            val options = it.options.map { VariantResult.Selection.Option(it.value) }
            VariantResult.Selection(options)
        }
        val imageUrl = response.getProductV3.pictures.firstOrNull()?.urlThumbnail.orEmpty()

        val products = variant.products.map {
            Variant(
                variantId = it.productID.toLongOrZero(),
                variantName = "",
                combinations = it.combination,
                imageUrl = imageUrl,
                price = it.price.toLongOrZero(),
                isSelected = false,
                stockCount = it.stock,
                soldCount = response.getProductV3.txStats.itemSold,
                isEligible = true,
                reason = "",
                isCheckable = true,
                isDeletable = false
            )
        }

        return VariantResult(
            response.getProductV3.productName,
            response.getProductV3.price.toLongOrZero(),
            response.getProductV3.stock,
            response.getProductV3.txStats.itemSold,
            imageUrl,
            selection,
            products
        )
    }
}
