package com.tokopedia.buyerorder.detail.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.buyerorder.detail.revamp.adapter.OrderDetailTypeFactoryImpl

/**
 * created by @bayazidnasir on 23/8/2022
 */

data class ItemsDealsShort(val item: Items) : Visitable<OrderDetailTypeFactoryImpl> {
    override fun type(typeFactory: OrderDetailTypeFactoryImpl): Int {
       return typeFactory.type(this)
    }
}

data class ItemsDealsOMP(
    val orderDetails: OrderDetails,
    val item: Items,
) : Visitable<OrderDetailTypeFactoryImpl> {
    override fun type(typeFactory: OrderDetailTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }
}

data class ItemsDeals(
    val orderDetails: OrderDetails,
    val item: Items,
) : Visitable<OrderDetailTypeFactoryImpl> {
    override fun type(typeFactory: OrderDetailTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }
}

data class ItemsEvents(
    val orderDetails: OrderDetails,
    val item: Items,
) : Visitable<OrderDetailTypeFactoryImpl> {
    override fun type(typeFactory: OrderDetailTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }
}

data class ItemsInsurance(
    val orderDetails: OrderDetails,
    val item: Items,
) : Visitable<OrderDetailTypeFactoryImpl> {
    override fun type(typeFactory: OrderDetailTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }
}

data class ItemsDefault(
    val orderDetails: OrderDetails,
    val item: Items,
) : Visitable<OrderDetailTypeFactoryImpl> {
    override fun type(typeFactory: OrderDetailTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }
}
