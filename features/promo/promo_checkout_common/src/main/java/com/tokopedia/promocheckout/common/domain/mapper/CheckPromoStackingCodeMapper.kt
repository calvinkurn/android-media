package com.tokopedia.promocheckout.common.domain.mapper

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.promocheckout.common.domain.model.promostacking.response.*
import com.tokopedia.promocheckout.common.domain.model.promostacking.response.ClashingInfoDetail
import com.tokopedia.promocheckout.common.view.uimodel.*
import rx.functions.Func1
import javax.inject.Inject

open class CheckPromoStackingCodeMapper @Inject constructor() : Func1<GraphqlResponse, ResponseGetPromoStackUiModel> {
    private val STATUS_OK = "OK"
    private val STATUS_ERROR = "ERROR"

    var isFinal: Boolean = false
        set(value) {
            field = value
        }

    override fun call(t: GraphqlResponse?): ResponseGetPromoStackUiModel {
        var status = ""
        var data = DataUiModel()
        var responseFirst : ResponseGetPromoStackFirst?
        var responseFinal : ResponseGetPromoStackFinal?

        if (isFinal) {
            responseFinal = t?.getData(ResponseGetPromoStackFinal::class.java)
            responseFinal.let { responseGetPromoStackFinal ->
                responseGetPromoStackFinal?.getPromoStackUse.let {
                    // TODO : buka kondisi status, krn dummy statusnya string kosong
                    /*status = it?.status ?: STATUS_ERROR
                    when (it?.status) {
                        STATUS_OK -> {*/
                            data = mapData(it?.data)
                       /* }
                    }*/
                }
            }
        } else {
            responseFirst = t?.getData(ResponseGetPromoStackFirst::class.java)
            responseFirst.let { responseGetPromoStackFirst ->
                responseGetPromoStackFirst?.getPromoStackFirst.let {
                    // TODO : buka kondisi status, krn dummy statusnya string kosong
                    /*status = it?.status ?: STATUS_ERROR
                    when (it?.status) {
                        STATUS_OK -> {*/
                            data = mapData(it?.data)
                        /*}
                    }*/
                }
            }
        }

        return ResponseGetPromoStackUiModel(
                status,
                data
        )
    }

    fun callDummy(response: ResponseGetPromoStackFirst): ResponseGetPromoStackUiModel {
        var status = ""
        var data = DataUiModel()
        response.let { responseGetPromoStackFirst ->
            responseGetPromoStackFirst.getPromoStackFirst.let {
                status = it?.status ?: STATUS_ERROR
                when (it?.status) {
                    STATUS_OK -> {
                        data = mapData(it.data)
                    }
                }
            }
        }

        return ResponseGetPromoStackUiModel(
                status,
                data
        )
    }

