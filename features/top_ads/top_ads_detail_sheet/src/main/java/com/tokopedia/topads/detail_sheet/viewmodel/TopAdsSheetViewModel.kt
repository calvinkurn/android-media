package com.tokopedia.topads.detail_sheet.viewmodel

import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.response.ProductActionResponse
import com.tokopedia.topads.common.data.response.SingleAd
import com.tokopedia.topads.common.data.response.TopAdsAutoAds
import com.tokopedia.topads.common.data.response.TopAdsAutoAdsData
import com.tokopedia.topads.common.data.response.nongroupItem.WithoutGroupDataItem
import com.tokopedia.topads.common.domain.interactor.TopAdsGetGroupProductDataUseCase
import com.tokopedia.topads.common.domain.interactor.TopAdsGetProductStatisticsUseCase
import com.tokopedia.topads.common.domain.interactor.TopAdsProductActionUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetPromoUseCase
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import java.lang.reflect.Type
import javax.inject.Inject
import javax.inject.Named

/**
 * Author errysuprayogi on 22,October,2019
 */
class TopAdsSheetViewModel @Inject constructor(
        private val topAdsGetGroupProductDataUseCase: TopAdsGetGroupProductDataUseCase,
        private val topAdsGetProductStatisticsUseCase: TopAdsGetProductStatisticsUseCase,
        private val topAdsGetGroupIdUseCase: TopAdsGetPromoUseCase,
        private val topAdsProductActionUseCase: TopAdsProductActionUseCase,
        private val topAdsGetAutoAdsStatusUseCase: GraphqlUseCase<TopAdsAutoAds.Response>,
        @Named("Main")
        val dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val autoAdsData = MutableLiveData<TopAdsAutoAdsData>()

    fun getProductStats(resources: Resources, adIds: List<String>, onSuccess: ((List<WithoutGroupDataItem>) -> Unit)) {
        topAdsGetProductStatisticsUseCase.setGraphqlQuery(GraphqlHelper.loadRawString(resources,
                com.tokopedia.topads.common.R.raw.gql_query_product_statistics))
        topAdsGetProductStatisticsUseCase.setParams("", "", adIds)
        topAdsGetProductStatisticsUseCase.executeQuerySafeMode(
                {
                    onSuccess(it.getDashboardProductStatistics.data)
                },
                {
                    it.printStackTrace()
                })
    }

    fun getGroupProductData(groupId: String, onSuccess: (List<WithoutGroupDataItem>) -> Unit) {
        launchCatchError(block = {
            val requestParams =
                topAdsGetGroupProductDataUseCase.setParams(groupId, 0, "", "", null, "", "")

            val nonGroupResponse =
                topAdsGetGroupProductDataUseCase.execute(requestParams).topadsDashboardGroupProducts

            onSuccess(nonGroupResponse.data)
        }, onError = {
            it.printStackTrace()
        })
    }

    fun getGroupId(shopId: String, adId: String, onSuccess: ((List<SingleAd>) -> Unit)) {
        topAdsGetGroupIdUseCase.setParams(adId, shopId)
        topAdsGetGroupIdUseCase.execute({
            onSuccess(it.topAdsGetPromo.data)
        }, {
            it.printStackTrace()
        })
    }

    fun setProductAction(
        onSuccess: (action: String) -> Unit, action: String, adIds: List<String>,
        selectedFilter: String?,
    ) {
        launchCatchError(block = {
            val params = topAdsProductActionUseCase.setParams(action, adIds, selectedFilter)
            topAdsProductActionUseCase.execute(params)
            onSuccess(action)
        }, onError = {
            it.printStackTrace()
        })
    }

    fun getAutoAdsStatus(shopId: String, resources: Resources) {
        val params = mapOf(ParamObject.SHOP_Id to shopId, ParamObject.SOURCE to "android.topads_sheet")
        topAdsGetAutoAdsStatusUseCase.setGraphqlQuery(GraphqlHelper.loadRawString(resources, com.tokopedia.topads.common.R.raw.query_auto_ads_status))
        topAdsGetAutoAdsStatusUseCase.setTypeClass(TopAdsAutoAds.Response::class.java)
        topAdsGetAutoAdsStatusUseCase.setRequestParams(params)
        topAdsGetAutoAdsStatusUseCase.execute(
                {
                    autoAdsData.postValue(it.autoAds.data)
                }, {
            it.printStackTrace()
        }
        )
    }

    public override fun onCleared() {
        super.onCleared()
        topAdsGetProductStatisticsUseCase.cancelJobs()
        topAdsGetGroupIdUseCase.cancelJobs()
        topAdsGetAutoAdsStatusUseCase.cancelJobs()
    }
}
