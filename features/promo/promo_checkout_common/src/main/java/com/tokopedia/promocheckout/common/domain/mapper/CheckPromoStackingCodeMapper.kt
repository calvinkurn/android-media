package com.tokopedia.promocheckout.common.domain.mapper

import com.tokopedia.promocheckout.common.domain.model.promostacking.response.Data
import com.tokopedia.promocheckout.common.domain.model.promostacking.response.Message
import com.tokopedia.promocheckout.common.domain.model.promostacking.response.Response
import com.tokopedia.promocheckout.common.domain.model.promostacking.response.VoucherOrdersItem
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.promocheckout.common.view.uimodel.MessageUiModel
import com.tokopedia.promocheckout.common.view.uimodel.ResponseUiModel
import com.tokopedia.promocheckout.common.view.uimodel.VoucherOrdersItemUiModel

object CheckPromoStackingCodeMapper: ICheckPromoStackingCodeMapper {
    override fun convertResponseDataModel(data: Data): DataUiModel? {
        data.globalSuccess.let { globalSuccess ->
            data.success.let { success ->
                data.message?.let { message ->
                    data.codes.let { codes ->
                        data.promoCodeId.let { promoCodeId ->
                            data.titleDescription.let { titleDesc ->
                                data.discountAmount.let { discountAmount ->
                                    data.cashbackWalletAmount.let { cashbackWalletAmount ->
                                        data.invoiceDescription.let { invoiceDesc ->
                                            data.voucherOrders?.let { voucherOrders ->
                                                return DataUiModel(
                                                        globalSuccess = globalSuccess,
                                                        success = success,
                                                        message = responseToMessageUiModel(message),
                                                        codes = codes,
                                                        promoCodeId = promoCodeId,
                                                        titleDescription = titleDesc,
                                                        discountAmount = discountAmount,
                                                        cashbackWalletAmount = cashbackWalletAmount,
                                                        invoiceDescription = invoiceDesc,
                                                        voucherOrders = voucherOrders.map {
                                                            responseToVoucherOrdersUiModel(it)
                                                        }.filter { it != null }.map { it!! }
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
        } ?: return null
    }

    fun responseToResponseUiModel(response: Response): ResponseUiModel? {
        response.status.let { status ->
            response.data?.let { data ->
                return ResponseUiModel(
                        status = status,
                        data = convertResponseDataModel(data)
                )
            }
        } ?: return null
    }

    fun responseToVoucherOrdersUiModel(voucherOrdersItem: VoucherOrdersItem?): VoucherOrdersItemUiModel? {
        voucherOrdersItem?.success.let { success ->
            voucherOrdersItem?.code.let { code ->
                voucherOrdersItem?.uniqueId.let { uniqueId ->
                    voucherOrdersItem?.cartId.let { cartId ->
                        voucherOrdersItem?.type.let { type ->
                            voucherOrdersItem?.cashbackWalletAmount.let { cashbackWalletAmount ->
                                voucherOrdersItem?.discountAmount.let { discountAmount ->
                                    voucherOrdersItem?.invoiceDescription.let { invoiceDesc ->
                                        voucherOrdersItem?.message?.let { message ->
                                            return VoucherOrdersItemUiModel(
                                                    success = success,
                                                    code = code,
                                                    uniqueId = uniqueId,
                                                    cartId = cartId,
                                                    type = type,
                                                    cashbackWalletAmount = cashbackWalletAmount,
                                                    discountAmount = discountAmount,
                                                    invoiceDescription = invoiceDesc,
                                                    message = responseToMessageUiModel(message)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } ?: return null
    }

    fun responseToMessageUiModel(message: Message): MessageUiModel? {
        message.color.let { color ->
            message.state.let { state ->
                message.text.let { text ->
                    return MessageUiModel(
                            color = color,
                            state = state,
                            text = text
                    )
                }
            }
        }
    }
}