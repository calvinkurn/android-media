package com.tokopedia.shop_showcase.shop_showcase_product_add.domain.mapper

import com.tokopedia.shop_showcase.shop_showcase_product_add.data.model.Product
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.model.ShowcaseProduct
import javax.inject.Inject

/**
 * @author by Rafli Syam on 2020-03-09
 */

class ProductMapper @Inject constructor() {

    fun mapToUIModel(productList: List<Product>): List<ShowcaseProduct> {
        return productList.map {
            ShowcaseProduct(
                    productId = it.productId,
                    productName = it.productName,
                    productPrice = it.productPrice.value,
                    ratingStarAvg = (it.productStatistic.rating/20).toFloat(),
                    totalReview = it.productStatistic.totalReview,
                    productImageUrl = it.productImage.thumbnail,
                    isPo = it.productFlags.isPreorder,
                    isWholesale = it.productFlags.isWholesale,
                    isWishlist = it.productFlags.isWishlist,
                    isSold = it.productFlags.isSold
            )
        }
    }

}