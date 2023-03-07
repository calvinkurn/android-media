package com.tokopedia.promocheckout.common.domain.mapper

import com.tokopedia.promocheckout.common.domain.model.CheckVoucherDigitalData
import com.tokopedia.promocheckout.common.domain.model.Message
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.promocheckout.common.view.uimodel.MessageUiModel
import javax.inject.Inject

open class DigitalCheckVoucherMapper @Inject constructor() {

    fun mapData(data: CheckVoucherDigitalData): DataUiModel {
        return DataUiModel(
                success = data.success,
                message = mapMessage(data.message),
                codes = listOf(data.code),
                promoCodeId = data.promoCodeId,
                titleDescription = data.titleDescription,
                discountAmount = data.discountAmount,
                cashbackWalletAmount = data.cashbackAmount,
                invoiceDescription = data.invoiceDescription,
                gatewayId = data.gatewayId,
                isCoupon = data.isCoupon
        )
    }

    private fun mapMessage(message: Message): MessageUiModel {
        return MessageUiModel(
                color = message.color,
                state = message.state,
                text = message.text
        )
    }
}