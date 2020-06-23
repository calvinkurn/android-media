package com.tokopedia.reviewseller.feature.reviewdetail.data

/**
 * Created by Yehezkiel on 28/04/20
 */

data class ProductReviewInitialDataResponse(
        var productFeedBackResponse : ProductFeedbackDetailResponse? = null,
        var productReviewDetailOverallResponse: ProductReviewDetailOverallResponse? = null,
        var productReviewFilterResponse: ProductFeedbackFilterResponse? = null
)