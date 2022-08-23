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

class ItemsEvents : Visitable<OrderDetailTypeFactoryImpl> {
    override fun type(typeFactory: OrderDetailTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }
}

class ItemsInsurance : Visitable<OrderDetailTypeFactoryImpl> {
    override fun type(typeFactory: OrderDetailTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }
}

class ItemsDefault : Visitable<OrderDetailTypeFactoryImpl> {
    override fun type(typeFactory: OrderDetailTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }
}
