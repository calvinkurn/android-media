package com.tokopedia.oneclickcheckout.order.domain.mapper

import com.tokopedia.oneclickcheckout.order.view.model.OrderPromo
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant
import com.tokopedia.purchase_platform.common.feature.promo.domain.model.LastApply
import com.tokopedia.purchase_platform.common.feature.promo.domain.model.Message
import com.tokopedia.purchase_platform.common.feature.promo.domain.model.PromoSAFResponse
import com.tokopedia.purchase_platform.common.feature.promo.view.model.PromoCheckoutErrorDefault
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.*

object LastApplyMapper {

    fun mapPromo(promo: PromoSAFResponse?): OrderPromo {
        val lastApply = promo?.lastApply
        var orderPromo = OrderPromo()
        orderPromo = mapLastApply(lastApply, orderPromo)
        orderPromo = mapPromoErrorDefault(promo, orderPromo)
        return orderPromo
    }

    private fun mapLastApply(lastApply: LastApply?, orderPromo: OrderPromo): OrderPromo {
        if (lastApply?.data != null) {
            var lastApplyUiModel = LastApplyUiModel()
            val mappedCodes = mapCodes(lastApply)
            lastApplyUiModel.codes = mappedCodes
            lastApplyUiModel.voucherOrders = mapVoucherOrders(lastApply)
            lastApplyUiModel = mapAdditionalInfo(lastApply, lastApplyUiModel)
            lastApplyUiModel = mapMessage(lastApply, lastApplyUiModel, mappedCodes)
            lastApplyUiModel.listAllPromoCodes = mapAllPromoCodes(mappedCodes, lastApply)
            orderPromo.lastApply = lastApplyUiModel
        }
        return orderPromo
    }

    private fun mapVoucherOrders(lastApply: LastApply): List<LastApplyVoucherOrdersItemUiModel> {
        val listVoucherOrdersUiModel = ArrayList<LastApplyVoucherOrdersItemUiModel>()
        if (lastApply.data?.voucherOrders != null) {
            val voucherOrders = lastApply.data?.voucherOrders ?: emptyList()
            for (i in voucherOrders.indices) {
                val lastApplyVoucherOrdersItemUiModel = LastApplyVoucherOrdersItemUiModel()
                val voucherOrdersItem = lastApply.data?.voucherOrders?.get(i)
                if (voucherOrdersItem != null) {
                    val message = voucherOrdersItem.message
                    val code = voucherOrdersItem.code
                    val uniqueId = voucherOrdersItem.uniqueId
                    if (code != null && uniqueId != null && message?.color != null && message.state != null && message.text != null) {
                        lastApplyVoucherOrdersItemUiModel.code = code
                        lastApplyVoucherOrdersItemUiModel.uniqueId = uniqueId
                        val lastApplyMessageInfoUiModel = LastApplyMessageUiModel()
                        lastApplyMessageInfoUiModel.color = message.color ?: ""
                        lastApplyMessageInfoUiModel.state = message.state ?: ""
                        lastApplyMessageInfoUiModel.text = message.text ?: ""
                        lastApplyVoucherOrdersItemUiModel.message = lastApplyMessageInfoUiModel
                        listVoucherOrdersUiModel.add(lastApplyVoucherOrdersItemUiModel)
                    }
                }
            }
        }
        return listVoucherOrdersUiModel
    }

    private fun mapAllPromoCodes(mappedCodes: List<String>, lastApply: LastApply):List<String> {
        val listAllPromoCodes = ArrayList<String>()
        for (i in mappedCodes.indices) {
            listAllPromoCodes.add(mappedCodes[i])
        }
        if (lastApply.data?.voucherOrders != null) {
            val voucherOrders = lastApply.data?.voucherOrders ?: emptyList()
            for (voucherOrdersItem in voucherOrders) {
                val element = voucherOrdersItem?.code
                if (element != null) {
                    listAllPromoCodes.add(element)
                }
            }
        }
        return listAllPromoCodes
    }

