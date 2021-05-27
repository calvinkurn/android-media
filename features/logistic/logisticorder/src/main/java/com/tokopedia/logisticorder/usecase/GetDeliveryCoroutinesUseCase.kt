package com.tokopedia.logisticorder.usecase

import com.tokopedia.logisticorder.domain.response.GetDeliveryImageResponse
import com.tokopedia.logisticorder.domain.service.GetDeliveryImageApi
import com.tokopedia.logisticorder.domain.service.GetDeliveryImageRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import okhttp3.RequestBody
import retrofit2.http.Url
import javax.inject.Inject

class GetDeliveryCoroutinesUseCase @Inject constructor(
        private val getDeliveryImageRepository: GetDeliveryImageRepository
) {

    /*suspend fun execute(url: String): GetDeliveryImageResponse {
        return getDeliveryImageRepository.getDeliveryImage(url)
    }*/

    suspend fun executeParams(requestParams: RequestParams): GetDeliveryImageResponse {
        return getDeliveryImageRepository.getDeliveryImage(requestParams.parameters)
    }

    fun getParams(imageId: String, orderId: Long, size: String, userId: String, osType: Int, deviceId: String): RequestParams {

        val params = RequestParams.create()
        params.putString(KEY_IMAGE_ID, imageId)
        params.putLong(KEY_ORDER_ID, orderId)
        params.putString(KEY_SIZE, size)
        params.putString(KEY_USER_ID, userId)
        params.putInt(KEY_OS_TYPE, osType)
        params.putString(KEY_DEVICE_ID, deviceId)

        return params
    }

    companion object {

        private const val KEY_IMAGE_ID = "image_id"
        private const val KEY_ORDER_ID= "order_id"
        private const val KEY_SIZE = "size"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_OS_TYPE = "os_type"
        private const val KEY_DEVICE_ID = "device_id"
        private const val PARAM_MAPS = "maps"
    }
}