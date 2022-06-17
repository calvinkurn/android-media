package com.tokopedia.shop.flashsale.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.flashsale.data.request.DoSellerCampaignProductSubmissionRequest
import com.tokopedia.shop.flashsale.data.response.DoSellerCampaignProductSubmissionResponse
import javax.inject.Inject

@GqlQuery(
    DoSellerCampaignProductSubmissionUseCase.QUERY_NAME,
    DoSellerCampaignProductSubmissionUseCase.QUERY
)
class DoSellerCampaignProductSubmissionUseCase @Inject constructor(
    private val repository: GraphqlRepository
): GraphqlUseCase<DoSellerCampaignProductSubmissionResponse>(repository) {

    companion object {
        private const val CAMPAIGN_TYPE_SHOP_FLASH_SALE = 0
        private const val REQUEST_PARAM_KEY = "params"
        const val QUERY_NAME = "DoSellerCampaignProductSubmissionQuery"
        const val QUERY = """
            mutation doSellerCampaignProductSubmission(${'$'}params: DoSellerCampaignProductSubmissionRequest!) {
              doSellerCampaignProductSubmission(params: ${'$'}params) {
                is_success
                product_success {
                  product_id
                  message
                }
                product_failed {
                  product_id
                  message
                }
                message {
                  error_code
                  error_message
                }
              }
            }
        """
    }

    init {
        setGraphqlQuery(DoSellerCampaignProductSubmissionQuery())
    }

    suspend fun execute(
        campaignId: String,
        productData: List<DoSellerCampaignProductSubmissionRequest.ProductData>
    ): Boolean {
        val payload = DoSellerCampaignProductSubmissionRequest(
            campaignId = campaignId.toLongOrNull().orZero(),
            action = CAMPAIGN_TYPE_SHOP_FLASH_SALE,
            productData = productData
        )
        val request = buildRequest(payload)
        val response = repository.response(listOf(request))
        val data = response.getSuccessData<DoSellerCampaignProductSubmissionResponse>()

        if (!data.data.isSuccess) {
            throw MessageErrorException(
                data.data.message.errorMessage
            )
        }

        return data.data.isSuccess
    }

    private fun buildRequest(payload: DoSellerCampaignProductSubmissionRequest): GraphqlRequest {
        val params = mapOf(REQUEST_PARAM_KEY to payload)
        return GraphqlRequest(
            DoSellerCampaignProductSubmissionQuery(),
            DoSellerCampaignProductSubmissionResponse::class.java,
            params
        )
    }
}