package com.tokopedia.sellerorder.waitingpaymentorder.domain.mapper

import com.tokopedia.sellerorder.waitingpaymentorder.domain.model.WaitingPaymentOrderResponse
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.model.Paging
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.model.WaitingPaymentOrder
import javax.inject.Inject

/**
 * Created by yusuf.hendrawan on 2020-09-07.
 */

class WaitingPaymentOrderResultMapper @Inject constructor() {
    fun mapDomainToModelData(result: WaitingPaymentOrderResponse.Data.WaitingPaymentOrder): Map<String, Any> {
        return mapOf(
                WaitingPaymentOrder::class.java.simpleName to result.orders.map {
                    WaitingPaymentOrder(
                            orderId = it.orderId,
                            paymentDeadline = it.paymentDeadline,
                            buyerNameAndPlace = it.buyerNameAndPlace,
                            products = it.products.map {
                                WaitingPaymentOrder.Product(
                                        id = it.id,
                                        name = it.name,
                                        picture = it.picture,
                                        quantity = it.quantity,
                                        price = it.price
                                )
                            },
                            isExpanded = false
                    )
                },
                Paging::class.java.simpleName to Paging(
                        currentPage = result.paging.currentPage,
                        batchPage = result.paging.currentBatchPage,
                        nextPaymentDeadline = result.cursorPaymentDeadline.toString()
                )
        )
    }
}