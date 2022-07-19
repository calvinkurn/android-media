package com.tokopedia.topads.common.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.topads.common.data.response.TopAdsAutoAds
import com.tokopedia.topads.common.domain.usecase.TopAdsQueryPostAutoadsUseCaseUseCase.Companion.QUERY
import javax.inject.Inject

@GqlQuery("TopAdsQueryPostAutoadsUseCase", QUERY)
class TopAdsQueryPostAutoadsUseCaseUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository,
) : GraphqlUseCase<TopAdsAutoAds.Response>(graphqlRepository) {
    companion object {
        const val QUERY = """
            mutation(${'$'}input: TopAdsPostAutoAdsParam!){
              topAdsPostAutoAds(input:${'$'}input){
                data {
                 shopID
                 status
                 statusDesc
                 dailyUsage
                 dailyBudget
                 info {
                   reason
                   message
                 }
               }
               errors {
                  code
                  detail
                  object {
                      type
                      text
                  }
                  title
                }
               }
            }
        """
    }

    init {
        setGraphqlQuery(TopAdsQueryPostAutoadsUseCase())
        setCacheStrategy(GraphqlCacheStrategy
            .Builder(CacheType.ALWAYS_CLOUD)
            .build())
        setTypeClass(TopAdsAutoAds.Response::class.java)
    }

    suspend fun executeOnBackground(requestParams: Map<String, Any>): TopAdsAutoAds.Response {
        setRequestParams(requestParams)
        return executeOnBackground()
    }
}