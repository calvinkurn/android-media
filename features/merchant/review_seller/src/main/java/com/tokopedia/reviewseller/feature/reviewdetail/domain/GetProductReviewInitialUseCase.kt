package com.tokopedia.reviewseller.feature.reviewdetail.domain

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.reviewseller.common.util.GQL_GET_PRODUCT_FEEDBACK_LIST_DETAIL
import com.tokopedia.reviewseller.common.util.GQL_GET_PRODUCT_REVIEW_DETAIL_OVERALL
import com.tokopedia.reviewseller.feature.reviewdetail.data.ProductFeedbackDetailResponse
import com.tokopedia.reviewseller.feature.reviewdetail.data.ProductReviewDetailOverallResponse
import com.tokopedia.reviewseller.feature.reviewdetail.data.ProductReviewInitialDataResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created by Yehezkiel on 28/04/20
 */
class GetProductReviewInitialUseCase @Inject constructor(
        val rawQuery: Map<String, String>,
        private val graphQlRepository: GraphqlRepository
) : UseCase<ProductReviewInitialDataResponse>() {

    companion object {
        private const val PRODUCT_ID = "productID"
        private const val FILTER_BY = "filterBy"
        private const val SORT_BY = "sortBy"
        private const val LIMIT = "limit"
        private const val PAGE = "page"

        @JvmStatic
        fun createParams(productID: Int, filterBy: String, sortBy: String, page: Int): RequestParams = RequestParams.create().apply {
            putInt(PRODUCT_ID, productID)
            putString(FILTER_BY, filterBy)
            putString(SORT_BY, sortBy)
            putInt(PAGE, page)
        }
    }

    var requestParams: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): ProductReviewInitialDataResponse {
        val productReviewInitialResponse = ProductReviewInitialDataResponse()

        val productId = requestParams.getInt(PRODUCT_ID, 0)
        val filterBy = requestParams.getString(FILTER_BY, "")
        val limit = requestParams.getInt(LIMIT, 0)
        val sortBy = requestParams.getString(SORT_BY, "")
        val page = requestParams.getInt(PAGE, 0)

        val overAllRatingParams = mapOf(PRODUCT_ID to productId, FILTER_BY to filterBy)
        val overAllRatingRequest = GraphqlRequest(rawQuery[GQL_GET_PRODUCT_REVIEW_DETAIL_OVERALL], ProductReviewDetailOverallResponse::class.java,
                overAllRatingParams)

        val feedbackDetailListParams = mapOf(PRODUCT_ID to productId,
                SORT_BY to sortBy,
                FILTER_BY to filterBy,
                LIMIT to 10,
                PAGE to page)
        val feedbackDetailListRequest = GraphqlRequest(rawQuery[GQL_GET_PRODUCT_FEEDBACK_LIST_DETAIL], ProductFeedbackDetailResponse::class.java,
                feedbackDetailListParams)

        val requests = mutableListOf(overAllRatingRequest, feedbackDetailListRequest)

        try {
            val gqlResponse = graphQlRepository.getReseponse(requests)

            if (gqlResponse.getError(ProductReviewDetailOverallResponse::class.java)?.isNotEmpty() != true) {
                productReviewInitialResponse.productReviewDetailOverallResponse = gqlResponse.getData<ProductReviewDetailOverallResponse>(ProductReviewDetailOverallResponse::class.java)
            } else {
                productReviewInitialResponse.productReviewDetailOverallResponse = null
            }

            if (gqlResponse.getError(ProductFeedbackDetailResponse::class.java)?.isNotEmpty() != true) {
                productReviewInitialResponse.productFeedBackResponse = gqlResponse.getData<ProductFeedbackDetailResponse>(ProductFeedbackDetailResponse::class.java)
            } else {
                productReviewInitialResponse.productFeedBackResponse = null
            }

        } catch (e: Throwable) {

        }

        return productReviewInitialResponse
    }

}