    private fun mapAdditionalInfo(lastApply: LastApply, lastApplyUiModel: LastApplyUiModel): LastApplyUiModel {
        val responseAdditionalInfo = lastApply.data?.additionalInfo
        if (responseAdditionalInfo != null) {
            val responseCartEmptyInfo = responseAdditionalInfo.cartEmptyInfo
            val errorDetail = responseAdditionalInfo.errorDetail
            val messageInfo = responseAdditionalInfo.messageInfo
            val lastApplyAdditionalInfoUiModel = LastApplyAdditionalInfoUiModel()
            val lastApplyEmptyCartInfoUiModel = LastApplyEmptyCartInfoUiModel()
            if (responseCartEmptyInfo?.detail != null && responseCartEmptyInfo.imageUrl != null && responseCartEmptyInfo.message != null) {
                lastApplyEmptyCartInfoUiModel.detail = responseCartEmptyInfo.detail ?: ""
                lastApplyEmptyCartInfoUiModel.imgUrl = responseCartEmptyInfo.imageUrl ?: ""
                lastApplyEmptyCartInfoUiModel.message = responseCartEmptyInfo.message ?: ""
            }
            lastApplyAdditionalInfoUiModel.emptyCartInfo = lastApplyEmptyCartInfoUiModel
            val lastApplyErrorDetailUiModel = LastApplyErrorDetailUiModel()
            if (errorDetail?.message != null) {
                lastApplyErrorDetailUiModel.message = errorDetail.message ?: ""
            }
            lastApplyAdditionalInfoUiModel.errorDetail = lastApplyErrorDetailUiModel
            val lastApplyMessageInfoUiModel = LastApplyMessageInfoUiModel()
            if (messageInfo?.detail != null && messageInfo.message != null) {
                lastApplyMessageInfoUiModel.detail = messageInfo.detail ?: ""
                lastApplyMessageInfoUiModel.message = messageInfo.message ?: ""
            }
            lastApplyAdditionalInfoUiModel.messageInfo = lastApplyMessageInfoUiModel
            lastApplyAdditionalInfoUiModel.messageInfo = lastApplyMessageInfoUiModel
            lastApplyAdditionalInfoUiModel.errorDetail = lastApplyErrorDetailUiModel
            lastApplyAdditionalInfoUiModel.emptyCartInfo = lastApplyEmptyCartInfoUiModel
            // set usage summaries
            val listUsageSummaries = ArrayList<LastApplyUsageSummariesUiModel>()
            val responseListUsageSummaries = responseAdditionalInfo.listUsageSummaries
                    ?: emptyList()
            for ((desc, type, amountStr, amount, currencyDetailsStr) in responseListUsageSummaries) {
                if (desc != null && type != null && amountStr != null && amount != null) {
                    val lastApplyUsageSummariesUiModel = LastApplyUsageSummariesUiModel()
                    lastApplyUsageSummariesUiModel.description = desc
                    lastApplyUsageSummariesUiModel.type = type
                    lastApplyUsageSummariesUiModel.amountStr = amountStr
                    lastApplyUsageSummariesUiModel.amount = amount
                    lastApplyUsageSummariesUiModel.currencyDetailsStr = currencyDetailsStr
                    listUsageSummaries.add(lastApplyUsageSummariesUiModel)
                }
            }
            lastApplyAdditionalInfoUiModel.usageSummaries = listUsageSummaries
            lastApplyUiModel.additionalInfo = lastApplyAdditionalInfoUiModel
        }
        return lastApplyUiModel
    }

    private fun mapMessage(lastApply: LastApply, lastApplyUiModel: LastApplyUiModel, mappedCodes: List<String>): LastApplyUiModel {
        if (lastApply.data?.message != null) {
            lastApplyUiModel.message = generateLastApplyMessageUiModel(lastApply.data?.message)
            val listRedStates = ArrayList<String>()
            if (lastApply.data?.message?.state != null) {
                if (lastApply.data?.message?.state.equals(CheckoutConstant.STATE_RED, ignoreCase = true)) {
                    listRedStates.addAll(mappedCodes)
                }
                val voucherOrders = lastApply.data?.voucherOrders ?: emptyList()
                for (voucherOrdersItem in voucherOrders) {
                    if (voucherOrdersItem?.message?.state.equals(CheckoutConstant.STATE_RED, ignoreCase = true) && voucherOrdersItem?.code != null) {
                        listRedStates.add(voucherOrdersItem.code ?: "")
                    }
                }
            }
            lastApplyUiModel.listRedPromos = listRedStates
        }
        return lastApplyUiModel
    }

    private fun generateLastApplyMessageUiModel(lastApplyMessage: Message?): LastApplyMessageUiModel {
        val lastApplyMessageUiModel = LastApplyMessageUiModel()
        if (lastApplyMessage?.text != null && lastApplyMessage.state != null && lastApplyMessage.color != null) {
            lastApplyMessageUiModel.text = lastApplyMessage.text ?: ""
            lastApplyMessageUiModel.state = lastApplyMessage.state ?: ""
            lastApplyMessageUiModel.color = lastApplyMessage.color ?: ""
        }
        return lastApplyMessageUiModel
    }

    private fun mapCodes(lastApply: LastApply): List<String> {
        // set codes
        val codes = lastApply.data?.codes ?: emptyList()
        val mappedCodes = ArrayList<String>()
        for (code in codes) {
            if (code != null) {
                mappedCodes.add(code)
            }
        }
        return mappedCodes
    }

    private fun mapPromoErrorDefault(promo: PromoSAFResponse?, orderPromo: OrderPromo): OrderPromo {
        if (promo?.errorDefault != null && promo.errorDefault?.title?.isEmpty() != true && promo.errorDefault?.description?.isEmpty() != true) {
            val promoCheckoutErrorDefault = PromoCheckoutErrorDefault()
            promoCheckoutErrorDefault.title = promo.errorDefault?.title ?: ""
            promoCheckoutErrorDefault.desc = promo.errorDefault?.description ?: ""
            orderPromo.promoErrorDefault = promoCheckoutErrorDefault
        }
        return orderPromo
    }
}