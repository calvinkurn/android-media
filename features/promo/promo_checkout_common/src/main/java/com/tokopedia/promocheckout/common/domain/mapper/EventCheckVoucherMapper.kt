package com.tokopedia.promocheckout.common.domain.mapper

import com.tokopedia.promocheckout.common.domain.model.event.EventVerifyResponse
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.promocheckout.common.view.uimodel.MessageUiModel

object EventCheckVoucherMapper {
    fun mapDataEvent(data: EventVerifyResponse): DataUiModel {
        return DataUiModel(
                success = true,
                message = mapMessageEvent(data.data.cart.promocodeSuccessMessage),
                codes = listOf(data.data.cart.promocode),
                discountAmount = data.data.cart.promocodeDiscount,
                cashbackWalletAmount = data.data.cart.promocodeCashback,
                titleDescription = data.data.cart.promocodeSuccessMessage
        )
    }

    private fun mapMessageEvent(message: String): MessageUiModel {
        return MessageUiModel(
                text = message,
                state = "green"
        )
    }
}