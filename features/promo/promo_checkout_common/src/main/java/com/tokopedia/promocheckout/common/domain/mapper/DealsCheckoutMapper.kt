package com.tokopedia.promocheckout.common.domain.mapper

import com.tokopedia.promocheckout.common.domain.model.deals.DataDeals
import com.tokopedia.promocheckout.common.domain.model.deals.Details
import com.tokopedia.promocheckout.common.domain.model.deals.Summaries
import com.tokopedia.promocheckout.common.view.uimodel.BenefitSummaryInfoUiModel
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.promocheckout.common.view.uimodel.MessageUiModel
import com.tokopedia.promocheckout.common.view.uimodel.SummariesUiModel
import com.tokopedia.promocheckout.common.view.uimodel.DetailUiModel

object DealsCheckoutMapper {

    const val CASHBACK = "cashback"
    const val CASHBACK_VALUE = 0L


    fun mapDataNew(data: DataDeals): DataUiModel {
        return DataUiModel(
                success = data.global_success,
                message = mapMessageDeals(data.usage_details.firstOrNull()?.message?.text ?: ""),
                codes = listOf(data.usage_details.firstOrNull()?.code ?: ""),
                promoCodeId = data.promo_code_id,
                titleDescription = data.benefit_summary_info.final_benefit_text,
                discountAmount = if (data.usage_details.firstOrNull()?.benefit_summary?.firstOrNull()?.
                        benefit_details?.firstOrNull()?.benefit_type?.equals(CASHBACK, true) ?: false)
                            CASHBACK_VALUE else data.benefit_summary_info.final_benefit_amount,
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