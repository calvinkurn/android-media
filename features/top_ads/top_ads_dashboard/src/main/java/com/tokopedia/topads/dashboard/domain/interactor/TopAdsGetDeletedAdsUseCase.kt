package com.tokopedia.topads.dashboard.domain.interactor

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.dashboard.data.model.TopAdsDeletedAdsResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

const val TOP_ADS_DELETED_ADS_QUERY =
    """query topadsDeletedAds(${'$'}queryInput: topadsDeletedAdsInputType!) {
  topadsDeletedAds(queryInput: ${'$'}queryInput) {
    data {
      stat_total_spent
      stat_total_gross_profit
      stat_total_roas
      stat_avg_click
      stat_total_impression
      stat_total_click
      stat_total_ctr
      stat_total_conversion
      stat_total_sold
      stat_total_top_slot_impression
      ad_deleted_time
      ad_title
      product_image_uri
    }
    errors {
      code
      detail
      title
    }
    page {
      per_page
      total
    }
  }
}
"""

@GqlQuery("DeletedAdsQuery", TOP_ADS_DELETED_ADS_QUERY)
class TopAdsGetDeletedAdsUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository,
    private val userSession: UserSessionInterface
) : GraphqlUseCase<TopAdsDeletedAdsResponse>(graphqlRepository), CoroutineScope {

    private val job: Job by lazy { SupervisorJob() }

    init {
        setTypeClass(TopAdsDeletedAdsResponse::class.java)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build())
        setGraphqlQuery(DeletedAdsQuery.GQL_QUERY)
    }

    fun setParams(page: Int, type: String, startDate: String, endDate: String, goalId: Int = 0) {
        val requestParams = RequestParams.create()
        val queryMap = HashMap<String, Any?>()
        queryMap[ParamObject.SHOP_id] = userSession.shopId
        queryMap[ParamObject.PAGE] = page
        queryMap[ParamObject.START_DATE] = startDate
        queryMap[ParamObject.END_DATE] = endDate
        queryMap[ParamObject.TYPE] = type
        queryMap[ParamObject.GOAL_ID] = goalId
        requestParams.putAll(mapOf(ParamObject.QUERY_INPUT to queryMap))
        setRequestParams(requestParams.parameters)
    }

    fun execute(onSuccess: (TopAdsDeletedAdsResponse) -> Unit, onEmptyResult: () -> Unit) {
        launchCatchError(
            block = {
                val res = executeOnBackground()
                if (res.topAdsDeletedAds.topAdsDeletedAdsItemList.isNullOrEmpty()) onEmptyResult.invoke()
                else onSuccess.invoke(res)
            },
            onError = {
                it.printStackTrace()
            }
        )
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    fun cancelJob() {
        job.cancel()
    }
}