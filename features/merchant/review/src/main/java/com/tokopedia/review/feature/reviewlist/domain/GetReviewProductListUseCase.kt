package com.tokopedia.review.feature.reviewlist.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.review.feature.reviewlist.data.ProductReviewListResponse
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetReviewProductListUseCase @Inject constructor(
    private val graphQlRepository: GraphqlRepository
): UseCase<ProductReviewListResponse.ProductShopRatingAggregate>() {

    companion object {
        private const val SORT_BY = "sortBy"
        private const val FILTER_BY = "filterBy"
        private const val LIMIT = "limit"
        private const val PAGE = "page"
        const val PRODUCT_REVIEW_LIST_CLASS_NAME = "ProductReviewList"
        const val PRODUCT_REVIEW_LIST = """
            query get_product_review_list(${'$'}sortBy: String!, ${'$'}filterBy: String, ${'$'}limit: Int!, ${'$'}page: Int!) {
            	productrevShopRatingAggregate(sortBy: ${'$'}sortBy, filterBy: ${'$'}filterBy, limit: ${'$'}limit, page: ${'$'}page) {
                data {
                    product {
                        productID
                        productName
                        productImageURL
                    }
                    rating
                    reviewCount
                    isKejarUlasan
                }
                hasNext
            	}
            }
        """

        @JvmStatic
        fun createParams(sortBy: String, filterBy: String, limit: Int, page: Int): Map<String, Any> {
            return mapOf(SORT_BY to sortBy, FILTER_BY to filterBy, LIMIT to limit, PAGE to page)
        }
    }

    var params = mapOf<String, Any>()

    @GqlQuery(PRODUCT_REVIEW_LIST_CLASS_NAME, PRODUCT_REVIEW_LIST)
    override suspend fun executeOnBackground(): ProductReviewListResponse.ProductShopRatingAggregate {
        val gqlRequest = GraphqlRequest(ProductReviewList.GQL_QUERY, ProductReviewListResponse::class.java, params)
        val gqlResponse = graphQlRepository.getReseponse(listOf(gqlRequest))
        val error = gqlResponse.getError(GraphqlError::class.java)
        if (error == null || error.isEmpty()) {
            return gqlResponse.getData<ProductReviewListResponse>(ProductReviewListResponse::class.java).productShopRatingAggregate
        } else {
            throw MessageErrorException(error.joinToString(", ") { it.message })
        }
    }

}