package com.tokopedia.product.report.view.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.data.model.CacheType
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestCacheStrategy
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.network.utils.AuthUtil
import com.tokopedia.product.detail.data.util.ProductDetailConstant.PARAM_PRODUCT_ID
import com.tokopedia.product.detail.data.util.ProductDetailConstant.PARAM_REPORT_TYPE
import com.tokopedia.product.detail.data.util.ProductDetailConstant.PARAM_TEXT_MESSAGE
import com.tokopedia.product.detail.data.util.ProductDetailConstant.PATH_REPORT
import com.tokopedia.product.detail.data.util.ProductDetailConstant.PATH_REPORT_TYPE
import com.tokopedia.product.report.model.reportSubmit.ReportSubmit
import com.tokopedia.product.report.model.reportType.ReportType
import com.tokopedia.product.report.model.reportType.ReportTypeModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Named

class ProductReportViewModel @Inject constructor(private val restRepository: RestRepository,
                                                 private val userSession: UserSessionInterface,
                                                 private val urlMap: Map<String, String>,
                                                 @Named("Main")
                                                 val dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    fun getProductReportType(productId: String, onSuccessGetReportType : ((List<ReportType>)->Unit),
                             onErrorGetReportType: ((Throwable) -> Unit)) {
        launchCatchError(
                block = {
                    val result = withContext(Dispatchers.IO) {
                        val queryMap = mutableMapOf<String, String>(
                                PARAM_PRODUCT_ID to productId
                        )
                        AuthUtil.generateParamsNetwork(
                                userSession.userId, userSession.deviceId, queryMap)
                        val restRequest = RestRequest.Builder(
                                urlMap[PATH_REPORT_TYPE] ?: "",
                                object : TypeToken<DataResponse<ReportTypeModel>>() {}.type)
                                .setQueryParams(queryMap)
                                .setCacheStrategy(RestCacheStrategy.Builder(CacheType.CACHE_FIRST).build())
                                .build()
                        restRepository.getResponse(restRequest)
                    }
                    onSuccessGetReportType((result.getData() as DataResponse<ReportTypeModel>).data.reportType)
                },
                onError = {
                    onErrorGetReportType(it)
                }
        )
    }

    fun reportProduct(productId: String, reportType: String, reportDesc: String,
                      onSuccessReportProduct : (()->Unit),
                      onErrorReportProduct: ((Throwable) -> Unit)) {
        launchCatchError(
                block = {
                    val result = withContext(Dispatchers.IO) {
                        val bodyMap = AuthUtil.generateParamsNetwork(
                                userSession.userId, userSession.deviceId, mutableMapOf(
                                PARAM_REPORT_TYPE to reportType,
                                PARAM_PRODUCT_ID to productId,
                                PARAM_TEXT_MESSAGE to reportDesc
                        ))
                        val restRequest = RestRequest.Builder(
                                urlMap[PATH_REPORT] ?: "",
                                object : TypeToken<DataResponse<ReportSubmit>>() {}.type)
                                .setRequestType(RequestType.POST)
                                .setBody(bodyMap)
                                .build()
                        restRepository.getResponse(restRequest)
                    }
                    val reportSubmit = (result.getData() as DataResponse<ReportSubmit>).data
                    if (reportSubmit.getIsSuccess()) {
                        onSuccessReportProduct()
                    } else {
                        onErrorReportProduct(IOException())
                    }
                },
                onError = {
                    onErrorReportProduct(it)
                }
        )
    }


}