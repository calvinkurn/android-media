package com.tokopedia.sellerorder.orderextension.presentation.mapper

import com.tokopedia.sellerorder.orderextension.domain.models.SendOrderExtensionRequestResponse
import com.tokopedia.sellerorder.orderextension.presentation.model.OrderExtensionRequestResultUiModel
import com.tokopedia.sellerorder.orderextension.presentation.util.ResourceProvider
import javax.inject.Inject

class OrderExtensionRequestResultResponseMapper @Inject constructor(
    private val resourceProvider: ResourceProvider
) {
    fun mapResponseToUiModel(response: SendOrderExtensionRequestResponse.Data.OrderExtensionRequest.OrderExtensionRequestData): OrderExtensionRequestResultUiModel {
        return OrderExtensionRequestResultUiModel(
            message = response.message.orEmpty(),
            success = response.isSuccess()
        )
    }

    fun mapError(throwable: Throwable): String {
        return resourceProvider.getErrorMessageFromThrowable(throwable)
    }
}