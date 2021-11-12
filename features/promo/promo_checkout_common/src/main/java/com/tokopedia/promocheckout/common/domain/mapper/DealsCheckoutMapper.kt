package com.tokopedia.promocheckout.common.domain.mapper

import com.tokopedia.promocheckout.common.domain.model.deals.DataDeals
import com.tokopedia.promocheckout.common.domain.model.deals.DealsPromoCheckResponse
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

    fun mapDataNew(data: DataDeals): DataUiModel {
        return DataUiModel(
                success = data.global_success,
                message = mapMessageDeals(data.usage_details.firstOrNull()?.message?.text ?: ""),
                codes = listOf(data.usage_details.firstOrNull()?.code ?: ""),
                promoCodeId = data.promo_code_id.toInt(),
                discountAmount = data.benefit_summary_info.final_benefit_amount,
                titleDescription = data.benefit_summary_info.final_benefit_text,
                gatewayId = data.gateway_id,
        )
    }

    private fun mapMessageDeals(message: String): MessageUiModel {
        return MessageUiModel(
                text = message,
                state = "green"
        )
    }
}