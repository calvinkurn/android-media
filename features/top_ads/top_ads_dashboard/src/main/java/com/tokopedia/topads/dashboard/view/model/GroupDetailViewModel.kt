package com.tokopedia.topads.dashboard.view.model

import android.content.res.Resources
import android.os.Bundle
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.model.DashGroupListResponse
import com.tokopedia.topads.common.data.model.DataSuggestions
import com.tokopedia.topads.common.data.model.GroupListDataItem
import com.tokopedia.topads.common.data.response.FinalAdResponse
import com.tokopedia.topads.common.data.response.GroupInfoResponse
import com.tokopedia.topads.common.data.response.HeadlineInfoResponse
import com.tokopedia.topads.common.data.response.TopadsBidInfo
import com.tokopedia.topads.common.data.response.nongroupItem.GetDashboardProductStatistics
import com.tokopedia.topads.common.data.response.nongroupItem.NonGroupResponse
import com.tokopedia.topads.common.domain.interactor.BidInfoUseCase
import com.tokopedia.topads.common.domain.interactor.TopAdsGetGroupProductDataUseCase
import com.tokopedia.topads.common.domain.interactor.TopAdsGetProductStatisticsUseCase
import com.tokopedia.topads.common.domain.interactor.TopAdsProductActionUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsCreateUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetGroupListUseCase
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsStatisticsType
import com.tokopedia.topads.dashboard.data.model.CountDataItem
import com.tokopedia.topads.dashboard.data.model.DataStatistic
import com.tokopedia.topads.dashboard.data.model.KeywordsResponse
import com.tokopedia.topads.dashboard.domain.interactor.*
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber
import java.lang.reflect.Type
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

/**
 * Created by Pika on 7/6/20.
 */

