package com.tokopedia.purchase_platform.common.feature.promo.view.mapper

import com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse.AdditionalInfo
import com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse.BenefitSummaryInfo
import com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse.DetailsItem
import com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse.Message
import com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse.PromoValidateUseResponse
import com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse.SummariesItem
import com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse.TickerInfo
import com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse.TrackingDetailsItem
import com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse.UsageSummaries
import com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse.ValidateUsePromoRevamp
import com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse.VoucherOrdersItem
import com.tokopedia.purchase_platform.common.feature.promo.domain.model.PromoSpId
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.AdditionalInfoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.BenefitSummaryInfoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.DetailsItemUiModel
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

/**
 * Created by fwidjaja on 2020-03-05.
 */
class ValidateUsePromoCheckoutMapper {

    companion object {
        fun mapToValidateUseRevampPromoUiModel(validateUsePromoRevamp: ValidateUsePromoRevamp): ValidateUsePromoRevampUiModel {
            return ValidateUsePromoRevampUiModel(
                status = validateUsePromoRevamp.status,
                message = validateUsePromoRevamp.message,
                errorCode = validateUsePromoRevamp.errorCode,
                promoUiModel = mapToPromoUiModel(validateUsePromoRevamp.promo)
            )
        }

        private fun mapToPromoUiModel(promo: PromoValidateUseResponse): PromoUiModel {
            return PromoUiModel(
                globalSuccess = promo.globalSuccess,
                success = promo.success,
                codes = mapCodes(promo.codes),
                messageUiModel = mapMessageUiModel(promo.message),
                additionalInfoUiModel = mapToAdditionalInfoUiModel(promo.additionalInfo),
                benefitSummaryInfoUiModel = mapToBenefitSummaryInfoUiModel(promo.benefitSummaryInfo),
                voucherOrderUiModels = ArrayList(mapListVoucherOrders(promo.voucherOrders)),
                tickerInfoUiModel = mapTickerInfoUiModel(promo.tickerInfo),
                trackingDetailUiModels = mapTrackingDetails(promo.trackingDetails),
                userGroupMetadata = promo.userGroupMetadata
            )
        }

        private fun mapTrackingDetails(trackingDetails: List<TrackingDetailsItem>): List<TrackingDetailsItemUiModel> {
            return trackingDetails.map {
                TrackingDetailsItemUiModel().apply {
                    promoDetailsTracking = it.promoDetailsTracking
                    productId = it.productId
                    promoCodesTracking = it.promoCodesTracking
                }
            }
        }

        private fun mapTickerInfoUiModel(tickerInfo: TickerInfo): TickerInfoUiModel {
            val tickerInfoUiModel = TickerInfoUiModel()
            tickerInfoUiModel.let {
                it.uniqueId = tickerInfo.uniqueId
                it.statusCode = tickerInfo.statusCode
                it.message = tickerInfo.message
            }

            return tickerInfoUiModel
        }

        private fun mapCodes(codes: List<String>): ArrayList<String> {
            return ArrayList(codes)
        }

        private fun mapMessageUiModel(messageData: Message): MessageUiModel {
            return MessageUiModel(
                color = messageData.color,
                state = messageData.state,
                text = messageData.text
            )
        }

        private fun mapListVoucherOrders(voucherOrders: List<VoucherOrdersItem>): List<PromoCheckoutVoucherOrdersItemUiModel> {
            return voucherOrders.map { mapToVoucherOrdersItemUiModel(it) }
        }

        private fun mapToVoucherOrdersItemUiModel(voucherOrdersItem: VoucherOrdersItem): PromoCheckoutVoucherOrdersItemUiModel {
            return PromoCheckoutVoucherOrdersItemUiModel(
                success = voucherOrdersItem.success,
                code = voucherOrdersItem.code,
                type = voucherOrdersItem.type,
                uniqueId = voucherOrdersItem.uniqueId,
                messageUiModel = mapMessageUiModel(voucherOrdersItem.message),
                shippingId = voucherOrdersItem.shippingId,
                spId = voucherOrdersItem.spId,
                cartStringGroup = voucherOrdersItem.cartStringGroup
            )
        }

        private fun mapToAdditionalInfoUiModel(additionalInfo: AdditionalInfo): AdditionalInfoUiModel {
            val additionalInfoUiModel = AdditionalInfoUiModel()
            additionalInfo.messageInfo.let {
                additionalInfoUiModel.messageInfoUiModel.message = it.message
                additionalInfoUiModel.messageInfoUiModel.detail = it.detail
            }
            additionalInfo.errorDetail.let {
                additionalInfoUiModel.errorDetailUiModel.message = it.message
            }
            additionalInfo.emptyCartInfo.let {
                additionalInfoUiModel.emptyCartInfoUiModel.detail = it.detail
                additionalInfoUiModel.emptyCartInfoUiModel.imgUrl = it.imageUrl
                additionalInfoUiModel.emptyCartInfoUiModel.message = it.message
            }
            val listUsageSummariesUiModel = arrayListOf<UsageSummariesUiModel>()
            additionalInfo.usageSummaries.forEach {
                listUsageSummariesUiModel.add(mapToUsageSummariesUiModel(it))
            }
            additionalInfoUiModel.usageSummariesUiModel = listUsageSummariesUiModel
            val promoSpIdUiModels = arrayListOf<PromoSpIdUiModel>()
            additionalInfo.promoSpIds.forEach {
                promoSpIdUiModels.add(mapPromoSpId(it))
            }
            additionalInfoUiModel.promoSpIds = promoSpIdUiModels
            additionalInfoUiModel.pomlAutoApplied = additionalInfo.pomlAutoApplied
            return additionalInfoUiModel
        }

        private fun mapPromoSpId(promoSpId: PromoSpId): PromoSpIdUiModel {
            return PromoSpIdUiModel().apply {
                val mvcShippingBenefitUiModels = arrayListOf<MvcShippingBenefitUiModel>()
                promoSpId.mvcShippingBenefits.forEach {
                    mvcShippingBenefitUiModels.add(
                        MvcShippingBenefitUiModel().apply {
                            benefitAmount = it.benefitAmount
                            spId = it.spId
                        }
                    )
                }
                mvcShippingBenefits = mvcShippingBenefitUiModels
                uniqueId = promoSpId.uniqueId
            }
        }

        private fun mapToUsageSummariesUiModel(usageSummaries: UsageSummaries): UsageSummariesUiModel {
            return UsageSummariesUiModel(
                desc = usageSummaries.description,
                type = usageSummaries.type,
                amountStr = usageSummaries.amountString,
                amount = usageSummaries.amount,
                currencyDetailStr = usageSummaries.currencyDetailsStr
            )
        }

        private fun mapToBenefitSummaryInfoUiModel(benefit: BenefitSummaryInfo): BenefitSummaryInfoUiModel {
            val benefitSummaryInfoUiModel = BenefitSummaryInfoUiModel().apply {
                finalBenefitText = benefit.finalBenefitText
                finalBenefitAmountStr = benefit.finalBenefitAmountStr
                finalBenefitAmount = benefit.finalBenefitAmount
                summaries = mapToListSummaryInfoUiModel(benefit.summaries)
            }
            return benefitSummaryInfoUiModel
        }

        private fun mapToListSummaryInfoUiModel(listSummariesItem: List<SummariesItem>): List<SummariesItemUiModel> {
            return listSummariesItem.map { summary ->
                SummariesItemUiModel(
                    description = summary.description,
                    type = summary.type,
                    amount = summary.amount,
                    amountStr = summary.amountStr,
                    details = mapToDetailSummaryUiModel(summary.details)
                )
            }
        }

        private fun mapToDetailSummaryUiModel(listDetailsItem: List<DetailsItem>): List<DetailsItemUiModel> {
            return listDetailsItem.map { details ->
                DetailsItemUiModel(
                    description = details.description,
                    amountStr = details.amountStr,
                    amount = details.amount,
                    type = details.type
                )
            }
        }
    }
}
