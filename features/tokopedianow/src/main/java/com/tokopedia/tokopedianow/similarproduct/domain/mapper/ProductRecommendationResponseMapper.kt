package com.tokopedia.tokopedianow.similarproduct.domain.mapper

import com.tokopedia.tokopedianow.common.util.VariantUtil.isVariant
import com.tokopedia.tokopedianow.similarproduct.domain.model.ProductRecommendationResponse.ProductRecommendationWidgetSingle.Data.RecommendationItem
import com.tokopedia.productcard.compact.similarproduct.presentation.uimodel.ProductCardCompactSimilarProductUiModel
import com.tokopedia.tokopedianow.common.util.NumberFormatter.formatFloatToString

object ProductRecommendationResponseMapper {
    private const val POSITION_OFFSET = 1
    fun mapToProductUiModel(index: Int, product: RecommendationItem): ProductCardCompactSimilarProductUiModel? {
        val position = index + POSITION_OFFSET
        return product.stock?.let { stock ->
            product.price?.let { price ->
                product.imageUrl?.let { imageUrl ->
                    product.categoryBreadcrumbs?.let { categoryName ->
                        val parentID = product.parentID
                        ProductCardCompactSimilarProductUiModel(
                            id = product.id.orEmpty(),
                            shopId = product.shop?.id.orEmpty(),
                            shopName = product.shop?.name.orEmpty(),
                            name = product.name.orEmpty(),
                            stock = stock,
                            isVariant = isVariant(parentID),
                            minOrder = product.minOrder,
                            maxOrder = product.maxOrder,
                            priceFmt = price,
                            imageUrl = imageUrl,
                            slashedPrice = product.slashedPrice.orEmpty(),
                            discountPercentage = formatFloatToString(product.discountPercentage),
                            categoryId = product.departmentId.orEmpty(),
                            categoryName = categoryName,
                            position = position,
                            warehouseIds = product.warehouseId.orEmpty()
                        )
                    }
                }
            }
        }
    }

}
