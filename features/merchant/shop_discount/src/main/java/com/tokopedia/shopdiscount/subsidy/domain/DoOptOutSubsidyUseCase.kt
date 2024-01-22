package com.tokopedia.shopdiscount.subsidy.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.shopdiscount.subsidy.model.request.DoOptOutSubsidyRequest
import com.tokopedia.shopdiscount.subsidy.model.response.DoOptOutResponse
import javax.inject.Inject

class DoOptOutSubsidyUseCase @Inject constructor(
    gqlRepository: GraphqlRepository
) : GraphqlUseCase<DoOptOutResponse>(gqlRepository) {

    init {
        setupUseCase()
    }

    @GqlQuery(QUERY_NAME, QUERY)
    private fun setupUseCase() {
        setGraphqlQuery(DoOptOutSubsidyQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(DoOptOutResponse::class.java)
    }

    fun setParams(request: DoOptOutSubsidyRequest) {
        setRequestParams(
            mapOf<String, Any>(
                KEY_PARAMS to request
            )
        )
    }

    companion object {
        private const val KEY_PARAMS = "params"
        private const val QUERY_NAME = "DoOptOutSubsidyQuery"
        private const val QUERY = """
            mutation DoSellerOutProgram(${'$'}params: DoSellerOutProgramRequest!) {
              doSellerOutProgram(params: ${'$'}params) {
                response_header {
                  is_success
                }
              }
            }
        """
    }
}
