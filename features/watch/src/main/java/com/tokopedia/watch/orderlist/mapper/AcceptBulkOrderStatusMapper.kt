package com.tokopedia.watch.orderlist.mapper

import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.watch.orderlist.model.SomListAcceptBulkOrderStatusUiModel
import com.tokopedia.watch.orderlist.model.SomListGetAcceptBulkOrderStatusResponse
import javax.inject.Inject

class AcceptBulkOrderStatusMapper @Inject constructor() {
    fun mapResponseToUiModel(result: SomListGetAcceptBulkOrderStatusResponse.Data.GetMultiAcceptOrderStatus): SomListAcceptBulkOrderStatusUiModel {
        return SomListAcceptBulkOrderStatusUiModel(
                data = SomListAcceptBulkOrderStatusUiModel.Data(
                        multiOriginInvalidOrder = result.data.multiOriginInvalidOrder.map {
                            SomListAcceptBulkOrderStatusUiModel.Data.MultiOriginInvalidOrder(
                                    orderId = it.orderId.toLongOrZero(),
                                    invoiceRefNum = it.invoiceRefNum
                            )
                        },
                        success = result.data.success.toIntOrZero(),
                        fail = result.data.fail.toIntOrZero(),
                        totalOrder = result.data.totalOrder.toIntOrZero()
                ),
                errors = result.errors.map {
                    SomListAcceptBulkOrderStatusUiModel.Error(
                            code = it.code,
                            status = it.status,
                            title = it.title,
                            detail = it.detail
                    )
                }
        )
    }
}