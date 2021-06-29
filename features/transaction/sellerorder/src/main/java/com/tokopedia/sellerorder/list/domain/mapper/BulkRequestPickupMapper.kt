package com.tokopedia.sellerorder.list.domain.mapper

import com.tokopedia.sellerorder.list.domain.model.SomListBulkRequestPickupResponse
import com.tokopedia.sellerorder.list.presentation.models.SomListBulkRequestPickupUiModel
import javax.inject.Inject

class BulkRequestPickupMapper @Inject constructor() {

    fun mapResponseToUiModel(result: SomListBulkRequestPickupResponse.Data.MpLogisticBulkRequestPickup): SomListBulkRequestPickupUiModel {
        return SomListBulkRequestPickupUiModel(
                data = SomListBulkRequestPickupUiModel.Data(
                        jobId = result.jobId,
                        message = result.message,
                        totalOnProcess = result.totalOnProcess
                ),
                errors = result.errors.map {
                    SomListBulkRequestPickupUiModel.ErrorBulkRequestPickup(
                            orderId = it.orderId,
                            message = it.message
                    )
                }
        )
    }
}