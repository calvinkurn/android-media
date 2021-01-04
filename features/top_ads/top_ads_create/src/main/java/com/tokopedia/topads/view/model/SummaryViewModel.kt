package com.tokopedia.topads.view.model

import android.content.Context
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.common.data.response.DepositAmount
import com.tokopedia.topads.common.domain.usecase.TopAdsGetDepositUseCase
import com.tokopedia.topads.create.R
import com.tokopedia.topads.data.response.ResponseCreateGroup
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class SummaryViewModel @Inject constructor(
        private val context: Context,
        @Named("Main")
        private val dispatcher: CoroutineDispatcher,
        private val topAdsGetShopDepositUseCase: TopAdsGetDepositUseCase,
        private val repository: GraphqlRepository) : BaseViewModel(dispatcher) {

    fun getTopAdsDeposit(onSuccessGetDeposit: ((DepositAmount) -> Unit),
                         onErrorGetAds: ((Throwable) -> Unit)) {

        topAdsGetShopDepositUseCase.execute({
            onSuccessGetDeposit(it.topadsDashboardDeposits.data)
        }
                , {
            onErrorGetAds(it)
        })
    }


    fun topAdsCreated(param: HashMap<String, Any>, onSuccessGetDeposit: ((ResponseCreateGroup) -> Unit),
                      onErrorGetAds: ((Throwable) -> Unit)) {
        launchCatchError(
                block = {
                    val data = withContext(Dispatchers.IO) {
                        val request = GraphqlRequest(GraphqlHelper.loadRawString(context.resources, R.raw.query_ads_create_activate_ads),
                                ResponseCreateGroup::class.java,
                                param)
                        val cacheStrategy = GraphqlCacheStrategy
                                .Builder(CacheType.CLOUD_THEN_CACHE).build()
                        repository.getReseponse(listOf(request), cacheStrategy)
                    }
                    data.getSuccessData<ResponseCreateGroup>().let {
                        onSuccessGetDeposit(it)
                    }
                },
                onError = {
                    onErrorGetAds(it)
                }
        )
    }
}