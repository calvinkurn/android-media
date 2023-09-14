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
                            id = product.id.toString(),
                            shopId = product.shop?.id.toString(),
                            shopName = product.shop?.name.toString(),
                            name = product.name.toString(),
                            stock = stock,
                            isVariant = isVariant(parentID),
                            minOrder = product.minOrder,
                            maxOrder = product.maxOrder,
                            priceFmt = price,
                            imageUrl = imageUrl,
                            slashedPrice = product.slashedPrice.orEmpty(),
                            discountPercentage = formatFloatToString(product.discountPercentage),
                            categoryId = product.departmentId.toString(),
                            categoryName = categoryName,
                            position = position,
                            warehouseIds = product.warehouseIds.orEmpty()
                        )
                    }
                }
            }
        }
    }

}
