package com.tokopedia.topads.detail_sheet.viewmodel

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.data.model.CacheType
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestCacheStrategy
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.topads.detail_sheet.UrlConstant.BASE_REST_URL
import com.tokopedia.topads.detail_sheet.UrlConstant.PARAM_AD_ID
import com.tokopedia.topads.detail_sheet.UrlConstant.PARAM_END_DATE
import com.tokopedia.topads.detail_sheet.UrlConstant.PARAM_SHOP_ID
import com.tokopedia.topads.detail_sheet.UrlConstant.PARAM_START_DATE
import com.tokopedia.topads.detail_sheet.data.Data
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named
import com.tokopedia.topads.detail_sheet.UrlConstant.PATH_TOPADS_GROUP_PRODUCT
import com.tokopedia.topads.detail_sheet.UrlConstant.PATH_BULK_ACTION_PRODUCT_AD
import com.tokopedia.topads.detail_sheet.data.Ad
import com.tokopedia.topads.detail_sheet.data.ActionRequest
import com.tokopedia.topads.detail_sheet.data.Bulk
import java.text.SimpleDateFormat
import java.util.*
import com.tokopedia.authentication.AuthHelper

/**
 * Author errysuprayogi on 22,October,2019
 */
class TopAdsSheetViewModel @Inject constructor(private val restRepository: RestRepository,
                                               private val userSession: UserSessionInterface,
                                               private val urlMap: Map<String, String>,
                                               @Named("Main")
                                               val dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    private val REQUEST_DATE_FORMAT = "yyyy-MM-dd"

    private fun getDate(daysAgo: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)
        return calendar.time
    }

    fun getAdsProduct(adId: String, onSuccessGetAds: ((Data) -> Unit),
                      onErrorGetAds: ((Throwable) -> Unit)) {
        launchCatchError(
                block = {
                    val endDate = SimpleDateFormat(REQUEST_DATE_FORMAT, Locale.ENGLISH).format(getDate(7))
                    val startDate = SimpleDateFormat(REQUEST_DATE_FORMAT, Locale.ENGLISH).format(getDate(0))
                    val result = withContext(Dispatchers.IO) {
                        val queryMap = mutableMapOf<String, String>(
                                PARAM_AD_ID to adId,
                                PARAM_SHOP_ID to userSession.shopId,
                                PARAM_START_DATE to startDate,
                                PARAM_END_DATE to endDate
                        )
                        AuthHelper.generateParamsNetwork(
                                userSession.userId, userSession.deviceId, queryMap)
                        val restRequest = RestRequest.Builder(
                                urlMap[PATH_TOPADS_GROUP_PRODUCT] ?: "",
                                object : TypeToken<DataResponse<List<Data>>>() {}.type)
                                .setQueryParams(queryMap)
                                .build()
                        restRepository.getResponse(restRequest)
                    }
                    (result.getData() as DataResponse<List<Data>>).data.get(0)?.let { onSuccessGetAds(it) }
                },
                onError = {
                    onErrorGetAds(it)
                }
        )
    }

    fun postPromo(action: String, adId: Int, onSuccess: ((Bulk) -> Unit), onError: ((Throwable) -> Unit)) {
        launchCatchError(
                block = {
                    val result = withContext(Dispatchers.IO) {
                        val headers = HashMap<String, String>()
                        headers.put("Content-Type", "application/json")
                        val restRequest = RestRequest.Builder(BASE_REST_URL + PATH_BULK_ACTION_PRODUCT_AD, object : TypeToken<ActionRequest>() {}.type)
                                .setBody(Gson().toJson(generateActionRequest(action, userSession.shopId, adId.toString())))
                                .setHeaders(headers)
                                .setRequestType(RequestType.PATCH)
                                .build()
                        restRepository.getResponse(restRequest)
                    }
                    (result.getData() as ActionRequest).data?.let {
                        onSuccess(it)
                    }
                },
                onError = {
                    onError(it)
                }
        )
    }

    private fun generateActionRequest(action: String, shopId: String, adId: String): ActionRequest {
        return ActionRequest(
                Bulk(action,
                        listOf(Ad(null,
                                null,
                                null,
                                adId,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null)),
                        shopId)
        )
    }
}
