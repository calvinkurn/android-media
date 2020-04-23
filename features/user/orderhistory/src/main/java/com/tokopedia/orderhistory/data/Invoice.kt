package com.tokopedia.orderhistory.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.orderhistory.view.adapter.OrderHistoryTypeFactory

class Invoice: Visitable<OrderHistoryTypeFactory> {
    override fun type(typeFactory: OrderHistoryTypeFactory?): Int {
        return 0
    }
}