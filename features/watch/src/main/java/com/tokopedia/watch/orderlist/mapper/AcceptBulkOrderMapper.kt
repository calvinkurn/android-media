package com.tokopedia.watch.orderlist.mapper

import com.tokopedia.watch.orderlist.model.SomListAcceptBulkOrderResponse
import com.tokopedia.watch.orderlist.model.SomListAcceptBulkOrderUiModel
import javax.inject.Inject

class AcceptBulkOrderMapper @Inject constructor() {
    fun mapResponseToUiModel(result: SomListAcceptBulkOrderResponse.Data): SomListAcceptBulkOrderUiModel {
        return SomListAcceptBulkOrderUiModel(
                data = SomListAcceptBulkOrderUiModel.Data(
                        batchId = result.multiAcceptOrder.data.batchId,
                        message = result.multiAcceptOrder.data.message,
                        totalOrder = result.multiAcceptOrder.data.totalOrder
                ),
                errors = result.multiAcceptOrder.errors.map {
                    SomListAcceptBulkOrderUiModel.Error(
                            code = it.code,
                            status = it.status,
                            title = it.title,
                            detail = it.detail
                    )
                }
        )
    }
}