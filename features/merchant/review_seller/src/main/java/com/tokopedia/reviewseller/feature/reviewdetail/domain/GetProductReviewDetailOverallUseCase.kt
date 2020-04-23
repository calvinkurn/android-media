package com.tokopedia.reviewseller.feature.reviewdetail.domain

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.reviewseller.common.util.GQL_GET_PRODUCT_REVIEW_DETAIL_OVERALL
import com.tokopedia.reviewseller.feature.reviewdetail.data.ProductReviewDetailOverallResponse
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

class GetProductReviewDetailOverallUseCase @Inject constructor(
        @Named(GQL_GET_PRODUCT_REVIEW_DETAIL_OVERALL)
        val gqlQuery: String,
        private val graphQlRepository: GraphqlRepository
): UseCase<ProductReviewDetailOverallResponse.ProductGetReviewAggregateByProduct>() {

    companion object {
        private const val PRODUCT_ID = "productID"
        private const val FILTER_BY = "filterBy"

        @JvmStatic
        fun createParams(productID: Int, filterBy: String): Map<String, Any> = mapOf(PRODUCT_ID to productID,FILTER_BY to filterBy)
    }

    var params = mapOf<String, Any>()

    override suspend fun executeOnBackground(): ProductReviewDetailOverallResponse.ProductGetReviewAggregateByProduct {
        val gqlRequest = GraphqlRequest(gqlQuery, ProductReviewDetailOverallResponse::class.java, params)
        val gqlResponse = graphQlRepository.getReseponse(listOf(gqlRequest))
        val error = gqlResponse.getError(GraphqlError::class.java)
        if (error == null || error.isEmpty()) {
            return gqlResponse.getData<ProductReviewDetailOverallResponse>(ProductReviewDetailOverallResponse::class.java).productGetReviewAggregateByProduct
        } else {
            throw MessageErrorException(error.joinToString(", ") { it.message })
        }
    }
}