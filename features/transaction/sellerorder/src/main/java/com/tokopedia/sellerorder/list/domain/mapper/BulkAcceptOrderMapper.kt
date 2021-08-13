package com.tokopedia.sellerorder.list.domain.mapper

import com.tokopedia.sellerorder.list.domain.model.SomListBulkAcceptOrderResponse
import com.tokopedia.sellerorder.list.presentation.models.Error
import com.tokopedia.sellerorder.list.presentation.models.SomListBulkAcceptOrderUiModel
import javax.inject.Inject

class BulkAcceptOrderMapper @Inject constructor() {
    fun mapResponseToUiModel(result: SomListBulkAcceptOrderResponse.Data): SomListBulkAcceptOrderUiModel {
        return SomListBulkAcceptOrderUiModel(
                data = SomListBulkAcceptOrderUiModel.Data(
                        batchId = result.multiAcceptOrder.data.batchId,
                        message = result.multiAcceptOrder.data.message,
                        totalOrder = result.multiAcceptOrder.data.totalOrder
                ),
                errors = result.multiAcceptOrder.errors.map {
                    Error(
                            code = it.code,
                            status = it.status,
                            title = it.title,
                            detail = it.detail
                    )
                }
        )
    }
}