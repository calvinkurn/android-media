package com.tokopedia.homenav.mainnav.view.datamodel.orderlist

import com.tokopedia.homenav.mainnav.view.adapter.typefactory.OrderListTypeFactory
import com.tokopedia.topads.sdk.domain.model.ImpressHolder

data class OtherTransactionModel(
        val othersTransactionCount: Int
): OrderNavVisitable, ImpressHolder() {
    override fun type(factory: OrderListTypeFactory): Int {
        return factory.type(this)
    }
}