package com.tokopedia.promocheckout.common.domain.mapper

import com.tokopedia.promocheckout.common.domain.model.deals.*
import com.tokopedia.promocheckout.common.view.uimodel.*

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
                titleDescription = data.benefit_summary_info.final_benefit_text,
                discountAmount = data.benefit_summary_info.final_benefit_amount,
                isCoupon = if (data.usage_details.firstOrNull()?.promo_detail?.is_coupon ?: false) 1 else 0,
                gatewayId = data.gateway_id,
                benefit = BenefitSummaryInfoUiModel(data.benefit_summary_info.final_benefit_text,
                        data.benefit_summary_info.final_benefit_amount_str,
                        data.benefit_summary_info.final_benefit_amount,
                        mapSummaryBenefit(data.benefit_summary_info.summaries)
                )

        )
    }

    private fun mapMessageDeals(message: String): MessageUiModel {
        return MessageUiModel(
                text = message,
                state = "green"
        )
    }

    private fun mapSummaryBenefit(listSummaries: List<Summaries>): List<SummariesUiModel>{
        return listSummaries.map {
            SummariesUiModel(
                    it.description,
                    it.type,
                    it.amount_str,
                    it.amount,
                    mapDetails(it.details)
            )
        }
    }

    private fun mapDetails(listDetail: List<Details>): ArrayList<DetailUiModel>{
        val listDetailUi = listDetail.map {
            DetailUiModel(
                   it.description,
                   it.type,
                   it.amount_str,
                   it.amount
            )
        }
        return ArrayList(listDetailUi)
    }
}