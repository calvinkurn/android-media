package com.tokopedia.purchase_platform.features.promo.presentation.mapper

import com.tokopedia.purchase_platform.features.promo.data.response.validate_use.*
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.validate_use.*

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
                    voucherOrderUiModels = mapListVoucherOrders(promo?.voucherOrders)
            )
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

        private fun mapListVoucherOrders(voucherOrders: List<VoucherOrdersItem?>?) : List<PromoCheckoutVoucherOrdersItemUiModel> {
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
                    code = code,
                    messageUiModel = mapMessageUiModel(voucherOrdersItem?.message)
            )
        }

        private fun mapToAdditionalInfoUiModel(additionalInfo: AdditionalInfo?) : AdditionalInfoUiModel {
            val additionalInfoUiModel = AdditionalInfoUiModel()
            val listUsageSummariesUiModel = arrayListOf<UsageSummariesUiModel>()
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
            additionalInfo?.usageSummaries?.forEach {
                listUsageSummariesUiModel.add(mapToUsageSummariesUiModel(it))
            }
            return additionalInfoUiModel
        }

        private fun mapToUsageSummariesUiModel(usageSummaries: UsageSummaries): UsageSummariesUiModel {
            return UsageSummariesUiModel(
                    desc = usageSummaries.description,
                    type = usageSummaries.type,
                    amountStr = usageSummaries.amountString,
                    amount = usageSummaries.amount)
        }

        private fun mapToBenefitSummaryInfoUiModel(benefitSummaryInfo: BenefitSummaryInfo?): BenefitSummaryInfoUiModel {
            val benefitSummaryInfoUiModel = BenefitSummaryInfoUiModel()
            benefitSummaryInfo?.let { benefit ->
                benefitSummaryInfoUiModel.finalBenefitText = benefit.finalBenefitText
                benefitSummaryInfoUiModel.finalBenefitAmountStr = benefit.finalBenefitAmountStr
                mapToListSummaryInfoUiModel(benefit.summaries)
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
                        type = type
                )
                listDetailsItemUiModel.add(detailItemUiModel)
            }
            return listDetailsItemUiModel
        }
    }
}