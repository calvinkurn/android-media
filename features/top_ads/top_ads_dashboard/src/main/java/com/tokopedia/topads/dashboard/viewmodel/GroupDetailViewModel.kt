package com.tokopedia.topads.dashboard.viewmodel

import android.content.res.Resources
import android.os.Bundle
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.internal.ParamObject.AUTO_BID_STATE
import com.tokopedia.topads.common.data.internal.ParamObject.STRATEGIES
import com.tokopedia.topads.common.data.model.DataSuggestions
import com.tokopedia.topads.common.data.model.GroupListDataItem
import com.tokopedia.topads.common.data.response.*
import com.tokopedia.topads.common.data.response.nongroupItem.GetDashboardProductStatistics
import com.tokopedia.topads.common.data.response.nongroupItem.NonGroupResponse
import com.tokopedia.topads.common.data.util.TopAdsEditUtils
import com.tokopedia.topads.common.domain.interactor.BidInfoUseCase
import com.tokopedia.topads.common.domain.interactor.TopAdsGetGroupProductDataUseCase
import com.tokopedia.topads.common.domain.interactor.TopAdsGetProductStatisticsUseCase
import com.tokopedia.topads.common.domain.interactor.TopAdsProductActionUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsCreateUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetGroupListUseCase
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsStatisticsType
import com.tokopedia.topads.dashboard.data.model.CountDataItem
import com.tokopedia.topads.dashboard.data.model.DataStatistic
import com.tokopedia.topads.dashboard.data.model.KeywordsResponse
import com.tokopedia.topads.dashboard.domain.interactor.*
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import javax.inject.Inject

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
        page: Int, groupId: String, search: String,
        sort: String, status: Int?, startDate: String, endDate: String, goalId: Int,
        onSuccess: (NonGroupResponse.TopadsDashboardGroupProducts) -> Unit, onEmpty: () -> Unit,
    ) {
        launchCatchError(block = {
            val requestParams = topAdsGetGroupProductDataUseCase.setParams(groupId, page,
                    search, sort, status, startDate, endDate, goalId = goalId)

            val nonGroupResponse = topAdsGetGroupProductDataUseCase.execute(requestParams).topadsDashboardGroupProducts
            if (nonGroupResponse.data.isEmpty()) {
                onEmpty()
            } else {
                onSuccess(nonGroupResponse)
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

    fun changeBidState(
        isAutomatic: Boolean, groupId: Int,
        suggestBidPerClick: Float = 0f, bidPencarian: Float = 0f, bidRecomendasi: Float = 0f, isDailyBudgetUnlimited: Boolean = false,
        onSuccess: () -> Unit
    ) {
        val dataGrp = hashMapOf<String, Any?>(
            ParamObject.ACTION_TYPE to ParamObject.ACTION_EDIT,
            ParamObject.GROUPID to groupId
        )
        if(!isDailyBudgetUnlimited && !isAutomatic)
            dataGrp[ParamObject.DAILY_BUDGET] = TopAdsEditUtils.calculateDailyBudget(bidPencarian.toInt(), bidRecomendasi.toInt())

        val dataKey = hashMapOf<String, Any?>()
        if (isAutomatic) {
            dataKey[STRATEGIES] = arrayListOf(AUTO_BID_STATE)
        } else {
            dataKey[STRATEGIES] = arrayListOf<String>()
            dataKey[ParamObject.SUGGESTION_BID_SETTINGS] = listOf(
                GroupEditInput.Group.TopadsSuggestionBidSetting(ParamObject.PRODUCT_SEARCH,
                    suggestBidPerClick),
                GroupEditInput.Group.TopadsSuggestionBidSetting(ParamObject.PRODUCT_BROWSE,
                    suggestBidPerClick)
            )
            dataKey[ParamObject.BID_TYPE] = listOf(
                TopAdsBidSettingsModel(ParamObject.PRODUCT_SEARCH, bidPencarian),
                TopAdsBidSettingsModel(ParamObject.PRODUCT_BROWSE, bidRecomendasi),
            )
        }

        topAdsCreated(dataGrp, dataKey, onSuccess, {})
    }

    fun topAdsCreated(
        dataGrp: HashMap<String, Any?>, dataKey: HashMap<String, Any?>,
        onSuccess: (() -> Unit), onError: ((error: String?) -> Unit),
    ) {
        launchCatchError(block = {
            val productBundle = Bundle()
            val param =
                topAdsCreateUseCase.setParam(ParamObject.EDIT_PAGE, productBundle, dataKey, dataGrp)
            val response = topAdsCreateUseCase.execute(param)
            val dataGroup = response.topadsManageGroupAds.groupResponse
            if (dataGroup.errors.isNullOrEmpty())
                onSuccess()
            else {
                var error = ""
                if (!dataGroup.errors.isNullOrEmpty())
                    error = dataGroup.errors?.firstOrNull()?.detail ?: ""
                onError(error)
            }
        }, onError = {
            onError(it.message)
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
            it.printStackTrace()
        })
    }

    fun getGroupList(search: String, onSuccess: (List<GroupListDataItem>) -> Unit) {
        launchCatchError(block = {
            val params = topAdsGetGroupListUseCase.setParamsForKeyWord(search)

            val response = topAdsGetGroupListUseCase.execute(params)

            onSuccess(response.getTopadsDashboardGroups.data)

        }, onError = {
            it.printStackTrace()
        })
    }

    fun setProductActionMoveGroup(
        groupId: String, productIds: List<String>, onSuccess: (() -> Unit),
    ) {
        launchCatchError(block = {
            val param = topAdsCreateUseCase.createRequestParamMoveGroup(
                groupId, TopAdsDashboardConstant.SOURCE_DASH, productIds, ParamObject.ACTION_ADD
            )
            topAdsCreateUseCase.execute(param)
            onSuccess()
        }, onError = {
            it.printStackTrace()
        })
    }

    fun setProductAction(
        onSuccess: () -> Unit, action: String, adIds: List<String>, selectedFilter: String?
    ) {
        launchCatchError(block = {
            val params = topAdsProductActionUseCase.setParams(action, adIds, selectedFilter)
            topAdsProductActionUseCase.execute(params)
            onSuccess()
        }, onError = {
            it.printStackTrace()
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

    fun setKeywordActionForGroup(
        groupId: String,action: String, keywordIds: List<String>,
        resources: Resources, onSuccess: (() -> Unit)
    ) {

        if(action != TopAdsDashboardConstant.ACTION_DELETE) {
            setKeywordAction(action, keywordIds, resources, onSuccess)
            return
        }

        launchCatchError(block = {
            val param = topAdsCreateUseCase.createRequestParamActionDelete(
                TopAdsDashboardConstant.SOURCE_DASH, groupId, keywordIds
            )
            topAdsCreateUseCase.execute(param)
            onSuccess()
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
        topAdsGetProductStatisticsUseCase.cancelJobs()
        getHeadlineInfoUseCase.cancelJobs()
        topAdsGetProductKeyCountUseCase.cancelJobs()
        bidInfoUseCase.cancelJobs()
        groupInfoUseCase.cancelJobs()
    }
}



