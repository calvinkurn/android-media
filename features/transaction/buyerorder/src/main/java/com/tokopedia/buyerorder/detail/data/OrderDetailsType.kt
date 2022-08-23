package com.tokopedia.buyerorder.detail.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.buyerorder.detail.revamp.adapter.OrderDetailTypeFactoryImpl

/**
 * created by @bayazidnasir on 23/8/2022
 */

class ItemsDealsShort : Visitable<OrderDetailTypeFactoryImpl> {
    override fun type(typeFactory: OrderDetailTypeFactoryImpl): Int {
       return typeFactory.type(this)
    }
}

class ItemsDealsOMP : Visitable<OrderDetailTypeFactoryImpl> {
    override fun type(typeFactory: OrderDetailTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }
}

class ItemsDeals : Visitable<OrderDetailTypeFactoryImpl> {
    override fun type(typeFactory: OrderDetailTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }
}

class ItemsEvents(
    val orderDetails: OrderDetails,
    val item: Items,
) : Visitable<OrderDetailTypeFactoryImpl> {
    override fun type(typeFactory: OrderDetailTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }
}

class ItemsInsurance : Visitable<OrderDetailTypeFactoryImpl> {
    override fun type(typeFactory: OrderDetailTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }
}

data class ItemsDefault(val item: Items) : Visitable<OrderDetailTypeFactoryImpl> {
    override fun type(typeFactory: OrderDetailTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }
}
