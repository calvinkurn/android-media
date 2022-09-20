package com.tokopedia.deals.pdp.domain

import com.tokopedia.common_entertainment.data.DealsVerifyRequest
import com.tokopedia.common_entertainment.data.DealsVerifyResponse
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
        setRequestParams(createRequestParam(verifyRequest))
        return executeOnBackground()
    }

    private fun createRequestParam(dealsVerifyRequest: DealsVerifyRequest) = HashMap<String, Any>().apply {
        put(VERIFY_KEY, dealsVerifyRequest)
    }

    companion object {
        private const val VERIFY_KEY = "eventVerify"
    }
}
