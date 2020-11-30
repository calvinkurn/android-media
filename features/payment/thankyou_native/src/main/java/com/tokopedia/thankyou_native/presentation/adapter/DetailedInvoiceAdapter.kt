package com.tokopedia.thankyou_native.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.thankyou_native.presentation.adapter.factory.InvoiceTypeFactory

class DetailedInvoiceAdapter(val visitableList: ArrayList<Visitable<*>>,
                             typeFactory: InvoiceTypeFactory) :
        BaseAdapter<InvoiceTypeFactory>(typeFactory, visitableList) {
    fun addItems(data: ArrayList<Visitable<*>>) {
        visitableList.clear()
        visitableList.addAll(data)
    }
}