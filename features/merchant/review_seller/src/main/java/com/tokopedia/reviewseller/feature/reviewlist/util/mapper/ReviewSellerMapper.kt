package com.tokopedia.reviewseller.feature.reviewlist.util.mapper

import com.tokopedia.reviewseller.feature.reviewlist.data.ProductRatingOverallResponse
import com.tokopedia.reviewseller.feature.reviewlist.data.ProductReviewListResponse
import com.tokopedia.reviewseller.feature.reviewlist.view.model.ProductRatingOverallModel
import com.tokopedia.reviewseller.feature.reviewlist.view.model.ProductReviewUiModel

object ReviewSellerMapper {

    fun mapToProductReviewListUiModel(productShopRatingAggregate: ProductReviewListResponse.ProductShopRatingAggregate): List<ProductReviewUiModel> {
        val productReviewListUiModel = mutableListOf<ProductReviewUiModel>()

        productShopRatingAggregate.data.forEach {
            productReviewListUiModel.add(ProductReviewUiModel(
                    productID = it.product.productID,
                    productImageUrl = it.product.productImageURL,
                    productName = it.product.productName,
                    rating = it.rating,
                    reviewCount = it.reviewCount
            ))
        }
        return productReviewListUiModel
    }

    fun mapToProductRatingOverallModel(productGetProductRatingOverallByShop: ProductRatingOverallResponse.ProductGetProductRatingOverallByShop): ProductRatingOverallModel {
        return ProductRatingOverallModel().apply {
            rating = productGetProductRatingOverallByShop.rating
            reviewCount = productGetProductRatingOverallByShop.reviewCount
            period = productGetProductRatingOverallByShop.filterBy
        }
    }
}