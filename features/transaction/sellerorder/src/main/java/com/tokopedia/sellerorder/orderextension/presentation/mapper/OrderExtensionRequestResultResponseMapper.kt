package com.tokopedia.sellerorder.orderextension.presentation.mapper

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.sellerorder.orderextension.domain.models.SendOrderExtensionRequestResponse
import com.tokopedia.sellerorder.orderextension.presentation.model.OrderExtensionRequestResultUiModel
import javax.inject.Inject

class OrderExtensionRequestResultResponseMapper @Inject constructor(
    @ApplicationContext
    private val context: Context
) {
    fun mapResponseToUiModel(response: SendOrderExtensionRequestResponse.Data.OrderExtensionRequest.OrderExtensionRequestData): OrderExtensionRequestResultUiModel {
        return OrderExtensionRequestResultUiModel(
            message = response.message.orEmpty(),
            success = response.isSuccess()
        )
    }

    fun mapError(throwable: Throwable): String {
        return ErrorHandler.getErrorMessage(context, throwable)
    }
}