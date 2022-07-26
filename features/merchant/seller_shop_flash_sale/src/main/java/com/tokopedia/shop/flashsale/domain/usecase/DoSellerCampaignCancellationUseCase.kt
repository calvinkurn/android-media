package com.tokopedia.shop.flashsale.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.flashsale.data.request.DoSellerCampaignCancellationRequest
import com.tokopedia.shop.flashsale.data.response.DoSellerCampaignCancellationResponse
import javax.inject.Inject

@GqlQuery(
    DoSellerCampaignCancellationUseCase.QUERY_NAME,
    DoSellerCampaignCancellationUseCase.QUERY
)
class DoSellerCampaignCancellationUseCase @Inject constructor(
    private val repository: GraphqlRepository
): GraphqlUseCase<DoSellerCampaignCancellationResponse>(repository) {

    companion object {
        private const val REQUEST_PARAM_KEY = "params"
        const val QUERY_NAME = "DoSellerCampaignCancellationQuery"
        const val QUERY = """
            mutation DoSellerCampaignCancellation(${'$'}params: DoSellerCampaignCancellationRequest!)  {
              doSellerCampaignCancellation(params: ${'$'}params) {
                is_success
                response_header {
                  errorMessage
                }
              }
            }
        """
    }

    init {
        setGraphqlQuery(DoSellerCampaignCancellationQuery())
    }

    suspend fun execute(campaignId: Long, reason: String): Boolean {
        val payload = DoSellerCampaignCancellationRequest(campaignId, reason)
        val request = buildRequest(payload)
        val response = repository.response(listOf(request))
        val data = response.getSuccessData<DoSellerCampaignCancellationResponse>()

        if (!data.doSellerCampaignCancellation.isSuccess) {
            throw MessageErrorException(
                data.doSellerCampaignCancellation.responseHeader.errorMessage.joinToString()
            )
        }

        return data.doSellerCampaignCancellation.isSuccess
    }

    private fun buildRequest(payload: DoSellerCampaignCancellationRequest): GraphqlRequest {
        val params = mapOf(REQUEST_PARAM_KEY to payload)
        return GraphqlRequest(
            DoSellerCampaignCancellationQuery(),
            DoSellerCampaignCancellationResponse::class.java,
            params
        )
    }
}