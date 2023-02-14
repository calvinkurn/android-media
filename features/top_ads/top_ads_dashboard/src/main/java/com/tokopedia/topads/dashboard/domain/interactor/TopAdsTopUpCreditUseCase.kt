package com.tokopedia.topads.dashboard.domain.interactor

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.TopAdsCreditTopUpConstant.FIELDS_KEY
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.TopAdsCreditTopUpConstant.OTHER_GOLD_OS_VALUE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.TopAdsCreditTopUpConstant.SHOP_IDS_KEY
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.TopAdsCreditTopUpConstant.SOURCE_KEY
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.TopAdsCreditTopUpConstant.TOP_ADS_DASHBOARD_VALUE
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
        setGraphqlQuery(TopUpGetShopInfoQuery())
    }


    fun setParams() {
        val params = mutableMapOf(
            SHOP_IDS_KEY to listOf(userSessionInterface.shopId.toIntOrZero()),
            FIELDS_KEY to listOf(OTHER_GOLD_OS_VALUE),
            SOURCE_KEY to TOP_ADS_DASHBOARD_VALUE
        )
        setRequestParams(params)
    }
}

