package com.tokopedia.reviewseller.feature.reviewdetail.domain

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.reviewseller.common.GQL_GET_PRODUCT_FEEDBACK_LIST_DETAIL
import com.tokopedia.reviewseller.feature.reviewdetail.data.ProductFeedbackDetailResponse
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

class GetProductFeedbackDetailListUseCase @Inject constructor(
        @Named(GQL_GET_PRODUCT_FEEDBACK_LIST_DETAIL)
        val gqlQuery: String,
        private val gqlUseCase: MultiRequestGraphqlUseCase
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
        gqlUseCase.clearRequest()

        val gqlRequest = GraphqlRequest(gqlQuery, ProductFeedbackDetailResponse::class.java, params)
        gqlUseCase.addRequest(gqlRequest)
        val gqlResponse = gqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(GraphqlError::class.java)
        if (error == null || error.isEmpty()) {
            return gqlResponse.getData<ProductFeedbackDetailResponse>(ProductFeedbackDetailResponse::class.java).productFeedbackDataPerProduct
        } else {
            throw MessageErrorException(error.joinToString(", ") { it.message })
        }
    }

    fun clearCache() {
        gqlUseCase.clearCache()
    }

}