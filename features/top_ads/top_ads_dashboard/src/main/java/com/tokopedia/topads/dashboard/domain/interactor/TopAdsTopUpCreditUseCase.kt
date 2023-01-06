package com.tokopedia.topads.dashboard.domain.interactor

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topads.debit.autotopup.data.model.TopAdsShopTierShopGradeData
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

private const val GET_SHOP_INFO_QUERY =
    """query getShopInfo(${'$'}shopIds: [Int!]!, ${'$'}fields: [String!]!, ${'$'}source: String){
    shopInfoByID(input: {
        shopIDs: ${'$'}shopIds,
        fields: ${'$'}fields,
        source: ${'$'}source
    }){
    result {
      goldOS {
        shopTier
        shopGrade
      }
    }
  }
}"""

@GqlQuery("TopUpGetShopInfoQuery", GET_SHOP_INFO_QUERY)
class TopAdsTopUpCreditUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository,
    private val userSessionInterface: UserSessionInterface
) : GraphqlUseCase<TopAdsShopTierShopGradeData>(graphqlRepository) {
    init {
        setTypeClass(TopAdsShopTierShopGradeData::class.java)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build())
        setGraphqlQuery(TopUpGetShopInfoQuery.GQL_QUERY)
    }


    fun setParams() {
        val params = mutableMapOf(
            "shopIds" to listOf(userSessionInterface.shopId.toIntOrZero()),
            "fields" to listOf("other-goldos"),
            "source" to "confluence testing"
        )
        setRequestParams(params)
    }
}
