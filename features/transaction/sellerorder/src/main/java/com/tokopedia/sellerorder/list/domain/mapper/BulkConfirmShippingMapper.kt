package com.tokopedia.sellerorder.list.domain.mapper

import com.tokopedia.sellerorder.list.domain.model.SomListBulkConfirmShippingResponse
import com.tokopedia.sellerorder.list.presentation.models.SomListBulkConfirmShippingUiModel
import javax.inject.Inject

class BulkConfirmShippingMapper @Inject constructor() {

    fun mapResponseToUiModel(result: SomListBulkConfirmShippingResponse.Data.MpLogisticBulkConfirmShipping): SomListBulkConfirmShippingUiModel {
        return SomListBulkConfirmShippingUiModel(
                data = SomListBulkConfirmShippingUiModel.Data(
                        jobId = result.jobId,
                        message = result.message,
                        totalOnProcess = result.totalOnProcess
                ),
                errors = result.errors.map {
                    SomListBulkConfirmShippingUiModel.ErrorBulkConfirmShipping(
                            orderId = it.orderId,
                            message = it.message
                    )
                }
        )
    }
}