    private fun mapData(data: Data?): DataUiModel {
        val listCodes = ArrayList<String>()
        var dataUiModel = DataUiModel()
        data?.globalSuccess?.let { globalSuccess ->
            data.success?.let { success ->
                data.message?.let { message ->
                    data.codes.let { codes ->
                        codes?.forEach {
                            if (it != null) {
                                listCodes.add(it)
                            }
                        }
                        data.promoCodeId?.let { promoCodeId ->
                            data.titleDescription?.let { titleDesc ->
                                data.discountAmount?.let { discountAmount ->
                                    data.cashbackWalletAmount?.let { cashbackWalletAmount ->
                                        data.cashbackAdvocateReferralAmount?.let { cashbackAdvocateReferralAmount ->
                                            data.cashbackVoucherDescription?.let { cashbackVoucherDesc ->
                                                data.invoiceDescription?.let { invoiceDesc ->
                                                    data.benefitSummaryInfo?.let { benefit ->
                                                        data.clashingInfoDetail?.let { clashingDetails ->
                                                            data.couponDescription?.let { couponDesc ->
                                                                data.gatewayId?.let { gatewayId ->
                                                                    data.isCoupon?.let { isCoupon ->
                                                                        data.voucherOrders?.let { voucherOrders ->
                                                                            dataUiModel = DataUiModel(
                                                                                    globalSuccess = globalSuccess,
                                                                                    success = success,
                                                                                    message = mapMessage(message),
                                                                                    codes = listCodes,
                                                                                    promoCodeId = promoCodeId,
                                                                                    titleDescription = titleDesc,
                                                                                    discountAmount = discountAmount,
                                                                                    cashbackWalletAmount = cashbackWalletAmount,
                                                                                    invoiceDescription = invoiceDesc,
                                                                                    voucherOrders = voucherOrders.map {
                                                                                        mapVoucherOrders(it)
                                                                                    },
                                                                                    cashbackAdvocateReferralAmount = cashbackAdvocateReferralAmount,
                                                                                    cashbackVoucherDescription = cashbackVoucherDesc,
                                                                                    couponDescription = couponDesc,
                                                                                    benefit = mapBenefit(benefit),
                                                                                    clashings = mapClashing(clashingDetails),
                                                                                    gatewayId = gatewayId,
                                                                                    isCoupon = isCoupon
                                                                            )
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return dataUiModel
    }

    private fun mapVoucherOrders(voucherOrdersItem: VoucherOrdersItem?): VoucherOrdersItemUiModel {
        var voucherOrdersItemUiModel = VoucherOrdersItemUiModel()
        voucherOrdersItem?.success?.let { success ->
            voucherOrdersItem.code?.let { code ->
                voucherOrdersItem.uniqueId?.let { uniqueId ->
                    voucherOrdersItem.cartId?.let { cartId ->
                        voucherOrdersItem.type?.let { type ->
                            voucherOrdersItem.cashbackWalletAmount?.let { cashbackWalletAmount ->
                                voucherOrdersItem.discountAmount?.let { discountAmount ->
                                    voucherOrdersItem.invoiceDescription?.let { invoiceDesc ->
                                        voucherOrdersItem.message?.let { message ->
                                            voucherOrdersItemUiModel = VoucherOrdersItemUiModel(
                                                    success = success,
                                                    code = code,
                                                    uniqueId = uniqueId,
                                                    cartId = cartId,
                                                    type = type,
                                                    cashbackWalletAmount = cashbackWalletAmount,
                                                    discountAmount = discountAmount,
                                                    invoiceDescription = invoiceDesc,
                                                    message = mapMessage(message)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return voucherOrdersItemUiModel
    }

    // TODO : balikin "grey" to state untuk data real
    private fun mapMessage(message: Message?): MessageUiModel {
        var messageUiModel = MessageUiModel()
        message?.color?.let { color ->
            message.state?.let { state ->
                message.text?.let { text ->
                    messageUiModel = MessageUiModel(
                            color = color,
                            state = "grey",
                            text = text
                    )
                }
            }
        }
        return messageUiModel
    }

    private fun mapBenefit(benefit: BenefitSummaryInfo): BenefitSummaryInfoUiModel {
        var benefitSummaryInfoUiModel = BenefitSummaryInfoUiModel()
        benefit.finalBenefitText?.let { text ->
            benefit.finalBenefitAmount?.let { amount ->
                benefit.summaries?.let { listSummariesItem ->
                    benefitSummaryInfoUiModel = BenefitSummaryInfoUiModel(
                            finalBenefitText = text,
                            finalBenefitAmount = amount,
                            summaries = listSummariesItem.map {
                                mapSummariesBenefit(it)
                            }

                    )
                }
            }
        }

        return benefitSummaryInfoUiModel
    }

    private fun mapSummariesBenefit(summaries: SummariesItem?): SummariesUiModel {
        var summariesUiModel = SummariesUiModel()
        summaries?.description?.let { desc ->
            summaries.type?.let { type ->
                summaries.amountStr?.let { amountStr ->
                    summaries.amount?.let { amount ->
                        summariesUiModel = SummariesUiModel(
                                description = desc,
                                type = type,
                                amountStr = amountStr,
                                amount = amount
                        )
                    }
                }
            }
        }

        return summariesUiModel
    }

    private fun mapClashing(clash: ClashingInfoDetail): ClashingInfoDetailUiModel {
        val listOptions = ArrayList<ClashingVoucherOptionUiModel>()
        var clashingInfoDetailUiModel = ClashingInfoDetailUiModel()
        clash.isClashedPromos?.let { isClashed ->
            clash.clashMessage?.let { clashMessage ->
                clash.clashReason?.let { clashReason ->
                    clash.options?.let { options ->
                        options.forEach {
                            if (it != null) {
                                listOptions.add(mapClashingVoucherOption(it))
                            }
                        }
                        clashingInfoDetailUiModel = ClashingInfoDetailUiModel(
                                isClashedPromos = isClashed,
                                clashMessage = clashMessage,
                                clashReason = clashReason,
                                options = listOptions
                        )
                    }
                }
            }
        }

        return clashingInfoDetailUiModel
    }

    fun mapClashingVoucherOption(clashingVoucherOption: ClashingVoucherOption): ClashingVoucherOptionUiModel {
        var clashingVoucherOptionUiModel = ClashingVoucherOptionUiModel()
        val clashingVoucherOrderUiModelList = ArrayList<ClashingVoucherOrderUiModel>()
        for (clashingVoucherOrder: ClashingVoucherOrder in clashingVoucherOption.voucherOrders) {
            val clashingVoucherOrderUiModel = mapClashingVoucherOrder(clashingVoucherOrder)
            clashingVoucherOrderUiModelList.add(clashingVoucherOrderUiModel)
        }
        clashingVoucherOptionUiModel.voucherOrders = clashingVoucherOrderUiModelList

        return clashingVoucherOptionUiModel
    }

    fun mapClashingVoucherOrder(clashingVoucherOrder: ClashingVoucherOrder): ClashingVoucherOrderUiModel {
        var clashingVoucherOrderUiModel = ClashingVoucherOrderUiModel()
        clashingVoucherOrder.let {
            clashingVoucherOrderUiModel = ClashingVoucherOrderUiModel(
                    clashingVoucherOrder.code,
                    clashingVoucherOrder.uniqueId,
                    clashingVoucherOrder.cartId,
                    clashingVoucherOrder.promoName,
                    clashingVoucherOrder.potentialBenefit
            )
        }

        return clashingVoucherOrderUiModel
    }
}