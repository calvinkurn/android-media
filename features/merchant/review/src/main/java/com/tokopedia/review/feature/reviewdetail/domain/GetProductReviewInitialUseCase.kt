package com.tokopedia.review.feature.reviewdetail.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.review.feature.reviewdetail.data.ProductFeedbackDetailResponse
import com.tokopedia.review.feature.reviewdetail.data.ProductFeedbackFilterResponse
import com.tokopedia.review.feature.reviewdetail.data.ProductReviewDetailOverallResponse
import com.tokopedia.review.feature.reviewdetail.data.ProductReviewInitialDataResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created by Yehezkiel on 28/04/20
 */
@GqlQuery(GetProductReviewInitialUseCase.GET_PRODUCT_REVIEW_DETAIL_OVERALL_QUERY_CLASS_NAME, GetProductReviewInitialUseCase.GET_PRODUCT_REVIEW_DETAIL_OVERALL_QUERY)
class GetProductReviewInitialUseCase @Inject constructor(
        private val graphQlRepository: GraphqlRepository
) : UseCase<ProductReviewInitialDataResponse>() {

    companion object {
        private const val PRODUCT_ID = "productID"
        private const val FILTER_BY = "filterBy"
        private const val SORT_BY = "sortBy"
        private const val LIMIT = "limit"
        private const val PAGE = "page"
        private const val TIME_FILTER = "timeFilter"
        const val GET_PRODUCT_FEEDBACK_FILTER_QUERY_CLASS_NAME = "ProductFeedbackFilter"
        const val GET_PRODUCT_FEEDBACK_FILTER_QUERY = """
        query get_product_feedback_detail(${'$'}productID: Int!, ${'$'}sortBy: String!, ${'$'}filterBy: String!, ${'$'}limit: Int!, ${'$'}page: Int!) {
          productrevFeedbackDataPerProduct(productID: ${'$'}productID, sortBy: ${'$'}sortBy,
            filterBy: ${'$'}filterBy, limit: ${'$'}limit, page: ${'$'}page) {
              topics {
                  title
                  count
                  formatted
              }
              aggregatedRating {
                  rating
                  ratingCount
              }
              reviewCount
            }
        }
        """
        const val GET_PRODUCT_REVIEW_DETAIL_OVERALL_QUERY_CLASS_NAME = "ReviewDetailOverall"
        const val GET_PRODUCT_REVIEW_DETAIL_OVERALL_QUERY = """
        query get_product_review_detail_overall(${'$'}productID: Int!, ${'$'}filterBy: String!) {
             productrevGetReviewAggregateByProduct(productID: ${'$'}productID, filterBy: ${'$'}filterBy) {
               productName
               ratingAverage
               ratingCount
               period
            }
        }
        """

        @JvmStatic
        fun createParams(productID: Int, filterBy: String, sortBy: String, page: Int, timeFilter: String): RequestParams = RequestParams.create().apply {
            putInt(PRODUCT_ID, productID)
            putString(FILTER_BY, filterBy)
            putString(SORT_BY, sortBy)
            putInt(PAGE, page)
            putString(TIME_FILTER, timeFilter)
        }
    }

    var requestParams: RequestParams = RequestParams.EMPTY

    @GqlQuery(GET_PRODUCT_FEEDBACK_FILTER_QUERY_CLASS_NAME, GET_PRODUCT_FEEDBACK_FILTER_QUERY)
    override suspend fun executeOnBackground(): ProductReviewInitialDataResponse {
        val productReviewInitialResponse = ProductReviewInitialDataResponse()

        val productId = requestParams.getInt(PRODUCT_ID, 0)
        val filterBy = requestParams.getString(FILTER_BY, "")
        val sortBy = requestParams.getString(SORT_BY, "")
        val page = requestParams.getInt(PAGE, 0)
        val timeFilter = requestParams.getString(TIME_FILTER, "")

        val overAllRatingParams = mapOf(PRODUCT_ID to productId, FILTER_BY to filterBy)
        val overAllRatingRequest = GraphqlRequest(ReviewDetailOverall.GQL_QUERY, ProductReviewDetailOverallResponse::class.java,
                overAllRatingParams)

        val feedbackDetailListParams = mapOf(PRODUCT_ID to productId,
                SORT_BY to sortBy,
                FILTER_BY to filterBy,
                LIMIT to 10,
                PAGE to page)
        val feedbackDetailListRequest = GraphqlRequest(FeedbackDetailList.GQL_QUERY, ProductFeedbackDetailResponse::class.java,
                feedbackDetailListParams)

        val feedbackFilterParams = mapOf(PRODUCT_ID to productId,
                SORT_BY to sortBy,
                FILTER_BY to timeFilter,
                LIMIT to 10,
                PAGE to 0)
        val feedbackDetailFilterRequest = GraphqlRequest(ProductFeedbackFilter.GQL_QUERY, ProductFeedbackFilterResponse::class.java,
                feedbackFilterParams)

        val requests = mutableListOf(overAllRatingRequest, feedbackDetailListRequest, feedbackDetailFilterRequest)

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

            if (gqlResponse.getError(ProductFeedbackFilterResponse::class.java)?.isNotEmpty() != true) {
                productReviewInitialResponse.productReviewFilterResponse = gqlResponse.getData<ProductFeedbackFilterResponse>(ProductFeedbackFilterResponse::class.java)
            } else {
                productReviewInitialResponse.productReviewFilterResponse = null
            }
        } catch (e: Throwable) {

        }

        return productReviewInitialResponse
    }

}