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

        val products =  variant.products.map {
            Variant(
                it.productID.toLongOrZero(),
                "",
                it.combination,
                "",
                it.price.toLongOrZero(),
                false,
                it.stock,
                0
            )
        }

        return VariantResult(selection, products)
    }
}
