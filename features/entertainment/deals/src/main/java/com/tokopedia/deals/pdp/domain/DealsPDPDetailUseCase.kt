package com.tokopedia.deals.pdp.domain

import com.tokopedia.deals.pdp.data.DealsProductDetail
import com.tokopedia.deals.pdp.domain.query.DealsPDPDetailQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

class DealsPDPDetailUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<DealsProductDetail>(graphqlRepository) {

    init {
        setGraphqlQuery(DealsPDPDetailQuery)
        setTypeClass(DealsProductDetail::class.java)
    }

    suspend fun execute(productId: String): DealsProductDetail {
        setRequestParams(DealsPDPDetailQuery.createRequestParam(productId))
        return executeOnBackground()
    }
}