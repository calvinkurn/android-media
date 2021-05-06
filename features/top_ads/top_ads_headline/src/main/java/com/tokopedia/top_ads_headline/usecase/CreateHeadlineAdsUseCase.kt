package com.tokopedia.top_ads_headline.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.top_ads_headline.Constants.INPUT
import com.tokopedia.top_ads_headline.data.TopAdsManageHeadlineInput
import com.tokopedia.top_ads_headline.data.TopadsManageHeadlineAdResponse
import javax.inject.Inject

const val TOP_ADS_CREATE_HEADLINE_ADS_QUERY: String = """mutation topadsManageHeadlineAd(${'$'}input:topadsManageHeadlineAdInput!){
  topadsManageHeadlineAd(input:${'$'}input) {
    data {
      id
      resourceURL
    }
    errors{
      code
      title
      detail
    }
  }
}
"""

@GqlQuery("TopAdsCreateHeadlineAdsQuery", TOP_ADS_CREATE_HEADLINE_ADS_QUERY)
class CreateHeadlineAdsUseCase @Inject constructor(graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<TopadsManageHeadlineAdResponse.Data>(graphqlRepository) {

    init {
        setTypeClass(TopadsManageHeadlineAdResponse.Data::class.java)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build())
        setGraphqlQuery(TopAdsCreateHeadlineAdsQuery.GQL_QUERY)
    }

    fun setParams(input: TopAdsManageHeadlineInput) {
        val queryMap = mutableMapOf(
                INPUT to input
        )
        setRequestParams(queryMap)
    }

}
