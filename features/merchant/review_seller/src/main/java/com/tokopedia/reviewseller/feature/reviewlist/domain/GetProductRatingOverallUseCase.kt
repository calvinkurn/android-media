package com.tokopedia.reviewseller.feature.reviewlist.domain

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.reviewseller.common.util.GQL_GET_PRODUCT_RATING_OVERALL
import com.tokopedia.reviewseller.feature.reviewlist.data.ProductRatingOverallResponse
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

class GetProductRatingOverallUseCase @Inject constructor(
        @Named(GQL_GET_PRODUCT_RATING_OVERALL)
        val gqlQuery: String,
        private val gqlUseCase: MultiRequestGraphqlUseCase
) : UseCase<ProductRatingOverallResponse.ProductGetProductRatingOverallByShop>() {

    companion object {
        private const val FILTER_BY = "filterBy"

        @JvmStatic
        fun createParams(filterBy: String): Map<String, String> = mapOf(FILTER_BY to filterBy)
    }

    var params = mapOf<String, Any>()

    override suspend fun executeOnBackground(): ProductRatingOverallResponse.ProductGetProductRatingOverallByShop {
        gqlUseCase.clearRequest()

        val gqlRequest = GraphqlRequest(gqlQuery, ProductRatingOverallResponse::class.java, params)
        gqlUseCase.addRequest(gqlRequest)
        val gqlResponse = gqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(GraphqlError::class.java)
        if (error == null || error.isEmpty()) {
            return gqlResponse.getData<ProductRatingOverallResponse>(ProductRatingOverallResponse::class.java).getProductRatingOverallByShop
        } else {
            throw MessageErrorException(error.joinToString(", ") { it.message })
        }
    }

    fun clearCache() {
        gqlUseCase.clearCache()
    }

}