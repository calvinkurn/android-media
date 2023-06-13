package com.tokopedia.mvc.data.mapper

import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.mvc.data.response.ProductListResponse
import com.tokopedia.mvc.domain.entity.Product
import com.tokopedia.mvc.domain.entity.ProductResult
import javax.inject.Inject

class ProductListMapper @Inject constructor() {

    fun map(response: ProductListResponse): ProductResult {
        val products = response.productList.data.map { product ->
            Product(
                product.id.toLongOrZero(),
                product.isVariant,
                product.name,
                product.pictures.firstOrNull()?.urlThumbnail.orEmpty(),
                Product.Preorder(product.preorder.durationDays),
                Product.Price(product.price.min, product.price.max),
                product.sku,
                product.status,
                product.stock,
                Product.TxStats(product.txStats.sold),
                product.warehouseCount,
                selectedVariantsIds = emptySet(),
                isEligible = true,
                ineligibleReason = "",
                originalVariants = emptyList(),
                isSelected = false,
                enableCheckbox = true,
                isDeletable = false
            )
        }
        return ProductResult(response.productList.meta.totalHits, products)
    }

}
