package com.tokopedia.productcard_compact.similarproduct.domain.mapper

import com.tokopedia.productcard_compact.common.util.NumberUtil
import com.tokopedia.productcard_compact.similarproduct.domain.model.ProductRecommendationResponse.ProductRecommendationWidgetSingle.Data.RecommendationItem
import com.tokopedia.productcard_compact.similarproduct.presentation.uimodel.SimilarProductUiModel

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
                            discountPercentage = NumberUtil.formatFloatToString(product.discountPercentage),
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
