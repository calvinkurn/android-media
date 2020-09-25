package com.tokopedia.review.feature.reviewdetail.domain

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.review.feature.reviewdetail.data.ProductFeedbackDetailResponse
import com.tokopedia.review.feature.reviewdetail.util.GqlQueryDetail
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


        @JvmStatic
        fun createParams(productID: Int, sortBy: String, filterBy: String, limit: Int, page: Int): Map<String, Any> =
                mapOf(PRODUCT_ID to productID,
                        SORT_BY to sortBy,
                        FILTER_BY to filterBy,
                        LIMIT to limit,
                        PAGE to page)
    }

    var params = mapOf<String, Any>()

    override suspend fun executeOnBackground(): ProductFeedbackDetailResponse.ProductFeedbackDataPerProduct {
        val gqlRequest = GraphqlRequest(GqlQueryDetail.GET_PRODUCT_FEEDBACK_LIST_DETAIL, ProductFeedbackDetailResponse::class.java, params)
        val gqlResponse = graphQlRepository.getReseponse(listOf(gqlRequest))
        val error = gqlResponse.getError(GraphqlError::class.java)
        if (error == null || error.isEmpty()) {
            return gqlResponse.getData<ProductFeedbackDetailResponse>(ProductFeedbackDetailResponse::class.java).productrevFeedbackDataPerProduct
        } else {
            throw MessageErrorException(error.joinToString(", ") { it.message} )
        }
    }

}