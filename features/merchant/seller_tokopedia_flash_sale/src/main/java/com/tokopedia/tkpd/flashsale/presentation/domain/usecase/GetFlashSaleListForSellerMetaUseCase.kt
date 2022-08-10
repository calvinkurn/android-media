package com.tokopedia.tkpd.flashsale.presentation.domain.usecase

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.tkpd.flashsale.presentation.data.mapper.GetFlashSaleListForSellerMetaMapper
import com.tokopedia.tkpd.flashsale.presentation.data.query.GetFlashSaleListForSellerMetaQuery
import com.tokopedia.tkpd.flashsale.presentation.data.request.GetFlashSaleListForSellerMetaRequest
import com.tokopedia.tkpd.flashsale.presentation.data.response.GetFlashSaleListForSellerMetaResponse
import com.tokopedia.tkpd.flashsale.presentation.domain.entity.TabMetadata
import javax.inject.Inject


class GetFlashSaleListForSellerMetaUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val mapper: GetFlashSaleListForSellerMetaMapper
) : GraphqlUseCase<List<TabMetadata>>(repository) {


    companion object {
        private const val REQUEST_PARAM_KEY = "params"
    }

    init {
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    suspend fun execute(): List<TabMetadata> {
        val request = buildRequest()
        val response = repository.response(listOf(request))
        val data = response.getSuccessData<GetFlashSaleListForSellerMetaResponse>()
        return mapper.map(data)
    }

    private fun buildRequest(): GraphqlRequest {
        val requestHeader = GetFlashSaleListForSellerMetaRequest.CampaignParticipationRequestHeader(usecase = "campaign_list")
        val payload = GetFlashSaleListForSellerMetaRequest(requestHeader)
        val params = mapOf(REQUEST_PARAM_KEY to payload)

        return GraphqlRequest(
            GetFlashSaleListForSellerMetaQuery,
            GetFlashSaleListForSellerMetaResponse::class.java,
            params
        )
    }
}