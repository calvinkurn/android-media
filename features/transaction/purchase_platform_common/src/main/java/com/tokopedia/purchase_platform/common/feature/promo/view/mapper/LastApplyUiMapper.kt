package com.tokopedia.purchase_platform.common.feature.promo.view.mapper

import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.*
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.*

/**
 * Created by fwidjaja on 13/03/20.
 */


object LastApplyUiMapper {
    val RED_STATE = "red"
    fun mapValidateUsePromoUiModelToLastApplyUiModel(promoUiModel: PromoUiModel): LastApplyUiModel {
        return LastApplyUiModel(
                codes = promoUiModel.codes,
                voucherOrders = mapVoucherOrders(promoUiModel.voucherOrderUiModels),
                additionalInfo = mapAdditionalInfo(promoUiModel.additionalInfoUiModel),
                message = mapMessageUiModel(promoUiModel.messageUiModel),
                defaultEmptyPromoMessage = if (promoUiModel.titleDescription.isNotBlank()) promoUiModel.titleDescription else ""
        )
    }

    private fun mapVoucherOrders(voucherOrderUiModels: List<PromoCheckoutVoucherOrdersItemUiModel?>): List<LastApplyVoucherOrdersItemUiModel> {
        val listLastApplyVoucherOrders = arrayListOf<LastApplyVoucherOrdersItemUiModel>()
        voucherOrderUiModels.forEach {
            it?.let { it1 ->
                if (!it1.messageUiModel.state.equals(RED_STATE, true)) {
                    listLastApplyVoucherOrders.add(mapVoucherOrdersItem(it1))
                }
            }
        }
        return listLastApplyVoucherOrders
    }

    private fun mapVoucherOrdersItem(promoCheckoutVoucherOrdersItemUiModel: PromoCheckoutVoucherOrdersItemUiModel): LastApplyVoucherOrdersItemUiModel {
        var code: String
        promoCheckoutVoucherOrdersItemUiModel.code.let { code = it }

        return LastApplyVoucherOrdersItemUiModel(
                code = code,
                uniqueId = promoCheckoutVoucherOrdersItemUiModel.uniqueId,
                message = mapMessageUiModel(promoCheckoutVoucherOrdersItemUiModel.messageUiModel)
        )
    }

    private fun mapMessageUiModel(messageUiModel: MessageUiModel): LastApplyMessageUiModel {
        return LastApplyMessageUiModel(
                color = messageUiModel.color,
                state = messageUiModel.state,
                text = messageUiModel.text
        )
    }

    private fun mapAdditionalInfo(additionalInfoUiModel: AdditionalInfoUiModel): LastApplyAdditionalInfoUiModel {
        return LastApplyAdditionalInfoUiModel(
                messageInfo = mapMessageInfo(additionalInfoUiModel.messageInfoUiModel),
                errorDetail = mapErrorInfo(additionalInfoUiModel.errorDetailUiModel),
                emptyCartInfo = mapEmptyCartInfo(additionalInfoUiModel.emptyCartInfoUiModel),
                usageSummaries = mapUsageSummaries(additionalInfoUiModel.usageSummariesUiModel)
        )
    }

    private fun mapUsageSummaries(usageSummariesList: List<UsageSummariesUiModel>): List<LastApplyUsageSummariesUiModel> {
        val listLastAppyUsageSummariesUiModel = arrayListOf<LastApplyUsageSummariesUiModel>()
        usageSummariesList.forEach {
            listLastAppyUsageSummariesUiModel.add(mapUsageSummariesUiModel(it))
        }
        return listLastAppyUsageSummariesUiModel
    }

    private fun mapMessageInfo(messageInfoUiModel: MessageInfoUiModel): LastApplyMessageInfoUiModel {
        return LastApplyMessageInfoUiModel(
                detail = messageInfoUiModel.detail,
                message = messageInfoUiModel.message)
    }

    private fun mapErrorInfo(errorDetailUiModel: ErrorDetailUiModel): LastApplyErrorDetailUiModel {
        return LastApplyErrorDetailUiModel(
                message = errorDetailUiModel.message)
    }

    private fun mapEmptyCartInfo(emptyCartInfo: EmptyCartInfoUiModel): LastApplyEmptyCartInfoUiModel {
        return LastApplyEmptyCartInfoUiModel(
                imgUrl = emptyCartInfo.imgUrl,
                message = emptyCartInfo.message,
                detail = emptyCartInfo.detail
        )
    }

    private fun mapUsageSummariesUiModel(usageSummariesUiModel: UsageSummariesUiModel): LastApplyUsageSummariesUiModel {
        return LastApplyUsageSummariesUiModel(
                description = usageSummariesUiModel.desc,
                type = usageSummariesUiModel.type,
                amountStr = usageSummariesUiModel.amountStr,
                amount = usageSummariesUiModel.amount)
    }
}