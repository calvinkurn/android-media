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
            product.wholesalePrice?.first()?.quantityMin?.let { it1 ->
                product.wholesalePrice.first()?.quantityMax?.let { it2 ->
                    product.price?.let { it3 ->
                        product.imageUrl?.let { it4 ->
                            product.categoryBreadcrumbs?.let { it5 ->
                                SimilarProductUiModel(
                                    id = product.id.toString(),
                                    shopId = product.shop?.id.toString(),
                                    name = product.name.toString(),
                                    stock = it,
                                    minOrder = it1,
                                    maxOrder = it2,
                                    priceFmt = it3,
                                    imageUrl = it4,
                                    slashedPrice = product.price,
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
    }

}
