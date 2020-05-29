package com.tokopedia.buyerorder.list.view.adapter.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.buyerorder.list.view.adapter.factory.OrderListTypeFactory

class OrderListRecomTitleViewModel(var title:String) : Visitable<OrderListTypeFactory> {
    override fun type(typeFactory: OrderListTypeFactory): Int {
        return typeFactory.type(this)
    }
}
