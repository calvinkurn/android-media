package com.tokopedia.mvc.data.mapper

import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.mvc.data.response.ProductListResponse
import com.tokopedia.mvc.domain.entity.Product
import javax.inject.Inject

class ProductListMapper @Inject constructor() {

    fun map(response: ProductListResponse): List<Product> {
        return response.productList.data.map { product ->
            Product(
                product.id.toLongOrZero(),
                product.isVariant,
                product.name,
                product.pictures.firstOrNull()?.urlThumbnail.orEmpty(),
                Product.Preorder(product.preorder.durationDays),
                Product.Price(product.price.min, product.price.max),
                product.sku,
                Product.Stats(product.stats.countReview, product.stats.countTalk, product.stats.countView),
                product.status,
                product.stock,
                Product.TxStats(product.txStats.sold),
                product.warehouse.map { warehouse -> Product.Warehouse(warehouse.id.toLongOrZero()) },
                product.warehouseCount,
                isEligible = true,
                ineligibleReason = "",
                variants = emptyList()
            )
        }
    }

}
