package com.tokopedia.shop_showcase.shop_showcase_product_add.domain.mapper

import com.tokopedia.shop_showcase.shop_showcase_product_add.data.model.ProductListResponse
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.model.ShowcaseProduct
import javax.inject.Inject

/**
 * @author by Rafli Syam on 2020-03-09
 */

class ProductMapper @Inject constructor() {

    companion object {
        private const val RATING_DIVISION = 20
    }

    fun mapToUIModel(productList: List<ProductListResponse.ProductList.Data>): List<ShowcaseProduct> {
        return productList.map {
            ShowcaseProduct(
                productId = it.id,
                productName = it.name,
                productPrice = it.price.max,
                ratingStarAvg = (it.manageProductData.scoreV3 / RATING_DIVISION).toFloat(),
                totalReview = it.stats.countReview,
                productImageUrl = it.pictures.firstOrNull()?.urlThumbnail.orEmpty(),
            )
        }
    }

}
