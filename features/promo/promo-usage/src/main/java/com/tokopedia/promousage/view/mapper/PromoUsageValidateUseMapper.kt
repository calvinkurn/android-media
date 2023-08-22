package com.tokopedia.promousage.view.mapper

import com.tokopedia.kotlin.extensions.view.ifNullOrBlank
import com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse.PromoValidateUseResponse
import com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse.UserGroupMetadata
import com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse.ValidateUseResponse
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.AdditionalInfoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.BenefitSummaryInfoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.DetailsItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.EmptyCartInfoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ErrorDetailUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.MessageInfoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.MessageUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.MvcShippingBenefitUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoCheckoutVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoSpIdUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.SummariesItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.TickerInfoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.TrackingDetailsItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.UsageSummariesUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import javax.inject.Inject

internal class PromoUsageValidateUseMapper @Inject constructor() {

    fun mapToValidateUseResponse(
        response: ValidateUseResponse
    ): ValidateUsePromoRevampUiModel {
        return ValidateUsePromoRevampUiModel(
            code = response.validateUsePromoRevamp.code,
            status = response.validateUsePromoRevamp.status,
            message = response.validateUsePromoRevamp.message,
            errorCode = response.validateUsePromoRevamp.errorCode,
            promoUiModel = mapValidateUsePromo(response.validateUsePromoRevamp.promo)
        )
    }

    private fun mapValidateUsePromo(promo: PromoValidateUseResponse): PromoUiModel {
        return PromoUiModel(
            globalSuccess = promo.globalSuccess,
            success = promo.success,
            codes = promo.codes,
            messageUiModel = MessageUiModel(
                color = promo.message.color,
                state = promo.message.state,
                text = promo.message.text
            ),
            additionalInfoUiModel = AdditionalInfoUiModel(
                messageInfoUiModel = MessageInfoUiModel(
                    detail = promo.additionalInfo.messageInfo.detail,
                    message = promo.additionalInfo.messageInfo.message
                ),
                errorDetailUiModel = ErrorDetailUiModel(
                    message = promo.additionalInfo.errorDetail.message
                ),
                emptyCartInfoUiModel = EmptyCartInfoUiModel(
                    detail = promo.additionalInfo.emptyCartInfo.detail,
                    imgUrl = promo.additionalInfo.emptyCartInfo.imageUrl,
                    message = promo.additionalInfo.emptyCartInfo.message
                ),
                usageSummariesUiModel = promo.additionalInfo.usageSummaries.map { usageSummary ->
                    UsageSummariesUiModel(
                        desc = usageSummary.description,
                        type = usageSummary.type,
                        amountStr = usageSummary.amountString,
                        amount = usageSummary.amount,
                        currencyDetailStr = usageSummary.currencyDetailsStr
                    )
                },
                promoSpIds = promo.additionalInfo.promoSpIds.map { promoSpId ->
                    PromoSpIdUiModel(
                        mvcShippingBenefits = promoSpId.mvcShippingBenefits.map { benefit ->
                            MvcShippingBenefitUiModel(
                                benefitAmount = benefit.benefitAmount,
                                spId = benefit.spId
                            )
                        },
                        uniqueId = promoSpId.uniqueId,
                    )
                },
                pomlAutoApplied = promo.additionalInfo.pomlAutoApplied
            ),
            benefitSummaryInfoUiModel = BenefitSummaryInfoUiModel(
                finalBenefitText = promo.benefitSummaryInfo.finalBenefitText,
                finalBenefitAmountStr = promo.benefitSummaryInfo.finalBenefitAmountStr,
                finalBenefitAmount = promo.benefitSummaryInfo.finalBenefitAmount,
                summaries = promo.benefitSummaryInfo.summaries.map { summary ->
                    SummariesItemUiModel(
                        description = summary.description,
                        type = summary.type,
                        amount = summary.amount,
                        amountStr = summary.amountStr,
                        details = summary.details.map { detail ->
                            DetailsItemUiModel(
                                description = detail.description,
                                amountStr = detail.amountStr,
                                amount = detail.amount,
                                type = detail.type
                            )
                        }
                    )
                }
            ),
            voucherOrderUiModels = promo.voucherOrders.map { voucherOrder ->
                PromoCheckoutVoucherOrdersItemUiModel(
                    success = voucherOrder.success,
                    code = voucherOrder.code,
                    type = voucherOrder.type,
                    uniqueId = voucherOrder.uniqueId,
                    messageUiModel = MessageUiModel(
                        color = voucherOrder.message.color,
                        state = voucherOrder.message.state,
                        text = voucherOrder.message.text
                    ),
                    shippingId = voucherOrder.shippingId,
                    spId = voucherOrder.spId,
                    cartStringGroup = voucherOrder.cartStringGroup
                )
            },
            tickerInfoUiModel = TickerInfoUiModel(
                uniqueId = promo.tickerInfo.uniqueId,
                statusCode = promo.tickerInfo.statusCode,
                message = promo.tickerInfo.message,
            ),
            trackingDetailUiModels = promo.trackingDetails.map {
                TrackingDetailsItemUiModel(
                    promoDetailsTracking = it.promoDetailsTracking,
                    productId = it.productId,
                    promoCodesTracking = it.promoCodesTracking,
                )
            }
        )
    }
}
