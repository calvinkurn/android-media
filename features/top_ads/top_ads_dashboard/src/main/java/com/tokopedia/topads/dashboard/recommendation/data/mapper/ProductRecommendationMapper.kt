package com.tokopedia.topads.dashboard.recommendation.data.mapper

import com.tokopedia.topads.dashboard.data.model.ProductRecommendation
import com.tokopedia.topads.dashboard.recommendation.data.model.local.ProductItemUiModel
import javax.inject.Inject

class ProductRecommendationMapper @Inject constructor() {

    fun convertToProductItemUiModel(products: List<ProductRecommendation>): List<ProductItemUiModel> {
        val productList = mutableListOf<ProductItemUiModel>()
        products.forEach {
            productList.add(
                ProductItemUiModel(
                    productId = it.productId,
                    productName = it.productName,
                    imgUrl = it.imgUrl,
                    searchCount = it.searchCount
                )
            )
        }
        return productList
    }
}
