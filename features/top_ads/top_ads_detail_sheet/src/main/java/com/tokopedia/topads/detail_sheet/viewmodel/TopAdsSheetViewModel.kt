package com.tokopedia.topads.detail_sheet.viewmodel

import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.response.TopAdsAutoAds
import com.tokopedia.topads.common.data.response.TopAdsAutoAdsData
import com.tokopedia.topads.common.data.response.nongroupItem.WithoutGroupDataItem
import com.tokopedia.topads.common.domain.interactor.TopAdsGetGroupProductDataUseCase
import com.tokopedia.topads.common.domain.interactor.TopAdsGetProductStatisticsUseCase
import com.tokopedia.topads.common.domain.interactor.TopAdsProductActionUseCase
import com.tokopedia.topads.detail_sheet.R
import com.tokopedia.topads.detail_sheet.data.AdData
import com.tokopedia.topads.detail_sheet.data.AdInfo
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

/**
 * Author errysuprayogi on 22,October,2019
 */
class TopAdsSheetViewModel @Inject constructor(
        private val topAdsGetGroupProductDataUseCase: TopAdsGetGroupProductDataUseCase,
        private val topAdsGetProductStatisticsUseCase: TopAdsGetProductStatisticsUseCase,
        private val topAdsGetGroupIdUseCase: GraphqlUseCase<AdInfo>,
        private val topAdsProductActionUseCase: TopAdsProductActionUseCase,
        private val topAdsGetAutoAdsStatusUseCase: GraphqlUseCase<TopAdsAutoAds.Response>,
        @Named("Main")
        val dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val autoAdsData = MutableLiveData<TopAdsAutoAdsData>()

    companion object {
        const val GROUP_REQUEST = """query topAdsGetPromo(${'$'}shopID: String!, ${'$'}adID: String!) { 
            topAdsGetPromo(shopID: ${'$'}shopID, adID: ${'$'}adID) {
              data { 
                adID 
                adType 
                groupID 
                shopID 
                itemID 
                status 
                priceBid 
                priceDaily 
                adStartDate 
                adStartTime 
                adEndDate 
                adEndTime 
                adImage 
                adTitle 
              } 
              errors { 
                code 
                detail 
                title 
              } 
             } 
            }""""
    }


    fun getProductStats(resources: Resources, adIds: List<String>, onSuccess: ((List<WithoutGroupDataItem>) -> Unit)) {
        topAdsGetProductStatisticsUseCase.setGraphqlQuery(GraphqlHelper.loadRawString(resources,
                R.raw.gql_query_product_statistics))
        topAdsGetProductStatisticsUseCase.setParams("", "", adIds)
        topAdsGetProductStatisticsUseCase.executeQuerySafeMode(
                {
                    onSuccess(it.getDashboardProductStatistics.data)
                },
                {
                    it.printStackTrace()
                })

    }

    fun getGroupProductData(resources: Resources, groupId: Int,
                            onSuccess: ((List<WithoutGroupDataItem>) -> Unit)) {
        topAdsGetGroupProductDataUseCase.setGraphqlQuery(GraphqlHelper.loadRawString(resources,
                R.raw.query_get_group_products_dashboard))
        topAdsGetGroupProductDataUseCase.setParams(groupId, 0, "", "", null, "", "")
        topAdsGetGroupProductDataUseCase.executeQuerySafeMode(
                {
                    onSuccess(it.topadsDashboardGroupProducts.data)
                },
                {
                    it.printStackTrace()

                })
    }

    @GqlQuery("CategoryList", GROUP_REQUEST)
    fun getGroupId(shopId: String, adId: String, onSuccess: ((List<AdData>) -> Unit)) {
        val params = mapOf(ParamObject.SHOP_ID to shopId,
                ParamObject.AD_ID to adId)
        topAdsGetGroupIdUseCase.setTypeClass(AdInfo::class.java)
        topAdsGetGroupIdUseCase.setRequestParams(params)
        topAdsGetGroupIdUseCase.setGraphqlQuery(CategoryList.GQL_QUERY)
        topAdsGetGroupIdUseCase.execute(
                onSuccessGroup(onSuccess),
                onError()
        )
    }


    fun setProductAction(onSuccess: ((action: String) -> Unit), action: String, adIds: List<String>, resources: Resources, selectedFilter: String?) {
        topAdsProductActionUseCase.setGraphqlQuery(GraphqlHelper.loadRawString(resources,
                R.raw.gql_query_product_action))
        topAdsProductActionUseCase.setParams(action, adIds, selectedFilter)
        topAdsProductActionUseCase.executeQuerySafeMode(
                {
                    onSuccess(action)
                },
                {
                    it.printStackTrace()
                })
    }

    fun getAutoAdsStatus(shopId: String, resources: Resources) {
        val params = mapOf(ParamObject.SHOP_Id to shopId.toInt())
        topAdsGetAutoAdsStatusUseCase.setGraphqlQuery(GraphqlHelper.loadRawString(resources, R.raw.query_auto_ads_status))
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


    private fun onSuccessGroup(onSuccess: (List<AdData>) -> Unit): (AdInfo) -> Unit {
        return {
            onSuccess(it.topAdsGetPromo.data)
        }
    }

    private fun onError(): (Throwable) -> Unit {
        return {
            it.printStackTrace()
        }
    }
}
