package com.tokopedia.sellerorder.list.domain.mapper

import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.sellerorder.list.domain.model.SomListGetMultiShippingResponse
import com.tokopedia.sellerorder.list.presentation.models.MultiShippingStatusUiModel
import javax.inject.Inject

class MultiShippingStatusMapper @Inject constructor() {

    fun mapResponseToUiModel(response: SomListGetMultiShippingResponse.Data.MpLogisticMultiShippingStatus): MultiShippingStatusUiModel {
        return MultiShippingStatusUiModel(
                total_order = response.totalOrder.toLongOrZero(),
                processed = response.processed.toLongOrZero(),
                success = response.success.toLongOrZero(),
                fail = response.fail.toLongOrZero(),
                listFail = response.listFail,
                listError = response.listError.map {
                    MultiShippingStatusUiModel.ErrorMultiShippingStatusUiModel(
                            message = it.message,
                            orderId = it.orderId
                    )
                }
        )
    }
}