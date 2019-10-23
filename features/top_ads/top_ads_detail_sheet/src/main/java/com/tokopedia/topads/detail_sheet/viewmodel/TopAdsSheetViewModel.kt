package com.tokopedia.topads.detail_sheet.viewmodel

import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.data.model.CacheType
import com.tokopedia.common.network.data.model.RestCacheStrategy
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.network.utils.AuthUtil
import com.tokopedia.topads.detail_sheet.data.Data
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named
import com.tokopedia.topads.detail_sheet.UrlConstant.PATH_TOPADS_GROUP_PRODUCT

/**
 * Author errysuprayogi on 22,October,2019
 */
class TopAdsSheetViewModel @Inject constructor(private val restRepository: RestRepository,
                                               private val userSession: UserSessionInterface,
                                               private val urlMap: Map<String, String>,
                                               @Named("Main")
                                               val dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    fun getAdsProduct(adId: String, startDate: String, endDate: String,
                      onSuccessGetAds: ((Data) -> Unit),
                      onErrorGetAds: ((Throwable) -> Unit)) {
        launchCatchError(
                block = {
                    val result = withContext(Dispatchers.IO){
                        val queryMap = mutableMapOf<String, String>(
                                "ad_id" to adId,
                                "shop_id" to userSession.shopId,
                                "start_date" to startDate,
                                "end_date" to endDate
                        )
                        AuthUtil.generateParamsNetwork(
                                userSession.userId, userSession.deviceId, queryMap)
                        val restRequest = RestRequest.Builder(
                                urlMap[PATH_TOPADS_GROUP_PRODUCT] ?: "",
                                object : TypeToken<DataResponse<List<Data>>>() {}.type)
                                .setQueryParams(queryMap)
                                .setCacheStrategy(RestCacheStrategy.Builder(CacheType.CACHE_FIRST).build())
                                .build()
                        restRepository.getResponse(restRequest)
                    }
                    (result.getData() as DataResponse<List<Data>>).data.get(0)?.let {onSuccessGetAds(it)}
                },
                onError = {
                    onErrorGetAds(it)
                }
        )
    }
}
