package com.tokopedia.product.detail.usecase

import com.google.gson.reflect.TypeToken
import com.tokopedia.authentication.AuthHelper
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.warehouse.model.ProductActionSubmit
import com.tokopedia.usecase.coroutines.UseCase
import java.io.IOException
import javax.inject.Inject

class MoveProductToEtalaseUseCase @Inject constructor(private val restRepository: RestRepository,
                                                      private val rawQuery: Map<String, String>) : UseCase<ProductActionSubmit>() {

    var productId = ""
    var etalaseId: String = ""
    var etalaseName = ""
    var userId = ""
    var deviceId = ""

    fun createParams(productId: String, etalaseId: String?, etalaseName: String,
                     userId: String, deviceId: String) {
        this.productId = productId
        this.etalaseId = if (etalaseId.isNullOrEmpty()) ProductDetailConstant.VALUE_NEW_ETALASE else etalaseId
        this.etalaseName = etalaseName
        this.userId = userId
        this.deviceId = deviceId
    }

    override suspend fun executeOnBackground(): ProductActionSubmit {
        val data: ProductActionSubmit
        val bodyMap = mutableMapOf(
                ProductDetailConstant.PARAM_PRODUCT_ID to productId,
                ProductDetailConstant.PARAM_PRODUCT_ETALASE_ID to etalaseId,
                ProductDetailConstant.PARAM_PRODUCT_ETALASE_NAME to etalaseName)

        AuthHelper.generateParamsNetwork(
                userId, deviceId, bodyMap)
        val restRequest = RestRequest.Builder(
                rawQuery[ProductDetailConstant.PATH_MOVE_TO_ETALASE] ?: "",
                object : TypeToken<DataResponse<ProductActionSubmit>>() {}.type)
                .setRequestType(RequestType.POST)
                .setBody(bodyMap)
                .build()
        data = restRepository.getResponse(restRequest).getData()
        if (!data.getIsSuccess()) {
            throw IOException()
        }

        return data
    }

}