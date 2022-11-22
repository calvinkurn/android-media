package com.tokopedia.tokopedianow.similarproduct.domain

import com.tokopedia.tokopedianow.common.util.NumberFormatter
import com.tokopedia.tokopedianow.similarproduct.domain.model.ProductRecommendationResponse.ProductRecommendationWidgetSingle.Data.RecommendationItem
import com.tokopedia.tokopedianow.similarproduct.model.SimilarProductUiModel

object SimilarProductMapper {
    fun mapToProductUiModel(index: Int, product: RecommendationItem): SimilarProductUiModel? {
        val position = index + 1
        return product.stock?.let { stock ->
            product.price?.let { price ->
                product.imageUrl?.let { imageUrl ->
                    product.categoryBreadcrumbs?.let { categoryName ->
                        SimilarProductUiModel(
                            id = product.id.toString(),
                            shopId = product.shop?.id.toString(),
                            name = product.name.toString(),
                            stock = stock,
                            minOrder = product.wholesalePrice?.firstOrNull()?.quantityMin?:1,
                            maxOrder = product.wholesalePrice?.firstOrNull()?.quantityMax?:stock,
                            priceFmt = price,
                            imageUrl = imageUrl,
                            slashedPrice = product.slashedPrice?:"",
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
