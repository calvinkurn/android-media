package com.tokopedia.buyerorder.recharge.domain

import com.tokopedia.buyerorder.recharge.data.response.RechargeOrderDetail
import com.tokopedia.buyerorder.recharge.presentation.model.*
import com.tokopedia.unifycomponents.ticker.Ticker

/**
 * @author by furqan on 27/10/2021
 */
class RechargeOrderDetailMapper {
    companion object {
        fun transform(orderDetails: RechargeOrderDetail): RechargeOrderDetailModel {
            val additionalTickerInfo = if (orderDetails.additionalTickerInfo.isNotEmpty()) {
                RechargeOrderDetailTickerModel(
                        title = orderDetails.additionalTickerInfo[0].title,
                        text = orderDetails.additionalTickerInfo[0].notes,
                        urlDetail = orderDetails.additionalTickerInfo[0].urlDetail,
                        type = Ticker.TYPE_INFORMATION
                )
            } else null

            return RechargeOrderDetailModel(
                    topSectionModel = RechargeOrderDetailTopSectionModel(
                            labelStatusColor = orderDetails.status.backgroundColor,
                            textStatusColor = orderDetails.status.textColor,
                            textStatus = orderDetails.status.statusText,
                            tickerData = RechargeOrderDetailTickerModel(
                                    title = "",
                                    text = orderDetails.conditionalInfo.text,
                                    urlDetail = orderDetails.conditionalInfo.url,
                                    type = Ticker.TYPE_INFORMATION
                            ),
                            invoiceRefNum = orderDetails.invoice.invoiceRefNum,
                            invoiceUrl = orderDetails.invoice.invoiceUrl,
                            titleData = orderDetails.title.map {
                                RechargeOrderDetailSimpleModel(
                                        label = it.label,
                                        detail = it.value,
                                        isTitleBold = false,
                                        isDetailBold = false,
                                        alignment = RechargeSimpleAlignment.RIGHT
                                )
                            }.toList()
                    ),
                    detailsSection = RechargeOrderDetailSectionModel(
                            detailList = orderDetails.detail.map {
                                RechargeOrderDetailSimpleModel(
                                        label = it.label,
                                        detail = it.value,
                                        isTitleBold = false,
                                        isDetailBold = true,
                                        alignment = RechargeSimpleAlignment.LEFT,
                                        isCopyable = (it.label.equals(VOUCHER_CODE_LABEL, true))
                                )
                            }.toList()
                    ),
                    paymentSectionModel = RechargeOrderDetailPaymentModel(
                            paymentMethod = RechargeOrderDetailSimpleModel(
                                    label = orderDetails.paymentMethod.label,
                                    detail = orderDetails.paymentMethod.value,
                                    isTitleBold = false,
                                    isDetailBold = false,
                                    alignment = RechargeSimpleAlignment.RIGHT
                            ),
                            paymentDetails = orderDetails.purchasedItems.map {
                                RechargeOrderDetailSimpleModel(
                                        label = it.name,
                                        detail = it.price,
                                        isTitleBold = false,
                                        isDetailBold = false,
                                        alignment = RechargeSimpleAlignment.RIGHT
                                )
                            }.toList(),
                            totalPriceLabel = orderDetails.paymentData.label,
                            totalPrice = orderDetails.paymentData.value,
                            paymentInfoMessage = RechargePaymentInfoMessage(
                                message = orderDetails.digitalPaymentInfoMessage.message,
                                urlText = orderDetails.digitalPaymentInfoMessage.urlText,
                                appLink = orderDetails.digitalPaymentInfoMessage.appLink
                            ),
                            additionalTicker = additionalTickerInfo
                    ),
                    helpUrl = orderDetails.contactUs.helpUrl,
                    actionButtonList = RechargeOrderDetailActionButtonListModel(
                            actionButtons = orderDetails.actionButtons.map {
                                RechargeOrderDetailActionButtonModel(
                                        label = it.label,
                                        buttonType = it.buttonType,
                                        uri = it.uri,
                                        mappingUri = it.mappingUri,
                                        weight = it.weight,
                                        value = it.value,
                                        name = it.name,
                                        appUrl = it.body.appUrl
                                )
                            }.toList()
                    )
            )
        }

        private const val DATE_LABEL = "Tanggal"
        private const val VOUCHER_CODE_LABEL = "Kode Voucher"
    }
}
