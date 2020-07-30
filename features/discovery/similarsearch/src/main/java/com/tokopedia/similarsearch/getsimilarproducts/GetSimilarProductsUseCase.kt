package com.tokopedia.similarsearch.getsimilarproducts

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.similarsearch.getsimilarproducts.model.SimilarProductModel
import com.tokopedia.usecase.coroutines.UseCase

internal class GetSimilarProductsUseCase(
        private val productId: String,
        private val queryString: String,
        private val graphqlCacheStrategy: GraphqlCacheStrategy,
        private val graphqlRepository: GraphqlRepository
): UseCase<SimilarProductModel>() {

    override suspend fun executeOnBackground(): SimilarProductModel {
        val graphqlRequest = GraphqlRequest(
                queryString, SimilarProductModel::class.java, createParametersForRequest())

        val graphqlResponse = graphqlRepository.getReseponse(listOf(graphqlRequest), graphqlCacheStrategy)

        val error = graphqlResponse.getError(SimilarProductModel::class.java)

        if (error == null || error.isEmpty()){
            return graphqlResponse.getData(SimilarProductModel::class.java)
        } else {
            throw Throwable(error.mapNotNull { it.message }.joinToString(separator = ", "))
        }
    }

    private fun createParametersForRequest(): Map<String, Any> {
        val parameters = mutableMapOf<String, Any>()

        parameters[KEY_PRODUCT_ID] = productId
        parameters[KEY_PARAMS] = "$KEY_PAGE=0&$KEY_PAGE_SIZE=100&$KEY_DEVICE=android"

        return parameters
    }
}