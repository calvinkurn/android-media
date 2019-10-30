package com.tokopedia.tkpd.tkpdreputation.createreputation.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tkpd.tkpdreputation.createreputation.model.ProductRevGetForm
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject
import javax.inject.Named

class GetProductReputationForm @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                   @Named("review_form") private val rawQuery: String) {

    companion object {
        const val REPUTATION_ID = "reputationId"
        const val PRODUCT_ID = "productId"

        fun createRequestParam(reputationId: Int, productId: Int): RequestParams {
            return RequestParams.create().apply {
                putInt(REPUTATION_ID, reputationId)
                putInt(PRODUCT_ID, productId)
            }
        }
    }

    var forceRefresh = true

    suspend fun getReputationForm(requestParams: RequestParams): ProductRevGetForm {
        val cacheStrategy = GraphqlCacheStrategy.Builder(if (forceRefresh) CacheType.ALWAYS_CLOUD else CacheType.CACHE_FIRST).build()
        val graphqlRequest = GraphqlRequest(rawQuery, ProductRevGetForm::class.java, requestParams.parameters)

        val response = graphqlRepository.getReseponse(listOf(graphqlRequest), cacheStrategy)

        val data: ProductRevGetForm? = response.getData(ProductRevGetForm::class.java)
        val error: List<GraphqlError> = response.getError(GraphqlError::class.java) ?: listOf()

        if (data == null) {
            throw RuntimeException()
        } else if (error.isNotEmpty() && error.first().message.isNotEmpty()) {
            throw MessageErrorException(error.first().message)
        }

        return data
    }
}