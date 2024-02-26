package com.tokopedia.shopdiscount.subsidy.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.shopdiscount.subsidy.model.request.SubsidyEngineGetSellerOutReasonListRequest
import com.tokopedia.shopdiscount.subsidy.model.response.SubsidyEngineGetSellerOutReasonListResponse
import javax.inject.Inject

class SubsidyEngineGetSellerOutReasonListUseCase @Inject constructor(
    gqlRepository: GraphqlRepository
) : GraphqlUseCase<SubsidyEngineGetSellerOutReasonListResponse>(gqlRepository) {

    init {
        setupUseCase()
    }

    @GqlQuery(QUERY_NAME, QUERY)
    private fun setupUseCase() {
        setGraphqlQuery(SubsidyEngineGetSellerOutReasonListQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(SubsidyEngineGetSellerOutReasonListResponse::class.java)
    }

    fun setParams(request: SubsidyEngineGetSellerOutReasonListRequest) {
        setRequestParams(
            mapOf<String, Any>(
                KEY_PARAMS to request
            )
        )
    }

    companion object {
        private const val KEY_PARAMS = "Params"
        private const val QUERY_NAME = "SubsidyEngineGetSellerOutReasonListQuery"
        private const val QUERY = """
            query SubsidyEngineGetSellerOutReasonList(${'$'}Params: SubsidyEngineGetSellerOutReasonListRequest!) {
              SubsidyEngineGetSellerOutReasonList(Params: ${'$'}Params) {
                ResponseHeader {
                  Success
                  Messages
                  StatusCode
                  ProcessTime
                }
                ReasonOptions{
                  Reason
                }
              }
            }
        """
    }
}
