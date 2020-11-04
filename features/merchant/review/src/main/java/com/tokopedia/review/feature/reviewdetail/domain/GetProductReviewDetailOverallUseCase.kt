package com.tokopedia.review.feature.reviewdetail.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.review.feature.reviewdetail.data.ProductReviewDetailOverallResponse
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetProductReviewDetailOverallUseCase @Inject constructor(
        private val graphQlRepository: GraphqlRepository
): UseCase<ProductReviewDetailOverallResponse.ProductGetReviewAggregateByProduct>() {

    companion object {
        private const val PRODUCT_ID = "productID"
        private const val FILTER_BY = "filterBy"
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
        fun createParams(productID: Int, filterBy: String): Map<String, Any> = mapOf(PRODUCT_ID to productID, FILTER_BY to filterBy)
    }

    var params = mapOf<String, Any>()

    @GqlQuery(GET_PRODUCT_REVIEW_DETAIL_OVERALL_QUERY_CLASS_NAME, GET_PRODUCT_REVIEW_DETAIL_OVERALL_QUERY)
    override suspend fun executeOnBackground(): ProductReviewDetailOverallResponse.ProductGetReviewAggregateByProduct {
        val gqlRequest = GraphqlRequest(ReviewDetailOverall.GQL_QUERY, ProductReviewDetailOverallResponse::class.java, params)
        val gqlResponse = graphQlRepository.getReseponse(listOf(gqlRequest))
        val error = gqlResponse.getError(GraphqlError::class.java)
        if (error == null || error.isEmpty()) {
            return gqlResponse.getData<ProductReviewDetailOverallResponse>(ProductReviewDetailOverallResponse::class.java).productGetReviewAggregateByProduct
        } else {
            throw MessageErrorException(error.joinToString(", ") { it.message} )
        }
    }
}