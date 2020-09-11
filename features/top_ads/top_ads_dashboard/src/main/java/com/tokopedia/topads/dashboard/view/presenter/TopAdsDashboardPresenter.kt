package com.tokopedia.topads.dashboard.view.presenter

import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.topads.common.data.exception.ResponseErrorException
import com.tokopedia.topads.common.data.model.DataDeposit
import com.tokopedia.topads.common.domain.interactor.TopAdsDatePickerInteractor
import com.tokopedia.topads.common.domain.interactor.TopAdsGetShopDepositUseCase
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsStatisticsType
import com.tokopedia.topads.dashboard.data.model.*
import com.tokopedia.topads.common.data.response.groupitem.GetTopadsDashboardGroupStatistics
import com.tokopedia.topads.common.data.response.groupitem.GroupItemResponse
import com.tokopedia.topads.dashboard.data.model.insightkey.InsightKeyData
import com.tokopedia.topads.common.data.response.nongroupItem.GetDashboardProductStatistics
import com.tokopedia.topads.common.data.response.nongroupItem.NonGroupResponse
import com.tokopedia.topads.common.domain.interactor.TopAdsGetGroupDataUseCase
import com.tokopedia.topads.common.domain.interactor.TopAdsProductActionUseCase
import com.tokopedia.topads.dashboard.domain.interactor.*
import com.tokopedia.topads.dashboard.view.listener.TopAdsDashboardView
import com.tokopedia.topads.debit.autotopup.data.model.AutoTopUpData
import com.tokopedia.topads.debit.autotopup.data.model.AutoTopUpStatus
import com.tokopedia.topads.sourcetagging.constant.TopAdsSourceOption
import com.tokopedia.topads.sourcetagging.domain.interactor.TopAdsAddSourceTaggingUseCase
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * Created by hadi.putra on 23/04/18.
 */

