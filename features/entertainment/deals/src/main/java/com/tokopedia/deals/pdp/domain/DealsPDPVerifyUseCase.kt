package com.tokopedia.deals.pdp.domain

import com.tokopedia.deals.pdp.data.DealsVerifyRequest
import com.tokopedia.deals.pdp.data.DealsVerifyResponse
import com.tokopedia.deals.pdp.domain.query.DealsPDPVerifyQuery
import com.tokopedia.deals.pdp.domain.query.VerifyV2Query
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

class DealsPDPVerifyUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<DealsVerifyResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(VerifyV2Query())
        setTypeClass(DealsVerifyResponse::class.java)
    }

    suspend fun execute(verifyRequest: DealsVerifyRequest): DealsVerifyResponse {
        setRequestParams(DealsPDPVerifyQuery.createRequestParam(verifyRequest))
        return executeOnBackground()
    }
}
