package com.tokopedia.topads.dashboard.view.presenter

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.model.*
import com.tokopedia.topads.common.data.response.*
import com.tokopedia.topads.common.data.response.groupitem.GetTopadsDashboardGroupStatistics
import com.tokopedia.topads.common.data.response.groupitem.GroupItemResponse
import com.tokopedia.topads.common.data.response.nongroupItem.GetDashboardProductStatistics
import com.tokopedia.topads.common.data.response.nongroupItem.NonGroupResponse
import com.tokopedia.topads.common.data.util.Utils.locale
import com.tokopedia.topads.common.domain.interactor.*
import com.tokopedia.topads.common.domain.usecase.*
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsStatisticsType
import com.tokopedia.topads.dashboard.data.model.*
import com.tokopedia.topads.dashboard.data.model.insightkey.InsightKeyData
import com.tokopedia.topads.dashboard.data.raw.BUDGET_RECOMMENDATION
import com.tokopedia.topads.dashboard.data.raw.PRODUCT_RECOMMENDATION
import com.tokopedia.topads.dashboard.data.raw.SHOP_AD_INFO
import com.tokopedia.topads.dashboard.domain.interactor.*
import com.tokopedia.topads.dashboard.view.listener.TopAdsDashboardView
import com.tokopedia.topads.headline.data.ShopAdInfo
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Created by hadi.putra on 23/04/18.
 */

