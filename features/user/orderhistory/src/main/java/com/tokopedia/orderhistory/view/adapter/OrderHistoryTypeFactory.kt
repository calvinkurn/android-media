package com.tokopedia.orderhistory.view.adapter

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.orderhistory.data.Product

interface OrderHistoryTypeFactory : AdapterTypeFactory {
    fun type(product: Product): Int
}