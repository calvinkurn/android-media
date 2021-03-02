package com.tokopedia.purchase_platform.common.feature.promo.view.mapper

import com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse.*
import com.tokopedia.purchase_platform.common.feature.promo.domain.model.PromoSpId
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.*

/**
 * Created by fwidjaja on 2020-03-05.
 */
class ValidateUsePromoCheckoutMapper {

    companion object {
        fun mapToValidateUseRevampPromoUiModel(validateUsePromoRevamp: ValidateUsePromoRevamp?): ValidateUsePromoRevampUiModel {
            var status = ""
            validateUsePromoRevamp?.status?.let { status = it }

            val listMessage = ArrayList<String>()
            validateUsePromoRevamp?.message?.forEach {
                listMessage.add(it)
            }

            return ValidateUsePromoRevampUiModel(
                    status = status,
                    message = listMessage,
                    promoUiModel = mapToPromoUiModel(validateUsePromoRevamp?.promo)
            )
        }

        private fun mapToPromoUiModel(promo: PromoValidateUseResponse?): PromoUiModel {
            return PromoUiModel(
                    codes = mapCodes(promo?.codes),
                    messageUiModel = mapMessageUiModel(promo?.message),
                    additionalInfoUiModel = mapToAdditionalInfoUiModel(promo?.additionalInfo),
                    benefitSummaryInfoUiModel = mapToBenefitSummaryInfoUiModel(promo?.benefitSummaryInfo),
                    voucherOrderUiModels = mapListVoucherOrders(promo?.voucherOrders),
                    tickerInfoUiModel = mapTickerInfoUiModel(promo?.tickerInfo),
                    trackingDetailUiModels = mapTrackingDetails(promo?.trackingDetails)
            )
        }

        private fun mapTrackingDetails(trackingDetails: List<TrackingDetailsItem>?): List<TrackingDetailsItemUiModel> {
            val trackingDetailUiModels = ArrayList<TrackingDetailsItemUiModel>()
            trackingDetails?.forEach {
                val trackingDetailsItemUiModel = TrackingDetailsItemUiModel().apply {
                    promoDetailsTracking = it.promoDetailsTracking
                    productId = it.productId
                    promoCodesTracking = it.promoCodesTracking
                }
                trackingDetailUiModels.add(trackingDetailsItemUiModel)
            }

            return trackingDetailUiModels
        }

        private fun mapTickerInfoUiModel(tickerInfo: TickerInfo?): TickerInfoUiModel {
            val tickerInfoUiModel = TickerInfoUiModel()
            tickerInfoUiModel.let {
                it.uniqueId = tickerInfo?.uniqueId
                it.statusCode = tickerInfo?.statusCode
                it.message = tickerInfo?.message ?: ""
            }

            return tickerInfoUiModel
        }

        private fun mapCodes(codes: List<String?>?): ArrayList<String> {
            val listNewCodes = arrayListOf<String>()
            codes?.forEach {
                it?.let {
                    listNewCodes.add(it)
                }
            }
            return listNewCodes
        }

        private fun mapMessageUiModel(messageData: Message?): MessageUiModel {
            var color = ""
            messageData?.color?.let { color = it }

            var state = ""
            messageData?.state?.let { state = it }

            var text = ""
            messageData?.text?.let { text = it }
            return MessageUiModel(
                    color = color,
                    state = state,
                    text = text)
        }

        private fun mapListVoucherOrders(voucherOrders: List<VoucherOrdersItem?>?): List<PromoCheckoutVoucherOrdersItemUiModel> {
            val listVoucherOrders = arrayListOf<PromoCheckoutVoucherOrdersItemUiModel>()
            voucherOrders?.let {
                it.forEach { voucherOrderItem ->
                    listVoucherOrders.add(mapToVoucherOrdersItemUiModel(voucherOrderItem))
                }
            }
            return listVoucherOrders
        }

        private fun mapToVoucherOrdersItemUiModel(voucherOrdersItem: VoucherOrdersItem?): PromoCheckoutVoucherOrdersItemUiModel {
            var code = ""
            voucherOrdersItem?.code?.let { code = it }
            return PromoCheckoutVoucherOrdersItemUiModel(
                    success = voucherOrdersItem?.success ?: false,
                    code = code,
                    type = voucherOrdersItem?.type ?: "",
                    uniqueId = voucherOrdersItem?.uniqueId ?: "",
                    messageUiModel = mapMessageUiModel(voucherOrdersItem?.message)
            )
        }

        private fun mapToAdditionalInfoUiModel(additionalInfo: AdditionalInfo?): AdditionalInfoUiModel {
            val additionalInfoUiModel = AdditionalInfoUiModel()
            additionalInfo?.messageInfo?.let {
                additionalInfoUiModel.messageInfoUiModel.message = it.message
                additionalInfoUiModel.messageInfoUiModel.detail = it.detail
            }
            additionalInfo?.errorDetail?.let {
                additionalInfoUiModel.errorDetailUiModel.message = it.message
            }
            additionalInfo?.emptyCartInfo?.let {
                additionalInfoUiModel.emptyCartInfoUiModel.detail = it.detail
                additionalInfoUiModel.emptyCartInfoUiModel.imgUrl = it.imageUrl
                additionalInfoUiModel.emptyCartInfoUiModel.message = it.message
            }
            val listUsageSummariesUiModel = arrayListOf<UsageSummariesUiModel>()
            additionalInfo?.usageSummaries?.forEach {
                listUsageSummariesUiModel.add(mapToUsageSummariesUiModel(it))
            }
            additionalInfoUiModel.usageSummariesUiModel = listUsageSummariesUiModel
            val promoSpIdUiModels = arrayListOf<PromoSpIdUiModel>()
            additionalInfo?.promoSpIds?.forEach {
                promoSpIdUiModels.add(mapPromoSpId(it))
            }
            additionalInfoUiModel.promoSpIds = promoSpIdUiModels
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
                    currencyDetailStr = usageSummaries.currencyDetailsStr)
        }