class TopAdsDashboardPresenter @Inject constructor(
    private val topAdsGetShopDepositUseCase: TopAdsGetDepositUseCase,
    private val shopAdInfoUseCase: GraphqlUseCase<ShopAdInfo>,
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
    private val getStatisticUseCase: TopAdsGetStatisticsUseCase,
    private val budgetRecomUseCase: GraphqlUseCase<DailyBudgetRecommendationModel>,
    private val productRecomUseCase: GraphqlUseCase<ProductRecommendationModel>,
    private val validGroupUseCase: TopAdsGroupValidateNameUseCase,
    private val topAdsCreateUseCase: TopAdsCreateUseCase,
    private val bidInfoUseCase: BidInfoUseCase,
    private val groupInfoUseCase: GroupInfoUseCase,
    private val adsStatusUseCase: GraphqlUseCase<AdStatusResponse>,
    private val autoAdsStatusUseCase: GraphqlUseCase<AutoAdsResponse>,
    private val getExpiryDateUseCase: TopadsGetFreeDepositUseCase,
    private val getHiddenTrialUseCase: GraphqlUseCase<FreeTrialShopListResponse>,
    private val whiteListedUserUseCase: GetWhiteListedUserUseCase,
    private val topAdsGetDeletedAdsUseCase: TopAdsGetDeletedAdsUseCase,
    private val userSession: UserSessionInterface,
) : BaseDaggerPresenter<TopAdsDashboardView>(), CoroutineScope {

    private val job = SupervisorJob()
    var isShopWhiteListed: MutableLiveData<Boolean> = MutableLiveData()
    private val _expiryDateHiddenTrial: MutableLiveData<String> = MutableLiveData()
    val expiryDateHiddenTrial: LiveData<String> = _expiryDateHiddenTrial

    companion object {
        const val HIDDEN_TRIAL_FEATURE = 21
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", locale)
    }

    fun getGroupData(page: Int, search: String, sort: String, status: Int?, startDate: String,
                     endDate: String, groupType: Int, onSuccess: (GroupItemResponse.GetTopadsDashboardGroups) -> Unit) {
        val requestParams = topAdsGetGroupDataUseCase.setParams(search, page, sort, status, startDate, endDate, groupType)
        getDataGroup(requestParams, onSuccess)
    }

    private fun getDataGroup(requestParams: RequestParams, onSuccess: (GroupItemResponse.GetTopadsDashboardGroups) -> Unit) {
        launchCatchError(block = {
            val response = topAdsGetGroupDataUseCase.execute(requestParams)

            onSuccess(response.getTopadsDashboardGroups)

        }, onError = {
            it.printStackTrace()
        })
    }


    fun getStatistic(
        startDate: Date, endDate: Date, @TopAdsStatisticsType selectedStatisticType: Int,
        adType: String, onSuccesGetStatisticsInfo: ((dataStatistic: DataStatistic) -> Unit),
    ) {
        launchCatchError(block = {
            val requestParams = getStatisticUseCase.createRequestParams(
                startDate, endDate, selectedStatisticType, userSession.shopId, adType)

            val dataStatistic =
                getStatisticUseCase.execute(requestParams).topadsDashboardStatistics.data

            onSuccesGetStatisticsInfo(dataStatistic)
        }, onError = {
            view?.onErrorGetStatisticsInfo(it)
        })
    }

    fun getGroupStatisticsData(
        page: Int, search: String, sort: String, status: Int?, startDate: String, endDate: String,
        groupIds: List<String>, onSuccess: (GetTopadsDashboardGroupStatistics) -> Unit) {
        launchCatchError(block = {
            val params = topAdsGetGroupStatisticsUseCase.setParams(search, page, sort, status, startDate, endDate, groupIds)

            val response = topAdsGetGroupStatisticsUseCase.execute(params).getTopadsDashboardGroupStatistics

            onSuccess(response)
        }, onError = {
            it.printStackTrace()
        })
    }

    fun getProductStats(resources: Resources, startDate: String, endDate: String, adIds: List<String>, sort: String, status: Int, onSuccess: ((GetDashboardProductStatistics) -> Unit)) {
        topAdsGetProductStatisticsUseCase.setGraphqlQuery(GraphqlHelper.loadRawString(resources,
                com.tokopedia.topads.common.R.raw.gql_query_product_statistics))
        topAdsGetProductStatisticsUseCase.setParams(startDate, endDate, adIds, sort, status)
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

    fun getGroupList(search: String, onSuccess: (List<GroupListDataItem>) -> Unit) {
        launchCatchError(block = {
            val params = topAdsGetGroupListUseCase.setParamsForKeyWord(search)

            val response = topAdsGetGroupListUseCase.execute(params)

            onSuccess(response.getTopadsDashboardGroups.data)

        }, onError = {
            it.printStackTrace()
        })
    }

    fun setGroupAction(
        onSuccess: ((action: String) -> Unit), action: String,
        groupIds: List<String>, resources: Resources,
    ) {
        launchCatchError(block = {
            val query = GraphqlHelper.loadRawString(resources, R.raw.gql_query_group_action)
            val requestParams = topAdsGroupActionUseCase.setParams(action, groupIds)

            val response = topAdsGroupActionUseCase.execute(query, requestParams)

            if (response.topAdsEditGroupBulk?.errors?.isEmpty() == true) {
                onSuccess(response.topAdsEditGroupBulk.data.action)
            } else {
                view?.onError(response.topAdsEditGroupBulk?.errors?.firstOrNull()?.detail ?: "")
            }
        }, onError = { error ->
            error.printStackTrace()
            error.message?.let { view?.onError(it) }
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
        onSuccess: (() -> Unit), action: String, adIds: List<String>, selectedFilter: String?,
    ) {
        launchCatchError(block = {
            val params = topAdsProductActionUseCase.setParams(action, adIds, selectedFilter)
            topAdsProductActionUseCase.execute(params)
            onSuccess()
        }, onError = {
            it.printStackTrace()
        })
    }

    fun getGroupProductData(
        page: Int, groupId: String?, search: String, sort: String,
        status: Int?, startDate: String, endDate: String, goalId: Int,
        onSuccess: (NonGroupResponse.TopadsDashboardGroupProducts) -> Unit, onEmpty: () -> Unit,
    ) {
        launchCatchError(block = {
            val requestParams = topAdsGetGroupProductDataUseCase.setParams(
                groupId, page, search, sort, status, startDate, endDate, goalId = goalId)

            val nonGroupResponse =
                topAdsGetGroupProductDataUseCase.execute(requestParams).topadsDashboardGroupProducts

            if (nonGroupResponse.data.isEmpty()) {
                onEmpty()
            } else {
                onSuccess(nonGroupResponse)
            }
        }, onError = {
            onEmpty()
        })
    }

    fun getInsight(resources: Resources, onSuccess: ((InsightKeyData) -> Unit)) {
        launchCatchError(block = {
            val requestParams = topAdsInsightUseCase.setParams()
            val param = GraphqlHelper.loadRawString(resources, R.raw.gql_query_insights_keyword)

            val responseData = topAdsInsightUseCase.execute(param, requestParams)

            onSuccess.invoke(responseData)
        }, onError = {
            it.printStackTrace()
        })
    }

    @GqlQuery("ShopInfo", SHOP_AD_INFO)
    fun getShopAdsInfo(onSuccess: ((ShopAdInfo)) -> Unit) {
        val params = mapOf(ParamObject.SHOP_ID to userSession.shopId, ParamObject.SOURCE to TopAdsDashboardConstant.SOURCE_ANDROID_HEADLINE)
        shopAdInfoUseCase.setTypeClass(ShopAdInfo::class.java)
        shopAdInfoUseCase.setRequestParams(params)
        shopAdInfoUseCase.setGraphqlQuery(ShopInfo.GQL_QUERY)
        shopAdInfoUseCase.execute({
            onSuccess(it)
        }, {
            Timber.e(it, "P1#TOPADS_DASHBOARD_PRESENTER_GET_STATISTIC#%s", it.localizedMessage)
        })
    }

    fun getExpiryDate(resources: Resources) {
        getExpiryDateUseCase.execute {
            _expiryDateHiddenTrial.postValue(it.expiryDate)
        }
    }

    fun getShopListHiddenTrial(resources: Resources) {
        getHiddenTrialUseCase.setGraphqlQuery(GraphqlHelper.loadRawString(resources, R.raw.query_hidden_trial_shoplist))
        getHiddenTrialUseCase.setRequestParams(mapOf("shopID" to userSession.shopId.toIntOrZero()))
        getHiddenTrialUseCase.setTypeClass(FreeTrialShopListResponse::class.java)
        getHiddenTrialUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CACHE_ONLY)
                .setExpiryTime(GraphqlConstant.ExpiryTimes.WEEK.`val`())
                .setSessionIncluded(true)
                .build())
        getHiddenTrialUseCase.execute({
            var data = false
            it.topAdsGetShopWhitelistedFeature.data.forEach lit@{free->
                if (free?.featureID == HIDDEN_TRIAL_FEATURE) {
                    data = true
                    return@lit
                }
            }
            isShopWhiteListed.postValue(data)
        }, {
            it.printStackTrace()
        })
    }

    fun getAdsStatus(resources: Resources) {
        adsStatusUseCase.setGraphqlQuery(GraphqlHelper.loadRawString(resources, com.tokopedia.topads.common.R.raw.query_autoads_shop_info))
        adsStatusUseCase.setRequestParams(mapOf("shopId" to userSession.shopId, "source" to "android.topads_product_iklan"))
        adsStatusUseCase.setTypeClass(AdStatusResponse::class.java)
        adsStatusUseCase.execute({
            view?.onSuccessAdStatus(it.topAdsGetShopInfo.data)
        }, {
            Timber.e(it, "P1#TOPADS_DASHBOARD_PRESENTER_AUTO_TOPADS_STATUS#%s", it.localizedMessage)
        })
    }

    fun getAutoAdsStatus(resources: Resources, onSuccess: ((data: AutoAdsResponse.TopAdsGetAutoAds.Data) -> Unit)) {
        autoAdsStatusUseCase.setGraphqlQuery(GraphqlHelper.loadRawString(resources, com.tokopedia.topads.common.R.raw.query_auto_ads_status))
        autoAdsStatusUseCase.setRequestParams(mapOf("shopId" to userSession.shopId, "source" to "android.topads_product_iklan"))
        autoAdsStatusUseCase.setTypeClass(AutoAdsResponse::class.java)
        autoAdsStatusUseCase.execute({
            onSuccess(it.topAdsGetAutoAds.data)
        }, {
            Timber.e(it, "P1#TOPADS_DASHBOARD_PRESENTER_AUTO_TOPADS_STATUS#%s", it.localizedMessage)
        })
    }

    @GqlQuery("ProductRecommend", PRODUCT_RECOMMENDATION)
    fun getProductRecommendation(onSuccess: ((ProductRecommendationModel)) -> Unit) {
        val params = mapOf(ParamObject.SHOP_id to userSession.shopId)
        productRecomUseCase.setTypeClass(ProductRecommendationModel::class.java)
        productRecomUseCase.setRequestParams(params)
        productRecomUseCase.setGraphqlQuery(ProductRecommend.GQL_QUERY)
        productRecomUseCase.execute({
            onSuccess(it)
        }, {
            Timber.e(it, "P1#TOPADS_DASHBOARD_PRESENTER_GET_STATISTIC_PRODUCT_RECOM#%s", it.localizedMessage)
        }
        )
    }


    @GqlQuery("BudgetRecommend", BUDGET_RECOMMENDATION)
    fun getDailyBudgetRecommendation(onSuccess: ((DailyBudgetRecommendationModel)) -> Unit) {
        val params = mapOf(ParamObject.SHOP_Id to userSession.shopId)
        budgetRecomUseCase.setTypeClass(DailyBudgetRecommendationModel::class.java)
        budgetRecomUseCase.setRequestParams(params)
        budgetRecomUseCase.setGraphqlQuery(BudgetRecommend.GQL_QUERY)
        budgetRecomUseCase.execute({
            onSuccess(it)
        }, {
            Timber.e(it, "P1#TOPADS_DASHBOARD_PRESENTER_GET_STATISTIC_BUDGET_RECOM#%s", it.localizedMessage)
        }
        )
    }

    fun editBudgetThroughInsight(
        adOperations: MutableList<GroupEditInput.Group.AdOperationsItem>?,
        priceBid: Float?, currentGroupId: String, dailyBudget: Double?,
        onSuccess: ((FinalAdResponse.TopadsManageGroupAds)) -> Unit,
        onError: ((String)) -> Unit,
    ) {
        launchCatchError(block = {
            val params = topAdsCreateUseCase.createRequestParamEditBudgetInsight(
                adOperations, priceBid, dailyBudget, currentGroupId
            )
            val response = topAdsCreateUseCase.execute(params)

            onSuccess(response.topadsManageGroupAds)
        }, onError = {
            it.printStackTrace()
            Timber.e(it, "P1#TOPADS_DASHBOARD_PRESENTER_EDIT_RECOM_BUDGET#%s", it.localizedMessage)
        })
    }

    fun validateGroup(groupName: String, onSuccess: ((ResponseGroupValidateName.TopAdsGroupValidateNameV2) -> Unit)) {
        validGroupUseCase.setParams(groupName, "android.topads_validate_group")
        validGroupUseCase.execute(
                {
                    onSuccess(it.topAdsGroupValidateName)
                },
                { throwable ->
                    throwable.printStackTrace()
                })
    }

    fun createGroup(
        productIds: List<String>, currentGroupName: String, priceBid: Double,
        suggestedBidValue: Double, error: (String?) -> Unit,
    ) {
        val param =
            topAdsCreateUseCase.createRequestParamActionCreate(productIds, currentGroupName, priceBid, suggestedBidValue)

        launchCatchError(onError = {
            error(it.localizedMessage)
        }, block = {
            topAdsCreateUseCase.execute(param)
            error(null)
        })
    }

    fun getBidInfo(suggestion: List<DataSuggestions>, onSuccess: ((List<TopadsBidInfo.DataItem>) -> Unit)) {
        bidInfoUseCase.setParams(suggestion, ParamObject.PRODUCT, ParamObject.SOURCE_CREATE_HEADLINE)
        bidInfoUseCase.executeQuerySafeMode({
            onSuccess(it.topadsBidInfo.data)
        }, {
            Timber.e(it, "P1#TOPADS_DASHBOARD_PRESENTER_BID_INFO#%s", it.localizedMessage)
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

    fun getWhiteListedUser(onSuccess: (WhiteListUserResponse.TopAdsGetShopWhitelistedFeature) -> Unit, isFinished: () -> Unit) {
        whiteListedUserUseCase.setParams()
        whiteListedUserUseCase.executeQuerySafeMode(
            {
                onSuccess(it)
                isFinished.invoke()
            },
            {
                throwable ->
                    throwable.printStackTrace()
                    isFinished.invoke()
            }
        )
    }

    fun getDeletedAds(
        page: Int,
        type: String,
        startDate: String,
        endDate: String,
        onSuccess: (TopAdsDeletedAdsResponse) -> Unit,
        onEmptyResult: () -> Unit
    ) {
        topAdsGetDeletedAdsUseCase.setParams(page, type, startDate, endDate)
        topAdsGetDeletedAdsUseCase.execute(onSuccess, onEmptyResult)
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun detachView() {
        super.detachView()
        job.cancel()
        topAdsGetShopDepositUseCase.cancelJobs()
        gqlGetShopInfoUseCase.cancelJobs()
        topAdsGetProductKeyCountUseCase.cancelJobs()
        topAdsGetProductStatisticsUseCase.cancelJobs()
        budgetRecomUseCase.cancelJobs()
        validGroupUseCase.cancelJobs()
        productRecomUseCase.cancelJobs()
        bidInfoUseCase.cancelJobs()
        bidInfoUseCase.cancelJobs()
        groupInfoUseCase.cancelJobs()
        whiteListedUserUseCase.cancelJobs()
        topAdsGetDeletedAdsUseCase.cancelJob()
    }
}
