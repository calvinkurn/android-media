package com.tokopedia.topads.common.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.response.nongroupItem.NonGroupResponse
import javax.inject.Inject

const val TOP_ADS_DASHBOARD_GROUP_PRODUCTS_QUERY: String = """
               query topadsDashboardGroupProducts(${'$'}queryInput: topadsDashboardGroupProductsInputType!) {
  topadsDashboardGroupProducts(queryInput: ${'$'}queryInput) {
    separate_statistic
       meta {
              page {
                per_page
                current
                total
              }
            }
    data {
      ad_id
      item_id
      ad_status
      ad_status_desc
      ad_price_bid
      ad_price_bid_fmt
      ad_price_daily
      ad_price_daily_fmt
      stat_total_gross_profit
      ad_price_daily_spent_fmt
      ad_price_daily_bar
      product_name
      product_image_uri
      group_id
      group_name
    }
  }
}
"""


@GqlQuery("TopadsDashboardGroupProductsQuery", TOP_ADS_DASHBOARD_GROUP_PRODUCTS_QUERY)
class TopAdsGetGroupProductDataUseCase @Inject constructor(graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<NonGroupResponse>(graphqlRepository) {

    fun setParams(groupId: Int?, page: Int, search: String, sort: String, status: Int?, startDate: String,
                  endDate: String, shopId: Int, type: String = "") {
        val queryMap = HashMap<String, Any?>()
        queryMap[ParamObject.SHOP_id] = shopId
        queryMap[ParamObject.GROUP] = groupId
        queryMap[ParamObject.SORT] = sort
        queryMap[ParamObject.PAGE] = page
        queryMap[ParamObject.START_DATE] = startDate
        queryMap[ParamObject.END_DATE] = endDate
        queryMap[ParamObject.KEYWORD] = search
        queryMap[ParamObject.STATUS] = status
        queryMap[ParamObject.TYPE] = type
        setRequestParams(mapOf(ParamObject.QUERY_INPUT to queryMap))
    }

    private fun getQuery(): String {
        return TopadsDashboardGroupProductsQuery.GQL_QUERY
    }

    private val cacheStrategy: GraphqlCacheStrategy = GraphqlCacheStrategy
            .Builder(CacheType.CLOUD_THEN_CACHE).build()

    fun executeQuerySafeMode(onSuccess: (NonGroupResponse.TopadsDashboardGroupProducts) -> Unit, onError: (Throwable) -> Unit) {
        setTypeClass(NonGroupResponse::class.java)
        setGraphqlQuery(getQuery())
        setCacheStrategy(cacheStrategy)
        execute({
            onSuccess(it.topadsDashboardGroupProducts)

        }, onError)
    }
}