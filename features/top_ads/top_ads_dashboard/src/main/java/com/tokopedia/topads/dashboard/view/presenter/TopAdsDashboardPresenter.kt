package com.tokopedia.topads.dashboard.view.presenter

import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.topads.common.data.exception.ResponseErrorException
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.response.groupitem.GetTopadsDashboardGroupStatistics
import com.tokopedia.topads.common.data.response.groupitem.GroupItemResponse
import com.tokopedia.topads.common.data.response.groupitem.GroupStatisticsResponse
import com.tokopedia.topads.common.data.response.nongroupItem.GetDashboardProductStatistics
import com.tokopedia.topads.common.data.response.nongroupItem.NonGroupResponse
import com.tokopedia.topads.common.domain.interactor.TopAdsGetGroupDataUseCase
import com.tokopedia.topads.common.domain.interactor.TopAdsGetProductStatisticsUseCase
import com.tokopedia.topads.common.domain.interactor.TopAdsProductActionUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetGroupProductDataUseCase
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsStatisticsType
import com.tokopedia.topads.dashboard.data.model.*
import com.tokopedia.topads.dashboard.data.model.insightkey.InsightKeyData
import com.tokopedia.topads.dashboard.domain.interactor.*
import com.tokopedia.topads.dashboard.view.listener.TopAdsDashboardView
import com.tokopedia.topads.debit.autotopup.data.model.AutoTopUpData
import com.tokopedia.topads.debit.autotopup.data.model.AutoTopUpStatus
import com.tokopedia.topads.headline.data.ShopAdInfo
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber
import timber.log.Timber
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Created by hadi.putra on 23/04/18.
 */

