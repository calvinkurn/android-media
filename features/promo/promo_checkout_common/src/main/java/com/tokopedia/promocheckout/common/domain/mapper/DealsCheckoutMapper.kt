package com.tokopedia.promocheckout.common.domain.mapper

import com.tokopedia.promocheckout.common.domain.model.deals.DealsVerifyResponse
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.promocheckout.common.view.uimodel.MessageUiModel

object DealsCheckoutMapper {

    fun mapData(data: DealsVerifyResponse): DataUiModel {
        return DataUiModel(
                success = true,
                message = mapMessageDeals(data.data.cart.promocodeSuccessMessage),
                codes = listOf(data.data.cart.promocode),
                discountAmount = data.data.cart.promocodeDiscount,
                cashbackWalletAmount = data.data.cart.promocodeCashback,
                titleDescription = data.data.cart.promocodeSuccessMessage
        )
    }

    private fun mapMessageDeals(message: String): MessageUiModel {
        return MessageUiModel(
                text = message,
                state = "green"
        )
    }
}