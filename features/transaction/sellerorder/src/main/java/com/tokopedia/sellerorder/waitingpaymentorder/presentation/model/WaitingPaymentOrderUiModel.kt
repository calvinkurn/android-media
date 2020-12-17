package com.tokopedia.sellerorder.waitingpaymentorder.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.typefactory.WaitingPaymentOrderAdapterTypeFactory
import com.tokopedia.sellerorder.waitingpaymentorder.presentation.adapter.typefactory.WaitingPaymentOrderProductsAdapterTypeFactory

/**
 * Created by yusuf.hendrawan on 2020-09-07.
 */

data class WaitingPaymentOrderUiModel(
        val orderId: String,
        val paymentDeadline: String,
        val buyerNameAndPlace: String,
        val productUiModels: List<ProductUiModel>,
        var isExpanded: Boolean
) : Visitable<WaitingPaymentOrderAdapterTypeFactory> {

    override fun type(typeFactory: WaitingPaymentOrderAdapterTypeFactory?): Int {
        return typeFactory?.type(this).orZero()
    }

    data class ProductUiModel(
            val id: String,
            val name: String,
            val picture: String,
            val quantity: Int,
            val price: String
    ) : Visitable<WaitingPaymentOrderProductsAdapterTypeFactory> {
        override fun type(typeFactory: WaitingPaymentOrderProductsAdapterTypeFactory?): Int {
            return typeFactory?.type(this).orZero()
        }
    }
}