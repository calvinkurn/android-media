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

class MoveProductToWarehouseUseCase @Inject constructor(private val restRepository: RestRepository,
                                                        private val rawQuery: Map<String, String>) : UseCase<ProductActionSubmit>() {

    var productId = ""
    var userId = ""
    var deviceId = ""

    fun createParams(productId: String, userId: String, deviceId: String) {
        this.productId = productId
        this.userId = userId
        this.deviceId = deviceId
    }

    override suspend fun executeOnBackground(): ProductActionSubmit {
        val data: ProductActionSubmit
        val bodyMap = mutableMapOf(
                ProductDetailConstant.PARAM_PRODUCT_ID to productId
        )
        AuthHelper.generateParamsNetwork(
                userId, deviceId, bodyMap)
        val restRequest = RestRequest.Builder(
                rawQuery[ProductDetailConstant.PATH_MOVE_TO_WAREHOUSE] ?: "",
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