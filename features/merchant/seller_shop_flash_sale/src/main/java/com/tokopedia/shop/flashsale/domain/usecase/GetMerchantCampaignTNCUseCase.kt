package com.tokopedia.shop.flashsale.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop.flashsale.data.mapper.MerchantCampaignTNCMapper
import com.tokopedia.shop.flashsale.data.request.GetMerchantCampaignTNCRequest
import com.tokopedia.shop.flashsale.data.response.GetMerchantCampaignTNCResponse
import com.tokopedia.shop.flashsale.domain.entity.MerchantCampaignTNC
import com.tokopedia.shop.flashsale.domain.entity.enums.PaymentType
import javax.inject.Inject

class GetMerchantCampaignTNCUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val mapper: MerchantCampaignTNCMapper
) : GraphqlUseCase<MerchantCampaignTNC>(repository) {

    companion object {
        private const val ACTION_FROM_SELLER = "SELLER"
        private const val REQUEST_PARAM_KEY = "params"
        private const val QUERY_NAME = "GetMerchantCampaignTNC"
        private const val QUERY = """
            query GetMerchantCampaignTNC(${'$'}params: GetMerchantCampaignTNCRequest!) {
                getMerchantCampaignTNC(params: ${'$'}params) {
                    messages
                    title
                    error {
                       error_code
                       error_message
                    }
                }
            }
        """
    }

    init {
        setupUseCase()
    }

    @GqlQuery(QUERY_NAME, QUERY)
    private fun setupUseCase() {
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    suspend fun execute(
        campaignId: Long,
        isUniqueBuyer: Boolean,
        isCampaignRelation: Boolean,
        paymentType: PaymentType
    ): MerchantCampaignTNC {
        val request = buildRequest(campaignId, isUniqueBuyer, isCampaignRelation, paymentType)
        val response = repository.response(listOf(request))
        val data = response.getSuccessData<GetMerchantCampaignTNCResponse>()
        return mapper.map(data)
    }

    private fun buildRequest(
        campaignId: Long,
        isUniqueBuyer: Boolean,
        isCampaignRelation: Boolean,
        paymentType: PaymentType
    ): GraphqlRequest {
        val payload = GetMerchantCampaignTNCRequest(
            campaignId,
            isUniqueBuyer,
            isCampaignRelation,
            ACTION_FROM_SELLER,
            paymentType.id
        )
        val params = mapOf(REQUEST_PARAM_KEY to payload)
        return GraphqlRequest(
            GetMerchantCampaignTNC(),
            GetMerchantCampaignTNCResponse::class.java,
            params
        )
    }
}