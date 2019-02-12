package com.tokopedia.product.warehouse.view.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.network.utils.AuthUtil
import com.tokopedia.product.detail.data.util.ProductDetailConstant.PARAM_PRODUCT_ETALASE_ID
import com.tokopedia.product.detail.data.util.ProductDetailConstant.PARAM_PRODUCT_ETALASE_NAME
import com.tokopedia.product.detail.data.util.ProductDetailConstant.PARAM_PRODUCT_ID
import com.tokopedia.product.detail.data.util.ProductDetailConstant.PATH_MOVE_TO_ETALASE
import com.tokopedia.product.detail.data.util.ProductDetailConstant.PATH_MOVE_TO_WAREHOUSE
import com.tokopedia.product.detail.data.util.ProductDetailConstant.VALUE_NEW_ETALASE
import com.tokopedia.product.warehouse.model.ProductActionSubmit
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

class ProductWarehouseViewModel @Inject constructor(private val restRepository: RestRepository,
                                                    private val userSession: UserSessionInterface,
                                                    private val urlMap: Map<String, String>,
                                                    @Named("Main")
                                                    val dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    fun moveToWarehouse(productId: String,
                        onSuccessMoveToWarehouse : (()->Unit),
                        onErrorMoveToWarehouse : ((Throwable)->Unit)) {
        launchCatchError(
                block = {
                    val result = withContext(Dispatchers.IO) {
                        val bodyMap = mutableMapOf<String, String>(
                                PARAM_PRODUCT_ID to productId
                        )
                        AuthUtil.generateParamsNetwork(
                                userSession.userId, userSession.deviceId, bodyMap)
                        val restRequest = RestRequest.Builder(
                                urlMap[PATH_MOVE_TO_WAREHOUSE] ?: "",
                                object : TypeToken<DataResponse<ProductActionSubmit>>() {}.type)
                                .setRequestType(RequestType.POST)
                                .setBody(bodyMap)
                                .build()
                        restRepository.getResponse(restRequest)
                    }
                    val dataResponse = (result?.getData() as DataResponse<ProductActionSubmit>).data
                    if (dataResponse.getIsSuccess()) {
                        onSuccessMoveToWarehouse()
                    } else {
                        onErrorMoveToWarehouse(IOException())
                    }
                },
                onError = {
                    onErrorMoveToWarehouse(it)
                }
        )
    }

    fun moveToEtalase(productId: String, etalaseId: String?, etalaseName: String,
                      onSuccessMoveToEtalase : (()->Unit),
                      onErrorMoveToEtalase : ((Throwable)->Unit)) {
        launchCatchError(
                block = {
                    val result = withContext(Dispatchers.IO) {
                        val bodyMap = mutableMapOf(
                                        PARAM_PRODUCT_ID to productId,
                                        if (etalaseId.isNullOrEmpty()) {
                                            PARAM_PRODUCT_ETALASE_ID to VALUE_NEW_ETALASE
                                        } else {
                                            PARAM_PRODUCT_ETALASE_ID to etalaseId
                                        },
                                        PARAM_PRODUCT_ETALASE_NAME to etalaseName)
                        AuthUtil.generateParamsNetwork(
                                userSession.userId, userSession.deviceId, bodyMap)
                        val restRequest = RestRequest.Builder(
                                urlMap[PATH_MOVE_TO_ETALASE] ?: "",
                                object : TypeToken<DataResponse<ProductActionSubmit>>() {}.type)
                                .setRequestType(RequestType.POST)
                                .setBody(bodyMap)
                                .build()
                        restRepository.getResponse(restRequest)
                    }
                    val dataResponse = (result?.getData() as DataResponse<ProductActionSubmit>).data
                    if (dataResponse.getIsSuccess()) {
                        onSuccessMoveToEtalase()
                    } else {
                        onErrorMoveToEtalase(IOException())
                    }
                },
                onError = {
                    onErrorMoveToEtalase(it)
                }
        )
    }


}