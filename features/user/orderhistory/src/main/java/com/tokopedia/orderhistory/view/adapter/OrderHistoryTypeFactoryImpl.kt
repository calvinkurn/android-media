package com.tokopedia.orderhistory.view.adapter

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.orderhistory.data.Product

class OrderHistoryTypeFactoryImpl() : BaseAdapterTypeFactory(), OrderHistoryTypeFactory {

    override fun type(product: Product): Int {
        return -1
    }

}