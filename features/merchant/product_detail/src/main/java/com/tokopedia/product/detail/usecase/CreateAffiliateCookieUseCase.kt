package com.tokopedia.product.detail.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.product.detail.data.model.affiliate.AffiliateCookie
import com.tokopedia.product.detail.data.model.affiliate.AffiliateCookieRequest
import javax.inject.Inject

class CreateAffiliateCookieUseCase @Inject constructor(graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<AffiliateCookie>(graphqlRepository) {

    companion object {
        private const val INPUT_KEY = "input"
        const val QUERY = """
            mutation CreateAffiliateCookie(${'$'}input: CreateAffiliateCookieRequest!){
              createAffiliateCookie(input:${'$'}input){
                Data{
                  Status
                  Error{
                    ErrorType
                    Message
                  }
                  AffiliateUUID
                }
              }
            }
        """

        fun createParams(affiliateCookieRequest: AffiliateCookieRequest) = mutableMapOf<String, Any>().apply {
            put(INPUT_KEY, affiliateCookieRequest)
        }
    }

    init {
        setGraphqlQuery(CreateAffiliateCookie())
        setCacheStrategy(GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(AffiliateCookie::class.java)
    }

    @GqlQuery("createAffiliateCookie", QUERY)
    suspend fun executeOnBackground(requestParams: Map<String, Any>): AffiliateCookie {
        setRequestParams(requestParams)
        return executeOnBackground()
    }
}