class GroupDetailViewModel @Inject constructor(
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
    private val getHeadlineInfoUseCase: GetHeadlineInfoUseCase,
    private val bidInfoUseCase: BidInfoUseCase,
    private val topAdsCreateUseCase: TopAdsCreateUseCase,
    private val userSession: UserSessionInterface) : BaseViewModel(dispatcher.main) {

    fun getGroupProductData(
        page: Int, groupId: Int, search: String,
        sort: String, status: Int?, startDate: String, endDate: String, goalId: Int,
        onSuccess: (NonGroupResponse.TopadsDashboardGroupProducts) -> Unit, onEmpty: () -> Unit,
    ) {
        launchCatchError(block = {
            val param = topAdsGetGroupProductDataUseCase.setParams(
                groupId, page, search, sort, status, startDate, endDate, goalId = goalId)

            val nonGroupResponse =
                topAdsGetGroupProductDataUseCase.execute(param).topadsDashboardGroupProducts

            if (nonGroupResponse.data.isEmpty()) {
                onEmpty.invoke()
            } else {
                onSuccess.invoke(nonGroupResponse)
            }
        }, onError = {
            onEmpty.invoke()
        })
    }

    fun getGroupInfo(resources: Resources, groupId: String, source: String, onSuccess: (GroupInfoResponse.TopAdsGetPromoGroup.Data) -> Unit) {
        groupInfoUseCase.setGraphqlQuery(GraphqlHelper.loadRawString(resources,
                com.tokopedia.topads.common.R.raw.query_get_group_info))
        groupInfoUseCase.setParams(groupId, source)
        groupInfoUseCase.executeQuerySafeMode(
                {
                    onSuccess(it.topAdsGetPromoGroup?.data!!)
                },
                { throwable ->
                    throwable.printStackTrace()
                })
    }


    fun getHeadlineInfo(resources: Resources, groupId: String, onSuccess: (HeadlineInfoResponse.TopAdsGetPromoHeadline.Data) -> Unit) {
        getHeadlineInfoUseCase.setGraphqlQuery(GraphqlHelper.loadRawString(resources,
            com.tokopedia.topads.common.R.raw.get_headline_detail))
        getHeadlineInfoUseCase.setParams(groupId)
        getHeadlineInfoUseCase.executeQuerySafeMode(
            {
                onSuccess(it.topAdsGetPromoGroup?.data!!)
            },
            { throwable ->
                throwable.printStackTrace()
            })
    }

    fun getProductStats(resources: Resources, startDate: String, endDate: String, adIds: List<String>, onSuccess: (GetDashboardProductStatistics) -> Unit, selectedSortId: String, selectedStatusId: Int?, goalId: Int) {
        topAdsGetProductStatisticsUseCase.setGraphqlQuery(GraphqlHelper.loadRawString(resources,
                com.tokopedia.topads.common.R.raw.gql_query_product_statistics))
        topAdsGetProductStatisticsUseCase.setParams(startDate, endDate, adIds, selectedSortId, selectedStatusId
                ?: 0, goalId)
        topAdsGetProductStatisticsUseCase.executeQuerySafeMode(
                {
                    onSuccess(it.getDashboardProductStatistics)
                },
                {
                    it.printStackTrace()
                })
    }

    fun getBidInfo(suggestions: List<DataSuggestions>, sourceValue: String, onSuccess: (List<TopadsBidInfo.DataItem>) -> Unit) {
        bidInfoUseCase.setParams(suggestions, ParamObject.GROUP, sourceValue)
        bidInfoUseCase.executeQuerySafeMode(
            {
                onSuccess(it.topadsBidInfo.data)
            },
            { throwable ->
                throwable.printStackTrace()
            })
    }

    fun topAdsCreated(dataGrp: HashMap<String, Any?>,dataKey: HashMap<String,Any?>, onSuccess: (() -> Unit), onError: ((error: String?) -> Unit)) {
        val productBundle = Bundle()
        val param = topAdsCreateUseCase.setParam(ParamObject.EDIT_PAGE, productBundle, dataKey, dataGrp)
        topAdsCreateUseCase.execute(param, object : Subscriber<Map<Type, RestResponse>>() {
            override fun onNext(typeResponse: Map<Type, RestResponse>) {
                val token = object : TypeToken<DataResponse<FinalAdResponse?>>() {}.type
                val restResponse: RestResponse? = typeResponse[token]
                val response = restResponse?.getData() as DataResponse<FinalAdResponse>
                val dataGroup = response.data?.topadsManageGroupAds?.groupResponse
                if (dataGroup?.errors.isNullOrEmpty())
                    onSuccess()
                else {
                    var error = ""
                    if (!dataGroup?.errors.isNullOrEmpty())
                        error = dataGroup?.errors?.firstOrNull()?.detail ?: ""
                    onError(error)
                }
            }

            override fun onCompleted() {}

            override fun onError(e: Throwable?) {
                onError(e?.message)
                e?.printStackTrace()
            }
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


    fun getTopAdsStatistic(
        startDate: Date, endDate: Date, @TopAdsStatisticsType selectedStatisticType: Int,
        onSuccessGetStatisticsInfo: (dataStatistic: DataStatistic) -> Unit,
        groupId: String, goalId: Int,
    ) {
        launchCatchError(block = {
            val params = topAdsGetStatisticsUseCase.createRequestParams(
                startDate, endDate, selectedStatisticType, userSession.shopId, groupId, goalId)

            val response = topAdsGetStatisticsUseCase.execute(params)

            onSuccessGetStatisticsInfo(response.topadsDashboardStatistics.data)
        }, onError = {

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
        launchCatchError(block = {
            val query = GraphqlHelper.loadRawString(resources, R.raw.gql_query_group_action)
            val requestParams = topAdsGroupActionUseCase.setParams(action, groupIds)

            topAdsGroupActionUseCase.execute(query, requestParams)
        }, onError = {
            it.printStackTrace()
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
        topAdsKeywordsActionUseCase.cancelJobs()
        topAdsProductActionUseCase.unsubscribe()
        topAdsGetGroupListUseCase.unsubscribe()
        topAdsGetProductStatisticsUseCase.cancelJobs()
        getHeadlineInfoUseCase.cancelJobs()
        topAdsGetProductKeyCountUseCase.cancelJobs()
        bidInfoUseCase.cancelJobs()
    }
}



