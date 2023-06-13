package com.tokopedia.tkpd.flashsale.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.tkpd.flashsale.data.mapper.GetFlashSaleForSellerCategoryListMapper
import com.tokopedia.tkpd.flashsale.data.request.CampaignParticipationRequestHeader
import com.tokopedia.tkpd.flashsale.data.request.GetFlashSaleListForSellerCategoryRequest
import com.tokopedia.tkpd.flashsale.data.response.GetFlashSaleForSellerCategoryListResponse
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSaleCategory
import javax.inject.Inject


class GetFlashSaleListForSellerCategoryUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val mapper: GetFlashSaleForSellerCategoryListMapper
) : GraphqlUseCase<List<FlashSaleCategory>>(repository) {

    init {
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    companion object {
        private const val REQUEST_PARAM_KEY = "params"
    }

    private val query = object : GqlQueryInterface {

        private val OPERATION_NAME = "getFlashSaleForSellerCategoryList"
        private val QUERY = """
        query $OPERATION_NAME(${'$'}params: GetFlashSaleForSellerCategoryListRequest!) {
             $OPERATION_NAME(params: ${'$'}params) {
                category_list {
                  category_id
                  category_name
                }
             }
       }

    """.trimIndent()

        override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)
        override fun getQuery(): String = QUERY
        override fun getTopOperationName(): String = OPERATION_NAME
    }

    suspend fun execute(tabName: String): List<FlashSaleCategory> {
        val request = buildRequest(tabName)
        val response = repository.response(listOf(request))
        val data = response.getSuccessData<GetFlashSaleForSellerCategoryListResponse>()
        return mapper.map(data)
    }

    private fun buildRequest(tabName: String): GraphqlRequest {
        val requestHeader = CampaignParticipationRequestHeader(usecase = "campaign_list")
        val payload = GetFlashSaleListForSellerCategoryRequest(requestHeader, tabName)
        val params = mapOf(REQUEST_PARAM_KEY to payload)

        return GraphqlRequest(
            query,
            GetFlashSaleForSellerCategoryListResponse::class.java,
            params
        )
    }

}
