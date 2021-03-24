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
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.topads.common.data.exception.ResponseErrorException
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.model.DashGroupListResponse
import com.tokopedia.topads.common.data.model.DataSuggestions
import com.tokopedia.topads.common.data.model.GroupListDataItem
import com.tokopedia.topads.common.data.model.ResponseCreateGroup
import com.tokopedia.topads.common.data.response.*
import com.tokopedia.topads.common.data.response.groupitem.GetTopadsDashboardGroupStatistics
import com.tokopedia.topads.common.data.response.groupitem.GroupItemResponse
import com.tokopedia.topads.common.data.response.groupitem.GroupStatisticsResponse
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
import com.tokopedia.topads.dashboard.data.raw.STATS_URL
import com.tokopedia.topads.dashboard.domain.interactor.*
import com.tokopedia.topads.dashboard.view.listener.TopAdsDashboardView
import com.tokopedia.topads.debit.autotopup.data.model.AutoTopUpStatus
import com.tokopedia.topads.headline.data.ShopAdInfo
import com.tokopedia.usecase.RequestParams
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
constructor(private val topAdsGetShopDepositUseCase: TopAdsGetDepositUseCase,
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
            private val getStatisticUseCase: GetStatisticUseCase,
            private val budgetRecomUseCase: GraphqlUseCase<DailyBudgetRecommendationModel>,
            private val productRecomUseCase: GraphqlUseCase<ProductRecommendationModel>,
            private val topAdsEditUseCase: TopAdsEditUseCase,
            private val validGroupUseCase: TopAdsGroupValidateNameUseCase,
            private val createGroupUseCase: CreateGroupUseCase,
            private val bidInfoUseCase: BidInfoUseCase,
            private val groupInfoUseCase: GroupInfoUseCase,
            private val autoTopUpUSeCase: TopAdsAutoTopUpUSeCase,
            private val adsStatusUseCase: GraphqlUseCase<AdStatusResponse>,
            private val autoAdsStatusUseCase: GraphqlUseCase<AutoAdsResponse>,
            private val getExpiryDateUseCase: GraphqlUseCase<ExpiryDateResponse>,
            private val getHiddenTrialUseCase: GraphqlUseCase<FreeTrialShopListResponse>,
            private val userSession: UserSessionInterface) : BaseDaggerPresenter<TopAdsDashboardView>() {

    var isShopWhiteListed: MutableLiveData<Boolean> = MutableLiveData()
    var expiryDateHiddenTrial: MutableLiveData<String> = MutableLiveData()

    companion object {
        const val HIDDEN_TRIAL_FEATURE = 21
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", locale)
    }

    fun getShopDeposit(onSuccess: ((dataDeposit: DepositAmount) -> Unit)) {
        topAdsGetShopDepositUseCase.execute({
            onSuccess(it.topadsDashboardDeposits.data)
        }
                , {
            it.printStackTrace()
        })
    }

    fun getGroupData(page: Int, search: String, sort: String, status: Int?, startDate: String,
                     endDate: String, groupType: Int, onSuccess: (GroupItemResponse.GetTopadsDashboardGroups) -> Unit) {
        val requestParams = topAdsGetGroupDataUseCase.setParams(search, page, sort, status, startDate, endDate, groupType)
        getDataGroup(requestParams, onSuccess)
    }

    private fun getDataGroup(requestParams: RequestParams, onSuccess: (GroupItemResponse.GetTopadsDashboardGroups) -> Unit) {
        topAdsGetGroupDataUseCase.execute(requestParams, object : Subscriber<Map<Type, RestResponse>>() {
            override fun onCompleted() {}

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

    fun getGroupStatisticsData(page: Int, search: String, sort: String, status: Int?, startDate: String,
                               endDate: String, groupIds: List<String>, onSuccess: (GetTopadsDashboardGroupStatistics) -> Unit) {
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
                response.data?.getTopadsDashboardGroupStatistics?.let { onSuccess(it) }
            }
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
                if (response.data.topAdsEditGroupBulk?.errors?.isEmpty() == true) {
                    response.data.topAdsEditGroupBulk?.data?.action?.let { onSuccess(it) }
                } else {
                    view?.onError(response.data.topAdsEditGroupBulk?.errors?.firstOrNull()?.detail
                            ?: "")
                }
            }

            override fun onError(e: Throwable) {
                view?.onErrorGetStatisticsInfo(e)
            }
        })
    }

    fun setProductAction(onSuccess: (() -> Unit), action: String, adIds: List<String>,selectedFilter: String?) {
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


    fun getGroupProductData(page: Int, groupId: Int?, search: String,
                            sort: String, status: Int?, startDate: String, endDate: String, onSuccess: (NonGroupResponse.TopadsDashboardGroupProducts) -> Unit, onEmpty: () -> Unit) {
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

    fun getInsight(resources: Resources, onSuccess: ((InsightKeyData) -> Unit)) {
        topAdsInsightUseCase.setQuery(GraphqlHelper.loadRawString(resources, R.raw.gql_query_insights_keyword))
        val requestParams = topAdsInsightUseCase.setParams()
        topAdsInsightUseCase.execute(requestParams, object : Subscriber<Map<Type, RestResponse>>() {
            override fun onCompleted() {}

            override fun onError(e: Throwable?) {}

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

    @GqlQuery("ShopInfo", SHOP_AD_INFO)
    fun getShopAdsInfo(onSuccess: ((ShopAdInfo)) -> Unit) {
        val params = mapOf(ParamObject.SHOP_ID to userSession.shopId.toIntOrZero())
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
        getExpiryDateUseCase.setGraphqlQuery(GraphqlHelper.loadRawString(resources, R.raw.query_hidden_trial_expiry_date))
        getExpiryDateUseCase.setRequestParams(mapOf("shopID" to userSession.shopId.toIntOrZero()))
        getExpiryDateUseCase.setTypeClass(ExpiryDateResponse::class.java)
        getExpiryDateUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE)
                .setExpiryTime(GraphqlConstant.ExpiryTimes.WEEK.`val`())
                .setSessionIncluded(true)
                .build())
        getExpiryDateUseCase.execute({
            expiryDateHiddenTrial.postValue(it.topAdsGetFreeDeposit.expiryDate)
        }, {

        })
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
        })
    }

    fun getAdsStatus(resources: Resources) {
        adsStatusUseCase.setGraphqlQuery(GraphqlHelper.loadRawString(resources, com.tokopedia.topads.common.R.raw.query_autoads_shop_info))
        adsStatusUseCase.setRequestParams(mapOf("shopId" to userSession.shopId.toIntOrZero()))
        adsStatusUseCase.setTypeClass(AdStatusResponse::class.java)
        adsStatusUseCase.execute({
            view?.onSuccessAdStatus(it.topAdsGetShopInfo.data)
        }, {
            Timber.e(it, "P1#TOPADS_DASHBOARD_PRESENTER_AUTO_TOPADS_STATUS#%s", it.localizedMessage)
        })
    }

    fun getAutoAdsStatus(resources: Resources, onSuccess: ((data: AutoAdsResponse.TopAdsGetAutoAds.Data) -> Unit)) {
        autoAdsStatusUseCase.setGraphqlQuery(GraphqlHelper.loadRawString(resources, com.tokopedia.topads.common.R.raw.query_auto_ads_status))
        autoAdsStatusUseCase.setRequestParams(mapOf("shopId" to userSession.shopId.toIntOrZero()))
        autoAdsStatusUseCase.setTypeClass(AutoAdsResponse::class.java)
        autoAdsStatusUseCase.execute({
            onSuccess(it.topAdsGetAutoAds.data)
        }, {
            Timber.e(it, "P1#TOPADS_DASHBOARD_PRESENTER_AUTO_TOPADS_STATUS#%s", it.localizedMessage)
        })
    }

    fun getAutoTopUpStatus(resources: Resources, onSuccess: ((data: AutoTopUpStatus) -> Unit)) {
        autoTopUpUSeCase.setQuery(GraphqlHelper.loadRawString(resources, R.raw.gql_query_get_status_auto_topup))
        autoTopUpUSeCase.setParams()
        autoTopUpUSeCase.execute({ data ->
            when {
                data.response == null ->  Exception("Tidak ada data").printStackTrace()
                data.response.errors.isEmpty() -> onSuccess(data.response.data)
                else -> ResponseErrorException(data.response.errors).printStackTrace()
            }
        }, {
            Timber.e(it, "P1#TOPADS_DASHBOARD_PRESENTER_AUTO_TOPUP#%s", it.localizedMessage)
        })
    }


    @GqlQuery("ProductRecommend", PRODUCT_RECOMMENDATION)
    fun getProductRecommendation(onSuccess: ((ProductRecommendationModel)) -> Unit) {
        val params = mapOf(ParamObject.SHOP_id to userSession.shopId.toIntOrZero())
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
        val params = mapOf(ParamObject.SHOP_Id to userSession.shopId.toIntOrZero())
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

    fun editBudgetThroughInsight(productData: MutableList<GroupEditInput.Group.AdOperationsItem>?, groupData: HashMap<String, Any?>?, onSuccess: ((FinalAdResponse.TopadsManageGroupAds)) -> Unit, onError: ((String)) -> Unit) {
        val params = topAdsEditUseCase.setParam(productData, groupData)
        topAdsEditUseCase.execute(params, object : Subscriber<Map<Type, RestResponse>>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                e?.printStackTrace()
                Timber.e(e, "P1#TOPADS_DASHBOARD_PRESENTER_EDIT_RECOM_BUDGET#%s", e?.localizedMessage)
            }

            override fun onNext(typeResponse: Map<Type, RestResponse>) {
                val token = object : TypeToken<DataResponse<FinalAdResponse?>>() {}.type
                val restResponse: RestResponse? = typeResponse[token]
                val response = restResponse?.getData() as DataResponse<FinalAdResponse>
                response?.data?.topadsManageGroupAds?.let { onSuccess(it) }
            }
        })
    }

    fun validateGroup(groupName: String, onSuccess: ((ResponseGroupValidateName.TopAdsGroupValidateName) -> Unit)) {
        validGroupUseCase.setParams(groupName)
        validGroupUseCase.execute(
                {
                    onSuccess(it.topAdsGroupValidateName)
                },
                { throwable ->
                    throwable.printStackTrace()
                })
    }

    fun createGroup(param: HashMap<String, Any>, onSuccess: ((ResponseCreateGroup.TopadsCreateGroupAds) -> Unit)) {
        createGroupUseCase.setParams(param)
        createGroupUseCase.executeQuerySafeMode(
                {
                    onSuccess(it)

                }, {
            Timber.e(it, "P1#TOPADS_DASHBOARD_PRESENTER_CREATE_GROUP#%s", it.localizedMessage)

        }
        )
    }

    fun getBidInfo(suggestion: List<DataSuggestions>, onSuccess: ((List<TopadsBidInfo.DataItem>) -> Unit)) {
        bidInfoUseCase.setParams(suggestion, ParamObject.PRODUCT, ParamObject.SOURCE_CREATE_HEADLINE)
        bidInfoUseCase.executeQuerySafeMode({
            onSuccess(it.topadsBidInfo.data)
        }, {
            Timber.e(it, "P1#TOPADS_DASHBOARD_PRESENTER_BID_INFO#%s", it.localizedMessage)
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

    override fun detachView() {
        super.detachView()
        topAdsGetShopDepositUseCase.cancelJobs()
        gqlGetShopInfoUseCase.cancelJobs()
        topAdsGetGroupDataUseCase.unsubscribe()
        topAdsGetGroupProductDataUseCase.unsubscribe()
        topAdsGetGroupStatisticsUseCase.unsubscribe()
        topAdsGetProductKeyCountUseCase.cancelJobs()
        topAdsGetProductStatisticsUseCase.cancelJobs()
        topAdsGroupActionUseCase.unsubscribe()
        topAdsInsightUseCase.unsubscribe()
        budgetRecomUseCase.cancelJobs()
        validGroupUseCase.cancelJobs()
        topAdsEditUseCase.unsubscribe()
        productRecomUseCase.cancelJobs()
        createGroupUseCase.cancelJobs()
        bidInfoUseCase.cancelJobs()
        bidInfoUseCase.cancelJobs()
        groupInfoUseCase.cancelJobs()
    }
}