package com.tokopedia.topads.auto.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.auto.data.entity.TopAdsShopInfoData
import com.tokopedia.topads.auto.data.network.response.TopAdsShopInfo
import com.tokopedia.topads.auto.di.AutoAdsDispatcherProvider
import com.tokopedia.topads.auto.internal.RawQueryKeyObject
import com.tokopedia.topads.auto.view.RequestHelper
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Author errysuprayogi on 15,May,2019
 */
class TopAdsInfoViewModel @Inject constructor(
        private val dispatcher: AutoAdsDispatcherProvider,
        private val repository: GraphqlRepository,
        private val rawQueries: Map<String, String>
) : BaseViewModel(dispatcher.ui()) {

    val shopInfoData = MutableLiveData<TopAdsShopInfoData>()

    fun getShopAdsInfo(shopId: Int, onError: (Throwable) -> Unit) {
        launchCatchError(block = {
            val data = withContext(dispatcher.io()) {
                val request = RequestHelper.getGraphQlRequest(rawQueries[RawQueryKeyObject.QUERY_ADS_SHOP_INFO],
                        TopAdsShopInfo.Response::class.java, hashMapOf(SHOP_ID to shopId))
                val cacheStrategy = RequestHelper.getCacheStrategy()
                repository.getReseponse(listOf(request), cacheStrategy)
            }
            data.getSuccessData<TopAdsShopInfo.Response>().shopInfo.data.let {
                shopInfoData.postValue(it)
            }
        }) {
            onError(it)
            it.printStackTrace()
        }
    }

    companion object {
        const val SHOP_ID = "shopId"
    }
}
