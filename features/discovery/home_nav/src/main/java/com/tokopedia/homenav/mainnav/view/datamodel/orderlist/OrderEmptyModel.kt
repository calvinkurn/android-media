package com.tokopedia.homenav.mainnav.view.datamodel.orderlist

import com.tokopedia.homenav.mainnav.view.adapter.typefactory.OrderListTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder

/**
 * Created by dhaba
 */
class OrderEmptyModel : OrderNavVisitable, ImpressHolder() {
    override fun type(factory: OrderListTypeFactory): Int {
        return factory.type(this)
    }
}