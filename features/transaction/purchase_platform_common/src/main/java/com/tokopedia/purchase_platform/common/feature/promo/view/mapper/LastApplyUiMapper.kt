package com.tokopedia.purchase_platform.common.feature.promo.view.mapper

import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyAdditionalInfoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyEmptyCartInfoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyErrorDetailUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyMessageInfoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyMessageUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUsageSummariesUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.AdditionalInfoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.EmptyCartInfoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ErrorDetailUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.MessageInfoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.MessageUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoCheckoutVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.UsageSummariesUiModel

/**
 * Created by fwidjaja on 13/03/20.
 */
object LastApplyUiMapper {
    private const val RED_STATE = "red"

    fun mapValidateUsePromoUiModelToLastApplyUiModel(promoUiModel: PromoUiModel): LastApplyUiModel {
        return LastApplyUiModel(
            codes = promoUiModel.codes,
            voucherOrders = mapVoucherOrders(promoUiModel.voucherOrderUiModels),
            additionalInfo = mapAdditionalInfo(promoUiModel.additionalInfoUiModel),
            message = mapMessageUiModel(promoUiModel.messageUiModel),
            defaultEmptyPromoMessage = if (promoUiModel.titleDescription.isNotBlank()) promoUiModel.titleDescription else "",
            benefitSummaryInfo = promoUiModel.benefitSummaryInfoUiModel,
            userGroupMetadata = promoUiModel.userGroupMetadata
        )
    }

    private fun mapVoucherOrders(voucherOrderUiModels: List<PromoCheckoutVoucherOrdersItemUiModel?>): List<LastApplyVoucherOrdersItemUiModel> {
        val listLastApplyVoucherOrders = arrayListOf<LastApplyVoucherOrdersItemUiModel>()
        voucherOrderUiModels.forEach {
            if (it != null && !it.messageUiModel.state.equals(RED_STATE, true)) {
                listLastApplyVoucherOrders.add(mapVoucherOrdersItem(it))
            }
        }
        return listLastApplyVoucherOrders
    }

    private fun mapVoucherOrdersItem(promoCheckoutVoucherOrdersItemUiModel: PromoCheckoutVoucherOrdersItemUiModel): LastApplyVoucherOrdersItemUiModel {
        return LastApplyVoucherOrdersItemUiModel(
            code = promoCheckoutVoucherOrdersItemUiModel.code,
            uniqueId = promoCheckoutVoucherOrdersItemUiModel.uniqueId,
            message = mapMessageUiModel(promoCheckoutVoucherOrdersItemUiModel.messageUiModel),
            cartStringGroup = promoCheckoutVoucherOrdersItemUiModel.cartStringGroup,
            type = promoCheckoutVoucherOrdersItemUiModel.type
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
        return usageSummariesList.map { mapUsageSummariesUiModel(it) }
    }

    private fun mapMessageInfo(messageInfoUiModel: MessageInfoUiModel): LastApplyMessageInfoUiModel {
        return LastApplyMessageInfoUiModel(
            detail = messageInfoUiModel.detail,
            message = messageInfoUiModel.message
        )
    }

    private fun mapErrorInfo(errorDetailUiModel: ErrorDetailUiModel): LastApplyErrorDetailUiModel {
        return LastApplyErrorDetailUiModel(
            message = errorDetailUiModel.message
        )
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
            amount = usageSummariesUiModel.amount,
            currencyDetailsStr = usageSummariesUiModel.currencyDetailStr
        )
    }
}
