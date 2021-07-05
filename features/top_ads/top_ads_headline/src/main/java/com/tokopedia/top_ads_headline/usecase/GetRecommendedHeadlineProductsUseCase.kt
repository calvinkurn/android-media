package com.tokopedia.top_ads_headline.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.top_ads_headline.data.GetRecommendedHeadlineProductsData
import com.tokopedia.topads.common.data.internal.ParamObject
import javax.inject.Inject

const val TOP_ADS_GET_RECOMMENDED_HEADLINE_PRODUCTS_QUERY: String = """query topadsGetRecommendedHeadlineProducts(${'$'}shopID:String!){
  topadsGetRecommendedHeadlineProducts(shopID:${'$'}shopID) {
    data {
      products{
        id
        name
        price
        priceFmt
        imageURL
        rating
        reviewCount
        category {
          id
          name
        }
      }
    }
    errors {
      code
      title
      detail
    }
  }
}
"""

@GqlQuery("GetRecommendedHeadlineProductsQuery", TOP_ADS_GET_RECOMMENDED_HEADLINE_PRODUCTS_QUERY)
class GetRecommendedHeadlineProductsUseCase @Inject constructor(graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<GetRecommendedHeadlineProductsData>(graphqlRepository) {

    init {
        setTypeClass(GetRecommendedHeadlineProductsData::class.java)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build())
        setGraphqlQuery(GetRecommendedHeadlineProductsQuery.GQL_QUERY)
    }

    fun setParams(shopId:String) {
        val queryMap = mutableMapOf(
                ParamObject.SHOP_ID to shopId
        )
        setRequestParams(queryMap)
    }

}