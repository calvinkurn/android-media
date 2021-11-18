package com.tokopedia.oneclickcheckout.order.domain.mapper

import com.tokopedia.oneclickcheckout.order.view.model.OrderPromo
import com.tokopedia.purchase_platform.common.feature.promo.domain.model.*
import com.tokopedia.purchase_platform.common.feature.promo.view.model.PromoCheckoutErrorDefault
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.*

object LastApplyMapper {

    fun mapPromo(promo: PromoSAFResponse): OrderPromo {
        return OrderPromo(
                lastApply = mapLastApply(promo.lastApply),
                promoErrorDefault = mapPromoErrorDefault(promo)
        )
    }

    private fun mapLastApply(lastApply: LastApply): LastApplyUiModel {
        return LastApplyUiModel(
                codes = lastApply.data.codes,
                voucherOrders = mapVoucherOrders(lastApply.data.voucherOrders),
                additionalInfo = mapAdditionalInfo(lastApply.data.additionalInfo),
                message = mapMessage(lastApply.data.message),
                listAllPromoCodes = mapAllPromoCodes(lastApply.data.codes, lastApply.data.voucherOrders),
        )
    }

    private fun mapVoucherOrders(voucherOrders: List<VoucherOrdersItem>): List<LastApplyVoucherOrdersItemUiModel> {
        return voucherOrders.map {
            LastApplyVoucherOrdersItemUiModel(it.code, it.uniqueId,
                    LastApplyMessageUiModel(
                            it.message.color,
                            it.message.state,
                            it.message.text
                    )
            )
        }
    }

    private fun mapAllPromoCodes(mappedCodes: List<String>, voucherOrders: List<VoucherOrdersItem>): List<String> {
        return mappedCodes + voucherOrders.map { it.code }
    }

    private fun mapAdditionalInfo(additionalInfo: AdditionalInfo): LastApplyAdditionalInfoUiModel {
        return LastApplyAdditionalInfoUiModel(
                messageInfo = LastApplyMessageInfoUiModel(
                        additionalInfo.messageInfo.detail,
                        additionalInfo.messageInfo.message
                ),
                errorDetail = LastApplyErrorDetailUiModel(additionalInfo.errorDetail.message),
                emptyCartInfo = LastApplyEmptyCartInfoUiModel(
                        detail = additionalInfo.cartEmptyInfo.detail,
                        imgUrl = additionalInfo.cartEmptyInfo.imageUrl,
                        message = additionalInfo.cartEmptyInfo.message,
                ),
                usageSummaries = additionalInfo.listUsageSummaries.map {
                    LastApplyUsageSummariesUiModel(
                            it.desc,
                            it.type,
                            it.amountStr,
                            it.amount,
                            it.currencyDetailsStr
                    )
                }
        )
    }

    private fun mapMessage(lastApplyMessage: Message): LastApplyMessageUiModel {
        return LastApplyMessageUiModel(
                lastApplyMessage.text,
                lastApplyMessage.state,
                lastApplyMessage.color,
        )
    }

    private fun mapPromoErrorDefault(promo: PromoSAFResponse): PromoCheckoutErrorDefault {
        return PromoCheckoutErrorDefault(
                promo.errorDefault.title,
                promo.errorDefault.description
        )
    }
}