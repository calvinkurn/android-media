package com.tokopedia.thankyou_native.presentation.adapter.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.thankyou_native.presentation.adapter.OrderAdapterTypeFactory

data class ShopItemAdapterModel(
        val shopName: String
) : Visitable<OrderAdapterTypeFactory> {

    override fun type(typeFactory: OrderAdapterTypeFactory?): Int {
        return typeFactory?.type(this)!!
    }
}