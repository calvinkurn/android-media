package com.tokopedia.deals.ui.pdp.domain

import com.tokopedia.deals.ui.pdp.domain.query.DealsProductDetailQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

class DealsPDPDetailUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<com.tokopedia.deals.ui.pdp.data.DealsProductDetail>(graphqlRepository) {

    init {
        setGraphqlQuery(DealsProductDetailQuery())
        setTypeClass(com.tokopedia.deals.ui.pdp.data.DealsProductDetail::class.java)
    }

    suspend fun execute(productId: String): com.tokopedia.deals.ui.pdp.data.DealsProductDetail {
        setRequestParams(createRequestParam(productId))
        return executeOnBackground()
    }

    private fun createRequestParam(productId: String) = HashMap<String, Any>().apply {
        put(PRODUCT_ID_KEY, productId)
    }

    companion object {
        private const val PRODUCT_ID_KEY = "urlPDP"
    }
}
