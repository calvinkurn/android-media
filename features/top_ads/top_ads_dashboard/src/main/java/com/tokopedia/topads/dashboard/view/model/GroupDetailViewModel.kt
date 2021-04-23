package com.tokopedia.topads.dashboard.view.model

import android.content.res.Resources
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.topads.common.data.model.DashGroupListResponse
import com.tokopedia.topads.common.data.model.GroupListDataItem
import com.tokopedia.topads.common.data.response.GroupInfoResponse
import com.tokopedia.topads.common.data.response.nongroupItem.GetDashboardProductStatistics
import com.tokopedia.topads.common.data.response.nongroupItem.NonGroupResponse
import com.tokopedia.topads.common.domain.interactor.TopAdsGetGroupProductDataUseCase
import com.tokopedia.topads.common.domain.interactor.TopAdsGetProductStatisticsUseCase
import com.tokopedia.topads.common.domain.interactor.TopAdsProductActionUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetGroupListUseCase
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsStatisticsType
import com.tokopedia.topads.dashboard.data.model.CountDataItem
import com.tokopedia.topads.dashboard.data.model.DataStatistic
import com.tokopedia.topads.dashboard.data.model.KeywordsResponse
import com.tokopedia.topads.dashboard.data.model.StatsData
import com.tokopedia.topads.dashboard.data.raw.STATS_URL
import com.tokopedia.topads.dashboard.domain.interactor.*
import com.tokopedia.topads.dashboard.view.presenter.StatsList
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Pika on 7/6/20.
 */

