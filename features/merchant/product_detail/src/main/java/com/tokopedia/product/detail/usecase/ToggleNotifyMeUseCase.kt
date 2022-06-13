package com.tokopedia.product.detail.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.data.model.upcoming.TeaserNotifyMe
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class ToggleNotifyMeUseCase @Inject constructor(private val graphqlRepository: GraphqlRepository)
    : UseCase<TeaserNotifyMe>() {

    private var requestParams: RequestParams = RequestParams.EMPTY

    companion object {
        val QUERY = """
          mutation checkCampaignNotifyMe(${'$'}campaignId:Int!, ${'$'}product_id_64:Int64, ${'$'}action:Action!, ${'$'}source:String!){
              checkCampaignNotifyMe(params: {
                campaign_id: ${'$'}campaignId,
                product_id_64: ${'$'}product_id_64,
                action: ${'$'}action,
                source: ${'$'}source
              }) {
                  product_id
                  campaign_id
                  success
                  error_message
                  message
                }
            }
        """.trimIndent()

        fun createParams(campaignId: Long, productId: Long, action: String, source: String) =
                RequestParams().apply {
                    putLong(ProductDetailCommonConstant.PARAM_TEASER_CAMPAIGN_ID, campaignId)
                    putLong(ProductDetailCommonConstant.PARAM_TEASER_PRODUCT_ID_64, productId)
                    putString(ProductDetailCommonConstant.PARAM_TEASER_ACTION, action)
                    putString(ProductDetailCommonConstant.PARAM_TEASER_SOURCE, source)
                }
    }

    suspend fun executeOnBackground(requestParams: RequestParams): TeaserNotifyMe {
        this.requestParams = requestParams
        return executeOnBackground()
    }

    override suspend fun executeOnBackground(): TeaserNotifyMe {
        val request = GraphqlRequest(QUERY,
                TeaserNotifyMe::class.java, requestParams.parameters)

        val gqlResponse = graphqlRepository.response(listOf(request))
        val result = gqlResponse.getData<TeaserNotifyMe>(TeaserNotifyMe::class.java)
        val errorResult = gqlResponse.getError(TeaserNotifyMe::class.java)

        if (result == null) {
            throw RuntimeException()
        } else if (errorResult != null && errorResult.isNotEmpty() && errorResult.first().message.isNotEmpty()) {
            throw MessageErrorException(errorResult.first().message)
        } else if (!result.result.isSuccess) {
            if (result.result.errorMessage != "") {
                throw MessageErrorException(result.result.errorMessage)
            } else {
                throw Throwable()
            }
        }

        return result
    }
}