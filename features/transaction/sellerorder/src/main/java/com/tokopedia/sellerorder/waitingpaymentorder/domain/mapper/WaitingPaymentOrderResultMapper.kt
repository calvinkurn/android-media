package com.tokopedia.sellerorder.waitingpaymentorder.domain.mapper

import com.tokopedia.sellerorder.common.util.SomConsts.KEY_WAITING_PAYMENT_ORDER_LIST_PAGING_RESULT
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_WAITING_PAYMENT_ORDER_LIST_RESULT
import com.tokopedia.sellerorder.waitingpaymentorder.domain.model.WaitingPaymentOrderResponse
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.model.Paging
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.model.WaitingPaymentOrderUiModel
import javax.inject.Inject

/**
 * Created by yusuf.hendrawan on 2020-09-07.
 */

class WaitingPaymentOrderResultMapper @Inject constructor() {
    fun mapDomainToModelData(result: WaitingPaymentOrderResponse.Data.WaitingPaymentOrder): Map<String, Any> {
        return mapOf(
            KEY_WAITING_PAYMENT_ORDER_LIST_RESULT to result.orders.map {
                WaitingPaymentOrderUiModel(
                    orderId = it.orderId,
                    paymentDeadline = it.paymentDeadline,
                    buyerNameAndPlace = it.buyerNameAndPlace,
                    productUiModels = mapProducts(
                        it.haveProductBundle,
                        it.products,
                        it.bundleDetail
                    ),
                    haveProductBundling = it.haveProductBundle,
                    isExpanded = false
                )
            },
            KEY_WAITING_PAYMENT_ORDER_LIST_PAGING_RESULT to Paging(
                nextPaymentDeadline = result.cursorPaymentDeadline
            )
        )
    }

    private fun mapProducts(
        haveProductBundle: Boolean,
        products: List<WaitingPaymentOrderResponse.Data.WaitingPaymentOrder.Order.Product>,
        bundleDetail: WaitingPaymentOrderResponse.Data.WaitingPaymentOrder.Order.BundleDetail?
    ): List<WaitingPaymentOrderUiModel.BaseProductUiModel> {
        return if (haveProductBundle) {
            bundleDetail?.nonBundle?.map {
                WaitingPaymentOrderUiModel.ProductUiModel(
                    id = it.id,
                    name = it.name,
                    picture = it.picture,
                    quantity = it.quantity,
                    price = it.price
                )
            }.orEmpty().plus(
                bundleDetail?.bundle?.map {
                    WaitingPaymentOrderUiModel.ProductBundlingUiModel(
                        name = it.name,
                        iconUrl = bundleDetail.productBundlingIcon,
                        products = it.products.map {
                            WaitingPaymentOrderUiModel.ProductUiModel(
                                id = it.id,
                                name = it.name,
                                picture = it.picture,
                                quantity = it.quantity,
                                price = it.price
                            )
                        }
                    )
                }.orEmpty()
            )
        } else {
            products.map {
                WaitingPaymentOrderUiModel.ProductUiModel(
                    id = it.id,
                    name = it.name,
                    picture = it.picture,
                    quantity = it.quantity,
                    price = it.price
                )
            }
        }
    }
}