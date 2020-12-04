package com.tokopedia.homenav.mainnav.view.viewmodel.orderlist

import com.tokopedia.homenav.mainnav.domain.model.NavPaymentOrder
import com.tokopedia.homenav.mainnav.view.adapter.typefactory.OrderListTypeFactory
import com.tokopedia.topads.sdk.domain.model.ImpressHolder

data class OrderPaymentModel(
        val navPaymentModel: NavPaymentOrder
): OrderNavVisitable, ImpressHolder() {
    override fun type(factory: OrderListTypeFactory): Int {
        return factory.type(this)
    }
}