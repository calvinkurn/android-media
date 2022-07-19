package com.tokopedia.topads.common.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.topads.common.data.model.AutoAdsParam
import com.tokopedia.topads.common.data.response.TopAdsAutoAds
import com.tokopedia.topads.common.domain.usecase.TopAdsQueryPostAutoadsUseCase.Companion.QUERY
import javax.inject.Inject

@GqlQuery("TopAdsQueryPostAutoads", QUERY)
class TopAdsQueryPostAutoadsUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository,
) : GraphqlUseCase<TopAdsAutoAds.Response>(graphqlRepository) {

    companion object {
        const val QUERY = """
            mutation topAdsPostAutoAdsV2(${'$'}input: TopAdsPostAutoAdsParamV2!) {
              topAdsPostAutoAdsV2(input: ${'$'}input) {
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
        setTypeClass(TopAdsAutoAds.Response::class.java)
        setCacheStrategy(
            GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD)
                .build()
        )
        setGraphqlQuery(TopAdsQueryPostAutoads())
    }

    fun setParam(input: AutoAdsParam) : TopAdsQueryPostAutoadsUseCase {
//        val param = mapOf(
//            "shopID" to "480967",
//            "action" to "toggle_off",
//            "dailyBudget" to 1000,
//            "source" to "one_click_promo",
//            "channel" to "topchat"
//        )
        val param = mapOf("input" to input.input)
        setRequestParams(param)
        return this
    }

}