package com.tokopedia.review.feature.reviewlist.view.model

import com.tokopedia.review.feature.reviewlist.data.ProductRatingOverallResponse
import com.tokopedia.review.feature.reviewlist.data.ProductReviewListResponse
import com.tokopedia.usecase.coroutines.Result

data class ProductRatingWrapperUiModel(
        var productRatingOverall: Result<ProductRatingOverallResponse.ProductGetProductRatingOverallByShop>? = null,
        var reviewProductList: Result<ProductReviewListResponse.ProductShopRatingAggregate>? = null
)