class GroupDetailViewModel @Inject constructor(
        @Named("Main")
        private val dispatcher: CoroutineDispatchers,
        private val topAdsGetGroupProductDataUseCase: TopAdsGetGroupProductDataUseCase,
        private val topAdsGetAdKeywordUseCase: TopAdsGetAdKeywordUseCase,
        private val topAdsProductActionUseCase: TopAdsProductActionUseCase,
        private val topAdsGetGroupListUseCase: TopAdsGetGroupListUseCase,
        private val topAdsGetStatisticsUseCase: TopAdsGetStatisticsUseCase,
        private val topAdsKeywordsActionUseCase: TopAdsKeywordsActionUseCase,
        private val topAdsGroupActionUseCase: TopAdsGroupActionUseCase,
        private val topAdsGetProductKeyCountUseCase: TopAdsGetProductKeyCountUseCase,
        private val topAdsGetProductStatisticsUseCase: TopAdsGetProductStatisticsUseCase,
        private val groupInfoUseCase: GroupInfoUseCase,
        private val userSession: UserSessionInterface) : BaseViewModel(dispatcher.main) {

    fun getGroupProductData(page: Int, groupId: Int, search: String, sort: String, status: Int?, startDate: String,
                            endDate: String, onSuccess: (NonGroupResponse.TopadsDashboardGroupProducts) -> Unit, onEmpty: () -> Unit) {
        val requestParams = topAdsGetGroupProductDataUseCase.setParams(groupId, page, search, sort, status, startDate, endDate)
        topAdsGetGroupProductDataUseCase.execute(requestParams, object : Subscriber<Map<Type, RestResponse>>() {
            override fun onCompleted() {}

            override fun onError(e: Throwable?) {
                e?.printStackTrace()
            }

            override fun onNext(typeResponse: Map<Type, RestResponse>) {
                val token = object : TypeToken<DataResponse<NonGroupResponse?>>() {}.type
                val restResponse: RestResponse? = typeResponse[token]
                val response = restResponse?.getData() as DataResponse<NonGroupResponse>
                val nonGroupResponse = response.data.topadsDashboardGroupProducts
                if (nonGroupResponse.data.isEmpty()) {
                    onEmpty()
                } else {
                    onSuccess(nonGroupResponse)
                }
            }
        })
    }

    fun getGroupInfo(resources: Resources, groupId: String, onSuccess: (GroupInfoResponse.TopAdsGetPromoGroup.Data) -> Unit) {
        groupInfoUseCase.setGraphqlQuery(GraphqlHelper.loadRawString(resources,
                com.tokopedia.topads.common.R.raw.query_get_group_info))
        groupInfoUseCase.setParams(groupId)
        groupInfoUseCase.executeQuerySafeMode(
                {
                    onSuccess(it.topAdsGetPromoGroup?.data!!)
                },
                { throwable ->
                    throwable.printStackTrace()
                })
    }

    fun getProductStats(resources: Resources, startDate: String, endDate: String, adIds: List<String>, onSuccess: (GetDashboardProductStatistics) -> Unit, selectedSortId: String, selectedStatusId: Int?) {
        topAdsGetProductStatisticsUseCase.setGraphqlQuery(GraphqlHelper.loadRawString(resources,
                com.tokopedia.topads.common.R.raw.gql_query_product_statistics))
        topAdsGetProductStatisticsUseCase.setParams(startDate, endDate, adIds, selectedSortId, selectedStatusId
                ?: 0)
        topAdsGetProductStatisticsUseCase.executeQuerySafeMode(
                {
                    onSuccess(it.getDashboardProductStatistics)
                },
                {
                    it.printStackTrace()
                })
    }

    fun getGroupKeywordData(resources: Resources, isPositive: Int, group: Int, search: String, sort: String?, status: Int?, page: Int, onSuccess: ((KeywordsResponse.GetTopadsDashboardKeywords) -> Unit), onEmpty: (() -> Unit)) {
        topAdsGetAdKeywordUseCase.setGraphqlQuery(GraphqlHelper.loadRawString(resources,
                R.raw.gql_query_get_keywords_group))
        topAdsGetAdKeywordUseCase.setParams(isPositive, group, search, sort, status, page)
        topAdsGetAdKeywordUseCase.executeQuerySafeMode(
                {
                    if (it.getTopadsDashboardKeywords.data.isEmpty()) {
                        onEmpty()
                    } else
                        onSuccess(it.getTopadsDashboardKeywords)
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

    @GqlQuery("StatsList", STATS_URL)
    fun getTopAdsStatistic(startDate: Date, endDate: Date, @TopAdsStatisticsType selectedStatisticType: Int, onSuccesGetStatisticsInfo: (dataStatistic: DataStatistic) -> Unit, groupId: String) {
        val params = topAdsGetStatisticsUseCase.createRequestParams(startDate, endDate,
                selectedStatisticType, userSession.shopId, groupId)
        topAdsGetStatisticsUseCase.setQueryString(StatsList.GQL_QUERY)
        topAdsGetStatisticsUseCase.execute(params, object : Subscriber<Map<Type, RestResponse>>() {
            override fun onCompleted() {}

            override fun onError(e: Throwable?) {
                Timber.e(e, "P1#TOPADS_DASHBOARD_PRESENTER_GET_STATISTIC#%s", e?.localizedMessage)
            }

            override fun onNext(typeResponse: Map<Type, RestResponse>) {
                val token = object : TypeToken<DataResponse<StatsData?>>() {}.type
                val restResponse: RestResponse? = typeResponse[token]
                val response = restResponse?.getData() as DataResponse<StatsData>
                val dataStatistic = response.data.topadsDashboardStatistics.data
                onSuccesGetStatisticsInfo(dataStatistic)
            }
        })
    }

    fun getGroupList(search: String, onSuccess: (List<GroupListDataItem>) -> Unit) {
        val params = topAdsGetGroupListUseCase.setParamsForKeyWord(search)
        topAdsGetGroupListUseCase.execute(params, object : Subscriber<Map<Type, RestResponse>>() {
            override fun onCompleted() {
            }

            override fun onNext(typeResponse: Map<Type, RestResponse>) {
                val token = object : TypeToken<DataResponse<DashGroupListResponse?>>() {}.type
                val restResponse: RestResponse? = typeResponse[token]
                val response = restResponse?.getData() as DataResponse<DashGroupListResponse>
                val nonGroupResponse = response.data.getTopadsDashboardGroups
                onSuccess(nonGroupResponse.data)
            }

            override fun onError(e: Throwable?) {
                e?.printStackTrace()
            }
        })
    }

    fun setProductAction(onSuccess: (() -> Unit), action: String, adIds: List<String>, resources: Resources, selectedFilter: String?) {
        val params = topAdsProductActionUseCase.setParams(action, adIds, selectedFilter)
        topAdsProductActionUseCase.execute(params, object : Subscriber<Map<Type, RestResponse>>() {
            override fun onCompleted() {}

            override fun onNext(typeResponse: Map<Type, RestResponse>) {
                onSuccess()
            }

            override fun onError(e: Throwable?) {
                e?.printStackTrace()
            }
        })
    }

    fun setGroupAction(action: String, groupIds: List<String>, resources: Resources) {
        topAdsGroupActionUseCase.setQuery(GraphqlHelper.loadRawString(resources,
                R.raw.gql_query_group_action))
        val requestParams = topAdsGroupActionUseCase.setParams(action, groupIds)
        topAdsGroupActionUseCase.execute(requestParams, object : Subscriber<Map<Type, RestResponse>>() {
            override fun onCompleted() {}

            override fun onNext(t: Map<Type, RestResponse>?) {}

            override fun onError(e: Throwable?) {
                e?.printStackTrace()
            }
        })
    }

    fun setKeywordAction(action: String, keywordIds: List<String>, resources: Resources, onSuccess: (() -> Unit)) {
        topAdsKeywordsActionUseCase.setGraphqlQuery(GraphqlHelper.loadRawString(resources,
                R.raw.gql_query_keyword_action))
        topAdsKeywordsActionUseCase.setParams(action, keywordIds)
        topAdsKeywordsActionUseCase.executeQuerySafeMode(
                {
                    onSuccess()
                },
                {
                    it.printStackTrace()
                })
    }

    public override fun onCleared() {
        super.onCleared()
        topAdsGetAdKeywordUseCase.cancelJobs()
        topAdsGetGroupProductDataUseCase.unsubscribe()
        topAdsKeywordsActionUseCase.cancelJobs()
        topAdsProductActionUseCase.unsubscribe()
        topAdsGetGroupListUseCase.unsubscribe()
        topAdsGroupActionUseCase.unsubscribe()
        topAdsGetProductStatisticsUseCase.cancelJobs()
        topAdsGetProductKeyCountUseCase.cancelJobs()
    }
}



