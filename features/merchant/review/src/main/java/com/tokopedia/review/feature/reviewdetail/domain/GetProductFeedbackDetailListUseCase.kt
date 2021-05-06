package com.tokopedia.review.feature.reviewdetail.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.review.feature.reviewdetail.data.ProductFeedbackDetailResponse
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetProductFeedbackDetailListUseCase @Inject constructor(
        private val graphQlRepository: GraphqlRepository
): UseCase<ProductFeedbackDetailResponse.ProductFeedbackDataPerProduct>() {

    companion object {
        private const val PRODUCT_ID = "productID"
        private const val SORT_BY = "sortBy"
        private const val FILTER_BY = "filterBy"
        private const val LIMIT = "limit"
        private const val PAGE = "page"

        const val GET_PRODUCT_FEEDBACK_LIST_DETAIL_QUERY_CLASS_NAME = "FeedbackDetailList"
        const val GET_PRODUCT_FEEDBACK_LIST_DETAIL_QUERY = """
        query get_product_feedback_detail(${'$'}productID: Int!, ${'$'}sortBy: String!, ${'$'}filterBy: String!, ${'$'}limit: Int!, ${'$'}page: Int!) {
            productrevFeedbackDataPerProduct(productID: ${'$'}productID, sortBy: ${'$'}sortBy, filterBy: ${'$'}filterBy, limit: ${'$'}limit, page: ${'$'}page) {
                list {
                    feedbackID
                    rating
                    reviewText
                    reviewTime
                    autoReply
                    replyText
                    replyTime
                    attachments {
                        thumbnailURL
                        fullsizeURL
                    }
                    variantName
                    reviewerName
                    isKejarUlasan
                }
                topics {
                    title
                    count
                    formatted
                }
                aggregatedRating {
                    rating
                    ratingCount
                }
                sortBy
                filterBy
                limit
                page
                hasNext
                reviewCount
            }
        }
        """

        @JvmStatic
        fun createParams(productID: Int, sortBy: String, filterBy: String, limit: Int, page: Int): Map<String, Any> =
                mapOf(PRODUCT_ID to productID,
                        SORT_BY to sortBy,
                        FILTER_BY to filterBy,
                        LIMIT to limit,
                        PAGE to page)
    }

    var params = mapOf<String, Any>()

    @GqlQuery(GET_PRODUCT_FEEDBACK_LIST_DETAIL_QUERY_CLASS_NAME, GET_PRODUCT_FEEDBACK_LIST_DETAIL_QUERY)
    override suspend fun executeOnBackground(): ProductFeedbackDetailResponse.ProductFeedbackDataPerProduct {
        val gqlRequest = GraphqlRequest(FeedbackDetailList.GQL_QUERY, ProductFeedbackDetailResponse::class.java, params)
        val gqlResponse = graphQlRepository.getReseponse(listOf(gqlRequest))
        val error = gqlResponse.getError(GraphqlError::class.java)
        if (error == null || error.isEmpty()) {
            return gqlResponse.getData<ProductFeedbackDetailResponse>(ProductFeedbackDetailResponse::class.java).productrevFeedbackDataPerProduct
        } else {
            throw MessageErrorException(error.joinToString(", ") { it.message} )
        }
    }

}