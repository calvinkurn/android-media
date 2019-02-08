package com.tokopedia.product.detail.view.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.coroutines.repository.RestRepositoryImpl
import com.tokopedia.common.network.data.model.CacheType
import com.tokopedia.common.network.data.model.RestCacheStrategy
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.network.utils.AuthUtil
import com.tokopedia.product.detail.constant.Constant
import com.tokopedia.product.detail.constant.Constant.PARAM_PRODUCT_ID
import com.tokopedia.product.detail.data.model.product.ProductInfo
import com.tokopedia.product.detail.data.model.reporttype.ReportType
import com.tokopedia.product.detail.data.model.reporttype.ReportTypeModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.withContext
import okhttp3.internal.cache.CacheStrategy
import javax.inject.Inject
import javax.inject.Named

class ProductReportViewModel @Inject constructor(private val restRepository: RestRepository,
                                                 private val userSession: UserSessionInterface,
                                                 private val urlMap: Map<String, String>,
                                                 @Named("Main")
                                                 val dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val productReportTypeResp = MutableLiveData<Result<List<ReportType>>>()

    fun getProductReportType(productId: String) {
        launchCatchError(
                block = {
                    val result = withContext(Dispatchers.IO) {
                        val queryMap = mutableMapOf<String, String>(
                                PARAM_PRODUCT_ID to productId
                        )
                        AuthUtil.generateParamsNetwork(
                                userSession.userId, userSession.deviceId, queryMap)
                        val restRequest = RestRequest.Builder(
                                urlMap[Constant.PATH_REPORT_TYPE] ?: "",
                                object : TypeToken<DataResponse<ReportTypeModel>>() {}.type)
                                .setQueryParams(queryMap)
                                .setCacheStrategy(RestCacheStrategy.Builder(CacheType.CACHE_FIRST).build()) //TODO use cache first
                                .build()
                        restRepository.getResponse(restRequest)
                    }
                    productReportTypeResp.value = Success((result?.getData() as DataResponse<ReportTypeModel>).data.reportType)
                },
                onError = {
                    productReportTypeResp.value = Fail(it)
                }
        )
    }

    fun reportProduct() {

    }

    companion object {

    }

}