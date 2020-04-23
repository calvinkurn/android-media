package com.tokopedia.reviewseller.feature.reviewlist.domain

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.reviewseller.common.util.GQL_GET_PRODUCT_REVIEW_LIST
import com.tokopedia.reviewseller.feature.reviewlist.data.ProductReviewListResponse
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

class GetReviewProductListUseCase @Inject constructor(
    @Named(GQL_GET_PRODUCT_REVIEW_LIST)
    val gqlQuery: String,
    private val graphQlRepository: GraphqlRepository
): UseCase<ProductReviewListResponse.ProductShopRatingAggregate>() {

    companion object {
        private const val SORT_BY = "sortBy"
        private const val FILTER_BY = "filterBy"
        private const val LIMIT = "limit"
        private const val PAGE = "page"

        @JvmStatic
        fun createParams(sortBy: String, filterBy: String, limit: Int, page: Int): Map<String, Any> {
            return mapOf(SORT_BY to sortBy, FILTER_BY to filterBy, LIMIT to limit, PAGE to page)
        }
    }

    var params = mapOf<String, Any>()

    override suspend fun executeOnBackground(): ProductReviewListResponse.ProductShopRatingAggregate {
        val gqlRequest = GraphqlRequest(gqlQuery, ProductReviewListResponse::class.java, params)
        val gqlResponse = graphQlRepository.getReseponse(listOf(gqlRequest))
        val error = gqlResponse.getError(GraphqlError::class.java)
        if (error == null || error.isEmpty()) {
            return gqlResponse.getData<ProductReviewListResponse>(ProductReviewListResponse::class.java).productShopRatingAggregate
        } else {
            throw MessageErrorException(error.joinToString(", ") { it.message })
        }
    }

}