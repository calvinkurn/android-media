package com.tokopedia.sellerorder.list.domain.mapper

import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.sellerorder.list.domain.model.SomListGetBulkAcceptOrderStatusResponse
import com.tokopedia.sellerorder.list.presentation.models.Error
import com.tokopedia.sellerorder.list.presentation.models.SomListBulkAcceptOrderStatusUiModel
import javax.inject.Inject

class BulkAcceptOrderStatusMapper @Inject constructor() {
    fun mapResponseToUiModel(result: SomListGetBulkAcceptOrderStatusResponse.Data.GetMultiAcceptOrderStatus): SomListBulkAcceptOrderStatusUiModel {
        return SomListBulkAcceptOrderStatusUiModel(
                data = SomListBulkAcceptOrderStatusUiModel.Data(
                        multiOriginInvalidOrder = result.data.multiOriginInvalidOrder.map {
                            SomListBulkAcceptOrderStatusUiModel.Data.MultiOriginInvalidOrder(
                                    orderId = it.orderId.toIntOrZero(),
                                    invoiceRefNum = it.invoiceRefNum
                            )
                        },
                        success = result.data.success.toIntOrZero(),
                        fail = result.data.fail.toIntOrZero(),
                        totalOrder = result.data.totalOrder.toIntOrZero()
                ),
                errors = result.errors.map {
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