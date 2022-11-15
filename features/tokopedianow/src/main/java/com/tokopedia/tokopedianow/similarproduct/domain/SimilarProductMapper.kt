package com.tokopedia.tokopedianow.similarproduct.domain

import com.tokopedia.tokopedianow.common.util.NumberFormatter
import com.tokopedia.tokopedianow.similarproduct.domain.model.RecommendationItem
import com.tokopedia.tokopedianow.similarproduct.model.SimilarProductUiModel

object SimilarProductMapper {
    fun mapToProductUiModel(index: Int, product: RecommendationItem): SimilarProductUiModel? {
        val position = index + 1

//        val similarProducts = product.mapIndexed { idx, similarProduct ->
//            mapToProductUiModel(idx, similarProduct)
//        }.orEmpty()

        return product.stock?.let {
            product.price?.let { it3 ->
                product.imageUrl?.let { it4 ->
                    product.categoryBreadcrumbs?.let { it5 ->
                        SimilarProductUiModel(
                            id = product.id.toString(),
                            shopId = product.shop?.id.toString(),
                            name = product.name.toString(),
                            stock = it,
                            minOrder = product.wholesalePrice?.firstOrNull()?.quantityMin?:1,
                            maxOrder = product.wholesalePrice?.firstOrNull()?.quantityMax?:it,
                            priceFmt = it3,
                            imageUrl = it4,
                            slashedPrice = product.slashedPrice?:"",
                            discountPercentage = NumberFormatter.formatFloatToString(product.discountPercentage),
                            categoryId = product.departmentId.toString(),
                            categoryName = it5,
                            position = position
                        )
                    }
                }
            }
        }
    }

}
