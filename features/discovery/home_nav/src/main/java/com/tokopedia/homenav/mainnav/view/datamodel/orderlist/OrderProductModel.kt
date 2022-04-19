package com.tokopedia.homenav.mainnav.view.datamodel.orderlist

import com.tokopedia.homenav.mainnav.domain.model.NavProductOrder
import com.tokopedia.homenav.mainnav.view.adapter.typefactory.OrderListTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder

data class OrderProductModel(
        val navProductModel: NavProductOrder
): OrderNavVisitable, ImpressHolder() {
    override fun type(factory: OrderListTypeFactory): Int {
        return factory.type(this)
    }
}