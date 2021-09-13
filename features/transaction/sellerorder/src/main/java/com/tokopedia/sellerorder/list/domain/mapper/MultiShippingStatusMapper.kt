package com.tokopedia.sellerorder.list.domain.mapper

import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.sellerorder.list.domain.model.SomListGetMultiShippingResponse
import com.tokopedia.sellerorder.list.presentation.models.MultiShippingStatusUiModel
import javax.inject.Inject

class MultiShippingStatusMapper @Inject constructor() {

    fun mapResponseToUiModel(response: SomListGetMultiShippingResponse.Data): MultiShippingStatusUiModel {
        val multiShippingResponse = response.mpLogisticMultiShippingStatus
        return MultiShippingStatusUiModel(
                total_order = multiShippingResponse.totalOrder.toLongOrZero(),
                processed = multiShippingResponse.processed.toLongOrZero(),
                success = multiShippingResponse.success.toLongOrZero(),
                fail = multiShippingResponse.fail.toLongOrZero(),
                listFail = multiShippingResponse.listFail,
                listError = multiShippingResponse.listError.map {
                    MultiShippingStatusUiModel.ErrorMultiShippingStatusUiModel(
                            message = it.message,
                            orderId = it.orderId
                    )
                }
        )
    }
}