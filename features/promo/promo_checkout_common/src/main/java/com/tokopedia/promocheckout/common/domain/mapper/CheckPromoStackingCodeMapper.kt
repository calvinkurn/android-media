package com.tokopedia.promocheckout.common.domain.mapper

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.promocheckout.common.domain.model.promostacking.response.*
import com.tokopedia.promocheckout.common.view.uimodel.*
import rx.functions.Func1
import javax.inject.Inject

class CheckPromoStackingCodeMapper @Inject constructor() : Func1<GraphqlResponse, ResponseGetPromoStackFirstUiModel> {
    private val STATUS_OK = "OK"
    private val STATUS_ERROR = "ERROR"

    override fun call(t: GraphqlResponse?): ResponseGetPromoStackFirstUiModel {
        var status = ""
        var data = DataUiModel()
        val response = t?.getData<ResponseGetPromoStackFirst?>(ResponseGetPromoStackFirst::class.java)
        response?.let { responseGetPromoStackFirst ->
            responseGetPromoStackFirst.getPromoStackFirst.let {
                status = it?.status ?: STATUS_ERROR
                when (it?.status) {
                    STATUS_OK -> {
                        data = mapData(it.data)
                    }
                }
            }
        }

        return ResponseGetPromoStackFirstUiModel(
                status,
                data
        )
    }

    fun mapData(data: Data?): DataUiModel {
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
        return dataUiModel
    }

    fun mapVoucherOrders(voucherOrdersItem: VoucherOrdersItem?): VoucherOrdersItemUiModel {
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

    fun mapMessage(message: Message?): MessageUiModel {
        var messageUiModel = MessageUiModel()
        message?.color?.let { color ->
            message.state?.let { state ->
                message.text?.let { text ->
                    messageUiModel = MessageUiModel(
                            color = color,
                            state = state,
                            text = text
                    )
                }
            }
        }
        return messageUiModel
    }

    fun mapClashing(clash: ClashingInfoDetail): ClashingInfoDetailUiModel {
        val listOptions = ArrayList<VoucherOrdersItemUiModel>()
        var clashingInfoDetailUiModel = ClashingInfoDetailUiModel()
        clash.isClashedPromos?.let { isClashed ->
            clash.clashMessage?.let { clashMessage ->
                clash.clashReason?.let { clashReason ->
                    clash.option.let { options ->
                        options?.forEach {
                            if (it != null) {
                                mapVoucherOrders(it)
                            }
                        }
                        clashingInfoDetailUiModel = ClashingInfoDetailUiModel(
                                isClashedPromos = isClashed,
                                clashMessage = clashMessage,
                                clashReason = clashReason,
                                option = listOptions
                        )
                    }
                }
            }
        }

        return clashingInfoDetailUiModel
    }
}