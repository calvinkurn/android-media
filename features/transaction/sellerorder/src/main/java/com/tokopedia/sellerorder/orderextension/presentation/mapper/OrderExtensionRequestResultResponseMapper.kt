package com.tokopedia.sellerorder.orderextension.presentation.mapper

import com.tokopedia.sellerorder.orderextension.domain.models.SendOrderExtensionRequestResponse
import com.tokopedia.sellerorder.orderextension.presentation.model.OrderExtensionRequestResultUiModel
import javax.inject.Inject

class OrderExtensionRequestResultResponseMapper @Inject constructor() {
    fun mapResponseToUiModel(response: SendOrderExtensionRequestResponse.Data.OrderExtensionRequest.OrderExtensionRequestData): OrderExtensionRequestResultUiModel {
        return OrderExtensionRequestResultUiModel(
            message = response.message.orEmpty(),
            success = response.isSuccess()
        )
    }
}