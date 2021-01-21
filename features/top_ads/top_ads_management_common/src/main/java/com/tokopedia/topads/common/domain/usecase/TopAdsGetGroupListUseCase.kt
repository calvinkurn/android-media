package com.tokopedia.topads.common.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.internal.ParamObject.GROUP_TYPE
import com.tokopedia.topads.common.data.internal.ParamObject.KEYWORD
import com.tokopedia.topads.common.data.internal.ParamObject.QUERY_INPUT
import com.tokopedia.topads.common.data.internal.ParamObject.SINGLE_ROW
import com.tokopedia.topads.common.data.response.groupitem.GroupItemResponse
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by Pika on 29/5/20.
 */

const val TOP_ADS_GET_GROUP_LIST_QUERY: String = """query GetTopadsDashboardGroups(${'$'}queryInput: GetTopadsDashboardGroupsInputType!) {
  GetTopadsDashboardGroups(queryInput: ${'$'}queryInput) {
    separate_statistic
       meta {
          page {
            per_page
            current
            total
          }
        }
    data {
      group_id
      total_item
      total_keyword
      group_status
      group_status_desc
      group_status_toogle
      group_price_bid
      group_price_daily
      group_price_daily_spent_fmt
      group_price_daily_bar
      group_name
      group_type
      group_end_date
      stat_total_conversion
      stat_total_spent
      stat_total_ctr
      stat_total_sold
      stat_avg_click
      stat_total_income
    }
  }
}
"""

@GqlQuery("GetTopadsGroupListQuery", TOP_ADS_GET_GROUP_LIST_QUERY)
class TopAdsGetGroupListUseCase @Inject constructor(graphqlRepository: GraphqlRepository, val userSession: UserSessionInterface) : GraphqlUseCase<GroupItemResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(GetTopadsGroupListQuery.GQL_QUERY)
    }

    fun setParamsForKeyWord(search: String) {
        val queryMap = HashMap<String, Any?>()
        queryMap[ParamObject.SHOP_id] = userSession.shopId.toIntOrZero()
        queryMap[ParamObject.SEPARATE_STAT] = "true"
        queryMap[KEYWORD] = search
        queryMap[GROUP_TYPE] = 1
        queryMap[SINGLE_ROW] = "1"/*keywords*/
        setRequestParams(mapOf(QUERY_INPUT to queryMap))
    }

    fun setParams(search: String, page: Int, sort: String, status: Int?, startDate: String, endDate: String, groupType: Int) {
        val queryMap = HashMap<String, Any?>()
        queryMap[ParamObject.SHOP_id] = userSession.shopId.toIntOrZero()
        queryMap[ParamObject.SORT] = sort
        queryMap[KEYWORD] = search
        queryMap[ParamObject.PAGE] = page
        queryMap[ParamObject.START_DATE] = startDate
        queryMap[ParamObject.END_DATE] = endDate
        queryMap[ParamObject.STATUS] = status
        queryMap[GROUP_TYPE] = groupType
        setRequestParams(mapOf(QUERY_INPUT to queryMap))
    }

    private val cacheStrategy: GraphqlCacheStrategy = GraphqlCacheStrategy
            .Builder(CacheType.CLOUD_THEN_CACHE).build()

    fun executeQuerySafeMode(onSuccess: (GroupItemResponse) -> Unit, onError: (Throwable) -> Unit) {
        setTypeClass(GroupItemResponse::class.java)
        setCacheStrategy(cacheStrategy)
        execute({
            onSuccess(it)

        }, onError)
    }
}