class TopAdsDashboardPresenter @Inject
constructor(private val topAdsGetShopDepositUseCase: com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<Deposit>,
            private val shopAdInfoUseCase: com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<ShopAdInfo>,
            private val gqlGetShopInfoUseCase: GQLGetShopInfoUseCase,
            private val topAdsGetGroupDataUseCase: TopAdsGetGroupDataUseCase,
            private val topAdsGetGroupStatisticsUseCase: TopAdsGetGroupStatisticsUseCase,
            private val topAdsGetProductStatisticsUseCase: TopAdsGetProductStatisticsUseCase,
            private val topAdsGetProductKeyCountUseCase: TopAdsGetProductKeyCountUseCase,
            private val topAdsGetGroupListUseCase: TopAdsGetGroupListUseCase,
            private val topAdsGroupActionUseCase: TopAdsGroupActionUseCase,
            private val topAdsProductActionUseCase: TopAdsProductActionUseCase,
            private val topAdsGetGroupProductDataUseCase: TopAdsGetGroupProductDataUseCase,
            private val topAdsInsightUseCase: TopAdsInsightUseCase,
            private val getStatisticUseCase: GetStatisticUseCase,
            private val userSession: UserSessionInterface) : BaseDaggerPresenter<TopAdsDashboardView>() {

    var isShopWhiteListed: MutableLiveData<Boolean> = MutableLiveData()
    var expiryDateHiddenTrial: MutableLiveData<String> = MutableLiveData()

    companion object {
        const val HIDDEN_TRIAL_FEATURE = 21
        private const val SELECTION_TYPE_DEF = 0
        private const val SELECTION_IND_DEF = 2

        const val CAL_YYYY_MM_DD = "yyyy-MM-dd"
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        const val START_DATE = "startDate"
        const val END_DATE = "endDate"
        const val STATS_URL = """query topadsDashboardStatistics (${'$'}startDate: String!, ${'$'}endDate: String!,${'$'}shopID: Int!,${'$'}type:Int,${'$'}group:String){
    topadsDashboardStatistics(startDate:${'$'}startDate,endDate:${'$'}endDate,shopID:${'$'}shopID,type:${'$'}type,group:${'$'}group){
    data{
      summary {
        ads_impression_sum
        ads_click_sum
        ads_ctr_percentage
        ads_cost_avg
        ads_conversion_sum
        ads_all_sold_sum
        ads_cost_sum
        ads_all_gross_profit
        ads_follow_count
        ads_impression_sum_fmt
        ads_click_sum_fmt
        ads_ctr_percentage_fmt
        ads_cost_avg_fmt
        ads_conversion_sum_fmt
        ads_all_sold_sum_fmt
        ads_cost_sum_fmt
        ads_all_gross_profit_fmt
        ads_follow_count_fmt
        cost_sum
        all_gross_profit
        cost_sum_fmt
        all_gross_profit_fmt

      }
      cells {
        day
        month
        year
        ads_impression_sum
        ads_click_sum
        ads_ctr_percentage
        ads_cost_avg
        ads_conversion_sum
        ads_all_sold_sum
        ads_cost_sum
        ads_all_gross_profit
        ads_impression_sum_fmt
        ads_click_sum_fmt
        ads_ctr_percentage_fmt
        ads_cost_avg_fmt
        ads_conversion_sum_fmt
        ads_all_sold_sum_fmt
        ads_cost_sum_fmt
        ads_all_gross_profit_fmt
        cost_sum
        all_gross_profit
        cost_sum_fmt
        all_gross_profit_fmt
        aff_impression_sum
        aff_impression_sum_fmt
      }
    }
  }
}"""
        const val DEPOSIT = """query topadsDashboardDeposits(${'$'}shop_id: Int!) {
  topadsDashboardDeposits(shop_id: ${'$'}shop_id) {
    data {
      amount
      amount_fmt
    }
  }
}
"""
        const val SHOP_AD_INFO = """
            query topadsGetShopInfoV2(${'$'}shopID: Int!) {
  topadsGetShopInfoV2(shopID: ${'$'}shopID) {
    data {
      ads {
        type
        is_used
      }
    }
    errors {
      code
      detail
      title
    }
  }
}"""
    }

    @GqlQuery("DepositQuery", DEPOSIT)
    fun getShopDeposit(onSuccess: ((dataDeposit: DepositAmount) -> Unit)) {
        val params = mapOf(ParamObject.SHOP_id to userSession.shopId.toIntOrZero(),
                ParamObject.SOURCE to TopAdsDashboardConstant.SOURCE_DASH)
        topAdsGetShopDepositUseCase.setTypeClass(Deposit::class.java)
        topAdsGetShopDepositUseCase.setRequestParams(params)
        topAdsGetShopDepositUseCase.setGraphqlQuery(DepositQuery.GQL_QUERY)
        topAdsGetShopDepositUseCase.execute({
            onSuccess(it.topadsDashboardDeposits.data)
        }
                , {
            it.printStackTrace()
        })
    }


    fun getGroupData(resources: Resources, page: Int, search: String, sort: String, status: Int?,
                     startDate: String, endDate: String, groupType: Int, onSuccess: ((GroupItemResponse.GetTopadsDashboardGroups) -> Unit)) {
        val requestParams = topAdsGetGroupDataUseCase.setParams(search, page, sort, status, startDate, endDate, groupType)
        topAdsGetGroupDataUseCase.execute(requestParams, object : Subscriber<Map<Type, RestResponse>>() {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                e?.printStackTrace()
            }

            override fun onNext(typeResponse: Map<Type, RestResponse>) {
                val token = object : TypeToken<DataResponse<GroupItemResponse?>>() {}.type
                val restResponse: RestResponse? = typeResponse[token]
                val response = restResponse?.getData() as DataResponse<GroupItemResponse>
                onSuccess(response.data.getTopadsDashboardGroups)
            }
        })
    }

    @GqlQuery("StatsList", STATS_URL)
    fun getStatistic(startDate: Date, endDate: Date, @TopAdsStatisticsType selectedStatisticType: Int, adType: String, onSuccesGetStatisticsInfo: ((dataStatistic: DataStatistic) -> Unit)) {
        val requestParams = getStatisticUseCase.createRequestParams(startDate, endDate, selectedStatisticType, userSession.shopId, adType)
        getStatisticUseCase.setQueryString(StatsList.GQL_QUERY)
        getStatisticUseCase.execute(requestParams, object : Subscriber<Map<Type, RestResponse>>() {
            override fun onCompleted() {
            }

            override fun onNext(typeResponse: Map<Type, RestResponse>) {
                val token = object : TypeToken<DataResponse<StatsData?>>() {}.type
                val restResponse: RestResponse? = typeResponse[token]
                val response = restResponse?.getData() as DataResponse<StatsData>
                val dataStatistic = response.data.topadsDashboardStatistics.data
                onSuccesGetStatisticsInfo(dataStatistic)
            }

            override fun onError(e: Throwable) {
                view?.onErrorGetStatisticsInfo(e)
            }
        })
    }

    fun getGroupStatisticsData(resources: Resources, page: Int, search: String, sort: String, status: Int?,
                               startDate: String, endDate: String, groupIds: List<String>, onSuccess: ((GetTopadsDashboardGroupStatistics) -> Unit)) {
        val params = topAdsGetGroupStatisticsUseCase.setParams(search, page, sort, status, startDate, endDate, groupIds)
        topAdsGetGroupStatisticsUseCase.execute(params, object : Subscriber<Map<Type, RestResponse>>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                e?.printStackTrace()
            }

            override fun onNext(typeResponse: Map<Type, RestResponse>) {
                val token = object : TypeToken<DataResponse<GroupStatisticsResponse?>>() {}.type
                val restResponse: RestResponse? = typeResponse[token]
                val response = restResponse?.getData() as DataResponse<GroupStatisticsResponse>
                response?.data?.getTopadsDashboardGroupStatistics?.let { onSuccess(it) }
            }
        })
    }

    fun getProductStats(resources: Resources, startDate: String, endDate: String, adIds: List<String>, onSuccess: ((GetDashboardProductStatistics) -> Unit)) {
        topAdsGetProductStatisticsUseCase.setGraphqlQuery(GraphqlHelper.loadRawString(resources,
                com.tokopedia.topads.common.R.raw.gql_query_product_statistics))
        topAdsGetProductStatisticsUseCase.setParams(startDate, endDate, adIds)
        topAdsGetProductStatisticsUseCase.executeQuerySafeMode(
                {
                    onSuccess(it.getDashboardProductStatistics)
                },
                {
                    it.printStackTrace()
                })
    }

    fun getCountProductKeyword(resources: Resources, groupIds: List<String>, onSuccess: ((List<CountDataItem>) -> Unit)) {
        topAdsGetProductKeyCountUseCase.setGraphqlQuery(GraphqlHelper.loadRawString(resources,
                R.raw.gql_query_total_products_keywords))
        topAdsGetProductKeyCountUseCase.setParams(groupIds)
        topAdsGetProductKeyCountUseCase.executeQuerySafeMode(
                {
                    onSuccess(it.topAdsGetTotalAdsAndKeywords.data)
                },
                {
                    it.printStackTrace()
                })
    }

    fun getGroupList(resources: Resources, search: String, onSuccess: ((List<GroupListDataItem>) -> Unit)) {
        topAdsGetGroupListUseCase.setGraphqlQuery(GraphqlHelper.loadRawString(resources,
                R.raw.query_get_group_list))
        topAdsGetGroupListUseCase.setParams(search)
        topAdsGetGroupListUseCase.executeQuerySafeMode(
                {
                    onSuccess(it.getTopadsDashboardGroups.data)
                },
                {
                    it.printStackTrace()

                })
    }

    fun setGroupAction(onSuccess: ((action: String) -> Unit), action: String, groupIds: List<String>, resources: Resources) {
        topAdsGroupActionUseCase.setQuery(GraphqlHelper.loadRawString(resources,
                R.raw.gql_query_group_action))
        val requestParams = topAdsGroupActionUseCase.setParams(action, groupIds)
        topAdsGroupActionUseCase.execute(requestParams, object : Subscriber<Map<Type, RestResponse>>() {
            override fun onCompleted() {
            }

            override fun onNext(typeResponse: Map<Type, RestResponse>) {
                val token = object : TypeToken<DataResponse<GroupActionResponse?>>() {}.type
                val restResponse: RestResponse? = typeResponse[token]
                val response = restResponse?.getData() as DataResponse<GroupActionResponse>
                response.data.topAdsEditGroupBulk?.data?.action?.let { onSuccess(it) }
            }

            override fun onError(e: Throwable) {
                view?.onErrorGetStatisticsInfo(e)
            }
        })
    }

    fun setProductAction(onSuccess: ((action: String) -> Unit), action: String, adIds: List<String>, resources: Resources, selectedFilter: String?) {
        topAdsProductActionUseCase.setGraphqlQuery(GraphqlHelper.loadRawString(resources,
                com.tokopedia.topads.common.R.raw.gql_query_product_action))
        topAdsProductActionUseCase.setParams(action, adIds, selectedFilter)
        topAdsProductActionUseCase.executeQuerySafeMode(
                {
                    onSuccess(action)
                },
                {
                    it.printStackTrace()

                })
    }


    fun getGroupProductData(page: Int, groupId: Int?, search: String,
                            sort: String, status: Int?, startDate: String, endDate: String, onSuccess: (NonGroupResponse.TopadsDashboardGroupProducts) -> Unit, onEmpty: () -> Unit) {
        topAdsGetGroupProductDataUseCase.setParams(groupId, page, search, sort, status, startDate, endDate, userSession.shopId.toIntOrZero())
        topAdsGetGroupProductDataUseCase.executeQuerySafeMode(
                {
                    if (it.data.isEmpty()) {
                        onEmpty()
                    } else {
                        onSuccess(it)
                    }
                },
                {
                    it.printStackTrace()
                }
        )
    }

    fun getInsight(resources: Resources, onSuccess: ((InsightKeyData) -> Unit)) {
        topAdsInsightUseCase.setQuery(GraphqlHelper.loadRawString(resources, R.raw.gql_query_insights_keyword))
        val requestParams = topAdsInsightUseCase.setParams()
        topAdsInsightUseCase.execute(requestParams, object : Subscriber<Map<Type, RestResponse>>() {
            override fun onCompleted() {

            }


            override fun onError(e: Throwable?) {

            }

            override fun onNext(typeResponse: Map<Type, RestResponse>) {
                val token = object : TypeToken<DataResponse<JsonObject?>>() {}.type
                val restResponse: RestResponse? = typeResponse[token]
                val response = restResponse?.getData() as DataResponse<JsonObject>
                val responseData = response.data.getAsJsonObject("topAdsGetKeywordInsights").getAsJsonPrimitive(TopAdsDashboardConstant.DATA)
                val type = object : TypeToken<InsightKeyData>() {}.type
                val data: InsightKeyData = Gson().fromJson(responseData.asString, type)
                onSuccess(data)
            }
        })
    }

    @GqlQuery("ShopInfo", TopAdsDashboardPresenter.SHOP_AD_INFO)
    fun getShopAdsInfo(onSuccess: ((ShopAdInfo)) -> Unit) {
        val params = mapOf(ParamObject.SHOP_ID to userSession.shopId.toIntOrZero())
        shopAdInfoUseCase.setTypeClass(ShopAdInfo::class.java)
        shopAdInfoUseCase.setRequestParams(params)
        shopAdInfoUseCase.setGraphqlQuery(ShopInfo.GQL_QUERY)
        shopAdInfoUseCase.execute({
            onSuccess(it)
        }, {
            Timber.e(it, "P1#TOPADS_DASHBOARD_PRESENTER_GET_STATISTIC#%s", it.localizedMessage)
        }
        )
    }


    fun getAutoAdsStatus(resources: Resources) {
        val graphqlUseCase = GraphqlUseCase()
        val shopId: Int = userSession.shopId.toIntOrZero()
        val variables = mapOf<String, Any>(TopAdsDashboardConstant.SHOP_ID to shopId)
        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(resources,
                com.tokopedia.topads.common.R.raw.query_auto_ads_status), AutoAdsResponse::class.java, variables, false)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.execute(object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {}

            override fun onError(e: Throwable) {
            }

            override fun onNext(graphqlResponse: GraphqlResponse) {
                val adsInfo = graphqlResponse.getData<AutoAdsResponse>(AutoAdsResponse::class.java)
                view?.onSuccessAdsInfo(adsInfo.topAdsGetAutoAds.data)
            }
        })
    }


    fun getExpiryDate(resources: Resources) {

        val graphqlUseCase = GraphqlUseCase()
        val shopId = userSession.shopId
        val variables = mapOf<String, Any>("shopID" to shopId)
        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(resources,
                R.raw.query_hidden_trial_expiry_date), ExpiryDateResponse::class.java, variables, false)
        graphqlUseCase.clearRequest()
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE)
                .setExpiryTime(GraphqlConstant.ExpiryTimes.WEEK.`val`())
                .setSessionIncluded(true)
                .build())
        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.execute(object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {}

            override fun onError(e: Throwable) {
            }

            override fun onNext(graphqlResponse: GraphqlResponse) {
                val dateInfo = graphqlResponse.getData<ExpiryDateResponse>(ExpiryDateResponse::class.java)
                expiryDateHiddenTrial.postValue(dateInfo.topAdsGetFreeDeposit.expiryDate)
            }
        })

    }

    fun getShopListHiddenTrial(resources: Resources) {

        val graphqlUseCase = GraphqlUseCase()
        val shopId = userSession.shopId
        val variables = mapOf<String, Any>("shopID" to shopId)
        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(resources,
                R.raw.query_hidden_trial_shoplist), FreeTrialShopListResponse::class.java, variables, false)
        graphqlUseCase.clearRequest()
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CACHE_ONLY)
                .setExpiryTime(GraphqlConstant.ExpiryTimes.WEEK.`val`())
                .setSessionIncluded(true)
                .build())

        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.execute(object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {}

            override fun onError(e: Throwable) {
                isShopWhiteListed.postValue(false)
            }

            override fun onNext(graphqlResponse: GraphqlResponse) {

                val shopList = graphqlResponse.getData<FreeTrialShopListResponse>(FreeTrialShopListResponse::class.java)
                var data = false
                shopList.topAdsGetShopWhitelistedFeature.data.forEach lit@{
                    if (it?.featureID == HIDDEN_TRIAL_FEATURE) {
                        data = true
                        return@lit
                    }
                }
                isShopWhiteListed.postValue(data)
            }
        })
    }

    fun getAdsStatus(rawQuery: String) {
        val graphqlUseCase = GraphqlUseCase()
        val shop_id: String = userSession.shopId
        val variables = mapOf<String, Any>("shopId" to shop_id.toInt())
        val graphqlRequest = GraphqlRequest(rawQuery, AdStatusResponse::class.java, variables, false)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.execute(object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {}
            override fun onError(e: Throwable) {
                Timber.e(e, "P1#TOPADS_DASHBOARD_PRESENTER_AUTO_TOPADS_STATUS#%s", e.localizedMessage)
            }

            override fun onNext(graphqlResponse: GraphqlResponse) {
                val data = graphqlResponse.getSuccessData<AdStatusResponse>()
                view?.onSuccessAdStatus(data.topAdsGetShopInfo.data)
            }
        })

    }

    fun getAutoTopUpStatus(rawQuery: String, onSuccess: ((data: AutoTopUpStatus) -> Unit)) {
        val graphqlUseCase = GraphqlUseCase()
        val shopId: String = userSession.shopId
        val variables = mapOf<String, Any>(TopAdsDashboardConstant.SHOP_ID to shopId)
        val graphqlRequest = GraphqlRequest(rawQuery, AutoTopUpData.Response::class.java, variables, false)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.execute(object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {}
            override fun onError(e: Throwable) {
                Timber.e(e, "P1#TOPADS_DASHBOARD_PRESENTER_AUTO_TOPADS_STATUS#%s", e.localizedMessage)
            }

            override fun onNext(graphqlResponse: GraphqlResponse) {
                val data = graphqlResponse.getSuccessData<AutoTopUpData.Response>()

                when {
                    data.response == null -> {
                        Exception("Tidak ada data").printStackTrace()
                    }
                    data.response.errors.isEmpty() -> {
                        onSuccess(data.response.data)
                    }
                    else -> {
                        ResponseErrorException(data.response.errors).printStackTrace()
                    }
                }
            }
        })
    }

    override fun detachView() {
        super.detachView()
        topAdsGetShopDepositUseCase.cancelJobs()
        gqlGetShopInfoUseCase.cancelJobs()
        topAdsGetGroupDataUseCase.unsubscribe()
        topAdsGetGroupProductDataUseCase.cancelJobs()
        topAdsGetGroupDataUseCase.unsubscribe()
        topAdsGetGroupStatisticsUseCase.unsubscribe()
        topAdsGetProductKeyCountUseCase.cancelJobs()
        topAdsGetProductStatisticsUseCase.cancelJobs()
        topAdsGroupActionUseCase.unsubscribe()
        topAdsInsightUseCase.unsubscribe()
    }
}