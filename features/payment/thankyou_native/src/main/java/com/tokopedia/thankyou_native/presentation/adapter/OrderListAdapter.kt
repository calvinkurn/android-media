package com.tokopedia.thankyou_native.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter

class OrderListAdapter(factory: OrderAdapterTypeFactory, val visitableList: ArrayList<Visitable<*>>)
    : BaseAdapter<OrderAdapterTypeFactory>(factory, visitableList) {

    fun addItem(visitableList: ArrayList<Visitable<*>>) {
        this.visitableList.addAll(visitableList)
    }
}