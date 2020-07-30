package com.tokopedia.promocheckout.common.domain.mapper

import com.tokopedia.promocheckout.common.domain.model.CheckUmrahPromoCode
import com.tokopedia.promocheckout.common.domain.model.CheckUmrahPromoCodeData
import com.tokopedia.promocheckout.common.domain.model.Message
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.promocheckout.common.view.uimodel.MessageUiModel
import javax.inject.Inject

open class UmrahCheckPromoMapper @Inject constructor() {

    fun mapData(data: CheckUmrahPromoCodeData): DataUiModel {
        return DataUiModel(
                success = data.success,
                message = mapMessage(data.message),
                codes = listOf(data.codes),
                promoCodeId = data.promoCodeId,
                titleDescription = data.titleDescription,
                discountAmount = data.discountAmount,
                cashbackWalletAmount = data.cashbackWalletAmount,
                invoiceDescription = data.invoiceDescription,
                gatewayId = data.gatewayId.toString(),
                isCoupon = mapIsCoupon(data.isCoupon)
        )
    }

    private fun mapMessage(message: Message): MessageUiModel {
        return MessageUiModel(
                color = message.color,
                state = message.state,
                text = message.text
        )
    }

    private fun mapIsCoupon(isCoupon: Boolean): Int = if (isCoupon) 1 else 0

}