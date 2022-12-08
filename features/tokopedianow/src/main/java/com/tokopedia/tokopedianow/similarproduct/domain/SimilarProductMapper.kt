package com.tokopedia.tokopedianow.similarproduct.domain

import com.tokopedia.tokopedianow.common.util.NumberFormatter
import com.tokopedia.tokopedianow.similarproduct.domain.model.ProductRecommendationResponse.ProductRecommendationWidgetSingle.Data.RecommendationItem
import com.tokopedia.tokopedianow.similarproduct.model.SimilarProductUiModel

object SimilarProductMapper {
    private const val POSITION_OFFSET = 1
    fun mapToProductUiModel(index: Int, product: RecommendationItem): SimilarProductUiModel? {
        val position = index + POSITION_OFFSET
        return product.stock?.let { stock ->
            product.price?.let { price ->
                product.imageUrl?.let { imageUrl ->
                    product.categoryBreadcrumbs?.let { categoryName ->
                        SimilarProductUiModel(
                            id = product.id.toString(),
                            shopId = product.shop?.id.toString(),
                            shopName = product.shop?.name.toString(),
                            name = product.name.toString(),
                            stock = stock,
                            minOrder = product.minOrder,
                            maxOrder = product.maxOrder,
                            priceFmt = price,
                            imageUrl = imageUrl,
                            slashedPrice = product.slashedPrice.orEmpty(),
                            discountPercentage = NumberFormatter.formatFloatToString(product.discountPercentage),
                            categoryId = product.departmentId.toString(),
                            categoryName = categoryName,
                            position = position
                        )
                    }
                }
            }
        }
    }

}
