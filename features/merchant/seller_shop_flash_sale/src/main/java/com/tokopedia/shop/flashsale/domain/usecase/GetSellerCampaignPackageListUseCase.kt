package com.tokopedia.shop.flashsale.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop.flashsale.data.mapper.GetSellerCampaignPackageListMapper
import com.tokopedia.shop.flashsale.data.request.GetSellerCampaignPackageListRequest
import com.tokopedia.shop.flashsale.data.request.RequestHeader
import com.tokopedia.shop.flashsale.data.response.GetSellerCampaignPackageListResponse
import com.tokopedia.shop.flashsale.domain.entity.VpsPackage
import javax.inject.Inject


class GetSellerCampaignPackageListUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val mapper: GetSellerCampaignPackageListMapper
): GraphqlUseCase<GetSellerCampaignPackageListResponse>(repository) {

    companion object {
        private const val REQUEST_PARAM_KEY = "params"
        private const val QUERY_NAME = "GetSellerCampaignPackageList"
        private const val QUERY = """
            query GetSellerCampaignPackageList(${'$'}params: getSellerCampaignPackageListRequest!)  {
              getSellerCampaignPackageList(params: ${'$'}params) {
                response_header {
                  status
                  success
                  processTime
                }
                package_list {
                  package_id
                  package_name
                  original_quota
                  current_quota
                  remaining_quota
                  package_start_time
                  package_end_time
                  is_disabled
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

    suspend fun execute(): List<VpsPackage> {
        val payload = GetSellerCampaignPackageListRequest(
            RequestHeader(),
            thematicId = "0",
            thematicSubId = "0"
        )
        val request = buildRequest(payload)
        val response = repository.response(listOf(request))
        val data = response.getSuccessData<GetSellerCampaignPackageListResponse>()


        return mapper.map(data)
    }

    private fun buildRequest(payload: GetSellerCampaignPackageListRequest): GraphqlRequest {
        val params = mapOf(REQUEST_PARAM_KEY to payload)
        return GraphqlRequest(
            GetSellerCampaignPackageList(),
            GetSellerCampaignPackageListResponse::class.java,
            params
        )
    }
}