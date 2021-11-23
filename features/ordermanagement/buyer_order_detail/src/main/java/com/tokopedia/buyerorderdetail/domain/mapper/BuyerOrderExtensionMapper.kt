package com.tokopedia.buyerorderdetail.domain.mapper

import com.tokopedia.buyerorderdetail.domain.models.OrderExtensionRespondInfoResponse
import com.tokopedia.buyerorderdetail.domain.models.OrderExtensionRespondResponse
import com.tokopedia.buyerorderdetail.presentation.model.OrderExtensionRespondInfoUiModel
import com.tokopedia.buyerorderdetail.presentation.model.OrderExtensionRespondUiModel
import javax.inject.Inject

class BuyerOrderExtensionMapper @Inject constructor() {

    fun mapToOrderExtensionRespondInfo(
        data: OrderExtensionRespondInfoResponse.OrderExtensionRespondInfo,
        orderId: String
    ) = OrderExtensionRespondInfoUiModel(
        orderId = orderId,
        confirmationTitle = data.text,
        reasonExtension = data.reason,
        rejectText = data.rejectText,
        newDeadline = data.newDeadline,
        messageCode = data.messageCode,
        message = data.message
    )

    fun mapToOrderExtensionRespond(
        data: OrderExtensionRespondResponse.Data,
        actionType: Int
    ) = OrderExtensionRespondUiModel(
        message = data.message,
        messageCode = data.messageCode,
        actionType = actionType
    )
}