        private fun mapToBenefitSummaryInfoUiModel(benefitSummaryInfo: BenefitSummaryInfo?): BenefitSummaryInfoUiModel {
            val benefitSummaryInfoUiModel = BenefitSummaryInfoUiModel()
            benefitSummaryInfo?.let { benefit ->
                benefitSummaryInfoUiModel.finalBenefitText = benefit.finalBenefitText
                benefitSummaryInfoUiModel.finalBenefitAmountStr = benefit.finalBenefitAmountStr
                benefitSummaryInfoUiModel.finalBenefitAmount = benefit.finalBenefitAmount
                benefitSummaryInfoUiModel.summaries = mapToListSummaryInfoUiModel(benefit.summaries)
            }
            return benefitSummaryInfoUiModel
        }

        private fun mapToListSummaryInfoUiModel(listSummariesItem: List<SummariesItem?>): List<SummariesItemUiModel> {
            val listSummaryInfoUiModel = arrayListOf<SummariesItemUiModel>()
            listSummariesItem.forEach { summary ->
                var desc = ""
                summary?.description?.let { desc = it }

                var type = ""
                summary?.type?.let { type = it }

                var amountStr = ""
                summary?.amountStr?.let { amountStr = it }

                val summaryItemUiModel = SummariesItemUiModel(
                        description = desc,
                        type = type,
                        amount = summary?.amount ?: 0,
                        amountStr = amountStr,
                        details = mapToDetailSummaryUiModel(summary?.details)
                )
                listSummaryInfoUiModel.add(summaryItemUiModel)
            }
            return listSummaryInfoUiModel
        }

        private fun mapToDetailSummaryUiModel(listDetailsItem: List<DetailsItem?>?): List<DetailsItemUiModel> {
            val listDetailsItemUiModel = arrayListOf<DetailsItemUiModel>()
            listDetailsItem?.forEach { details ->
                var desc = ""
                details?.description?.let { desc = it }

                var amountStr = ""
                details?.amountStr?.let { amountStr = it }

                var type = ""
                details?.type?.let { type = it }

                val detailItemUiModel = DetailsItemUiModel(
                        description = desc,
                        amountStr = amountStr,
                        amount = details?.amount ?: 0,
                        type = type
                )
                listDetailsItemUiModel.add(detailItemUiModel)
            }
            return listDetailsItemUiModel
        }
    }
}