class TopAdsDashboardPresenter @Inject
constructor(private val topAdsGetShopDepositUseCase: TopAdsGetShopDepositUseCase,
            private val gqlGetShopInfoUseCase: GQLGetShopInfoUseCase,
            private val topAdsDatePickerInteractor: TopAdsDatePickerInteractor,
            private val topAdsGetStatisticsUseCase: TopAdsGetStatisticsUseCase,
            private val topAdsAddSourceTaggingUseCase: TopAdsAddSourceTaggingUseCase,
            private val deleteTopAdsStatisticsUseCase: DeleteTopAdsStatisticsUseCase,
            private val topAdsGetGroupDataUseCase: TopAdsGetGroupDataUseCase,
            private val topAdsGetGroupStatisticsUseCase: TopAdsGetGroupStatisticsUseCase,
            private val topAdsGetProductStatisticsUseCase: TopAdsGetProductStatisticsUseCase,
            private val topAdsGetProductKeyCountUseCase: TopAdsGetProductKeyCountUseCase,
            private val topAdsGetGroupListUseCase: TopAdsGetGroupListUseCase,
            private val topAdsGroupActionUseCase: TopAdsGroupActionUseCase,
            private val topAdsProductActionUseCase: TopAdsProductActionUseCase,
            private val topAdsGetGroupProductDataUseCase: TopAdsGetGroupProductDataUseCase,
            private val topAdsInsightUseCase: TopAdsInsightUseCase,
            private val userSession: UserSessionInterface) : BaseDaggerPresenter<TopAdsDashboardView>() {

    var isShopWhiteListed: MutableLiveData<Boolean> = MutableLiveData()
    var expiryDateHiddenTrial: MutableLiveData<String> = MutableLiveData()
    val HIDDEN_TRIAL_FEATURE = 21
    private var SELECTION_TYPE_DEF = 0
    private var SELECTION_IND_DEF = 2

    fun getShopDeposit(onSuccess: ((dataDeposit: DataDeposit) -> Unit)) {
        topAdsGetShopDepositUseCase.execute(TopAdsGetShopDepositUseCase.createParams(userSession.shopId),
                object : Subscriber<DataDeposit>() {
                    override fun onCompleted() {}

                    override fun onError(e: Throwable) {
                        view?.onLoadTopAdsShopDepositError(e)
                    }

                    override fun onNext(dataDeposit: DataDeposit) {
                        onSuccess(dataDeposit)
                    }
                })
    }

    fun saveSourceTagging(@TopAdsSourceOption source: String) {
        topAdsAddSourceTaggingUseCase.execute(TopAdsAddSourceTaggingUseCase.createRequestParams(source),
                object : Subscriber<Void>() {
                    override fun onCompleted() {}

                    override fun onError(e: Throwable) {}

                    override fun onNext(aVoid: Void) {}
                })
    }

    fun getGroupData(resources: Resources, page: Int, search: String, sort: String, status: Int?,
                     startDate: String, endDate: String, onSuccess: ((GroupItemResponse.GetTopadsDashboardGroups) -> Unit)) {
        topAdsGetGroupDataUseCase.setGraphqlQuery(GraphqlHelper.loadRawString(resources,
                R.raw.query_get_groups_dashboard))
        topAdsGetGroupDataUseCase.setParams(search, page, sort, status, startDate, endDate)
        topAdsGetGroupDataUseCase.executeQuerySafeMode(
                {
                    onSuccess(it.getTopadsDashboardGroups)
                },
                {
                    it.printStackTrace()
                })
    }


    fun getGroupStatisticsData(resources: Resources, page: Int, search: String, sort: String, status: Int?,
                               startDate: String, endDate: String, groupIds: List<String>, onSuccess: ((GetTopadsDashboardGroupStatistics) -> Unit)) {
        topAdsGetGroupStatisticsUseCase.setGraphqlQuery(GraphqlHelper.loadRawString(resources,
                R.raw.gql_query_group_statistics))
        topAdsGetGroupStatisticsUseCase.setParams(search, page, sort, status, startDate, endDate, groupIds)
        topAdsGetGroupStatisticsUseCase.executeQuerySafeMode(
                {
                    onSuccess(it.getTopadsDashboardGroupStatistics)
                },
                {
                    it.printStackTrace()
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

    fun saveDate(startDate: Date, endDate: Date) {
        topAdsDatePickerInteractor.saveDate(startDate, endDate)
    }

    fun saveSelectionDatePicker() {
        topAdsDatePickerInteractor.saveSelectionDatePicker(SELECTION_TYPE_DEF, SELECTION_IND_DEF)
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
        topAdsGroupActionUseCase.setGraphqlQuery(GraphqlHelper.loadRawString(resources,
                R.raw.gql_query_group_action))
        topAdsGroupActionUseCase.setParams(action, groupIds)
        topAdsGroupActionUseCase.executeQuerySafeMode(
                {
                    onSuccess(action)
                },
                {
                    it.printStackTrace()

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


    fun getGroupProductData(resources: Resources, page: Int, groupId: Int?,
                            search: String, sort: String, status: Int?, startDate: String, endDate: String, onSuccess: ((NonGroupResponse.TopadsDashboardGroupProducts) -> Unit), onEmpty: (() -> Unit)) {
        topAdsGetGroupProductDataUseCase.setGraphqlQuery(GraphqlHelper.loadRawString(resources,
                com.tokopedia.topads.common.R.raw.query_get_group_products_dashboard))
        topAdsGetGroupProductDataUseCase.setParams(groupId, page, search, sort, status, startDate, endDate)
        topAdsGetGroupProductDataUseCase.executeQuerySafeMode(
                {
                    if (it.topadsDashboardGroupProducts.data.isEmpty()) {
                        onEmpty()
                    } else
                        onSuccess(it.topadsDashboardGroupProducts)
                },
                {
                    it.printStackTrace()

                })
    }

    fun getInsight(resources: Resources,onSuccess:((InsightKeyData)->Unit)){
        topAdsInsightUseCase.run {
            setGraphqlQuery(GraphqlHelper.loadRawString(resources,R.raw.gql_query_insights_keyword))
            setParams()
            executeQuerySafeMode(
                {
                        onSuccess(it)

                },{

                })
        }
    }

    fun getTopAdsStatistic(startDate: Date, endDate: Date, @TopAdsStatisticsType selectedStatisticType: Int, onSuccesGetStatisticsInfo: ((dataStatistic: DataStatistic) -> Unit)) {
        topAdsGetStatisticsUseCase.execute(TopAdsGetStatisticsUseCase.createRequestParams(startDate, endDate,
                selectedStatisticType, userSession.shopId, null), object : Subscriber<DataStatistic>() {
            override fun onCompleted() {}

            override fun onError(e: Throwable) {
                Timber.e(e, "P1#TOPADS_DASHBOARD_PRESENTER_GET_STATISTIC#%s", e.localizedMessage)
                view?.onErrorGetStatisticsInfo(e)
            }

            override fun onNext(dataStatistic: DataStatistic) {
                onSuccesGetStatisticsInfo(dataStatistic)
            }
        })
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
        topAdsGetShopDepositUseCase.unsubscribe()
        gqlGetShopInfoUseCase.cancelJobs()
        topAdsGetStatisticsUseCase.unsubscribe()
        topAdsAddSourceTaggingUseCase.unsubscribe()
        deleteTopAdsStatisticsUseCase.unsubscribe()
        topAdsGetGroupDataUseCase.cancelJobs()
        topAdsGetGroupProductDataUseCase.cancelJobs()
        topAdsGetGroupStatisticsUseCase.cancelJobs()
        topAdsGetProductKeyCountUseCase.cancelJobs()
        topAdsGetProductStatisticsUseCase.cancelJobs()
        topAdsInsightUseCase.cancelJobs()
    }
}
