package com.tokopedia.topads.widget.dashboard.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topads.widget.dashboard.data.TopAdsDepositResponse
import com.tokopedia.topads.widget.dashboard.data.TopAdsStatisticResponse
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named
import com.tokopedia.topads.widget.dashboard.internal.ParamObject.END_DATE
import com.tokopedia.topads.widget.dashboard.internal.QueryObject.QUERY_TOPADS_DEPOSIT
import com.tokopedia.topads.widget.dashboard.internal.QueryObject.QUERY_TOPADS_STATISTIC
import com.tokopedia.topads.widget.dashboard.internal.ParamObject.SHOP_ID
import com.tokopedia.topads.widget.dashboard.internal.ParamObject.START_DATE
import com.tokopedia.topads.widget.dashboard.internal.DateObject
import com.tokopedia.topads.widget.dashboard.internal.ParamObject.CREDIT_DATA
import com.tokopedia.topads.widget.dashboard.internal.ParamObject.SHOP_DATA

/**
 * Author errysuprayogi on 25,October,2019
 */
class DashboardWidgetViewModel @Inject constructor(private val repository: GraphqlRepository,
                                                   private val userSession: UserSessionInterface,
                                                   private val rawQueries: Map<String, String>,
                                                   @Named("Main")
                                                   val dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    fun getTopAdsDeposit(onSuccessGetDeposit: ((TopAdsDepositResponse.Data) -> Unit),
                         onErrorGetAds: ((Throwable) -> Unit)) {
        launchCatchError(
                block = {
                    val param = mapOf(SHOP_ID to userSession.shopId?.toIntOrZero(),
                            CREDIT_DATA to "unclaimed", SHOP_DATA to "1")
                    val data = withContext(Dispatchers.IO) {
                        val request = GraphqlRequest(rawQueries[QUERY_TOPADS_DEPOSIT],
                                TopAdsDepositResponse.Data::class.java,
                                param, false)
                        val cacheStrategy = GraphqlCacheStrategy
                                .Builder(CacheType.ALWAYS_CLOUD).build()
                        repository.getReseponse(listOf(request), cacheStrategy)
                    }
                    data.getSuccessData<TopAdsDepositResponse.Data>().let {
                        onSuccessGetDeposit(it)
                    }
                },
                onError = {
                    onErrorGetAds(it)
                }
        )
    }

    fun getTopAdsStatisctic(onSuccessGetStatistic: ((TopAdsStatisticResponse.Data) -> Unit),
                            onErrorGetAds: ((Throwable) -> Unit)) {
        launchCatchError(
                block = {
                    val param = mapOf(SHOP_ID to userSession.shopId?.toIntOrZero(),
                            START_DATE to DateObject.startDate,
                            END_DATE to DateObject.endDate)
                    val data = withContext(Dispatchers.IO) {
                        val request = GraphqlRequest(rawQueries[QUERY_TOPADS_STATISTIC],
                                TopAdsStatisticResponse.Data::class.java,
                                param, false)
                        val cacheStrategy = GraphqlCacheStrategy
                                .Builder(CacheType.ALWAYS_CLOUD).build()
                        repository.getReseponse(listOf(request), cacheStrategy)
                    }
                    data.getSuccessData<TopAdsStatisticResponse.Data>().let {
                        onSuccessGetStatistic(it)
                    }
                },
                onError = {
                    onErrorGetAds(it)
                }
        )
    }


}