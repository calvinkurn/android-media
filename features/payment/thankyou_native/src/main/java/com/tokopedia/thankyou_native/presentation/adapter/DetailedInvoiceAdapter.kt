package com.tokopedia.thankyou_native.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter

class DetailedInvoiceAdapter(visitables: List<Visitable<*>>,
                             typeFactory: InvoiceTypeFactory) :
        BaseAdapter<InvoiceTypeFactory>(typeFactory, visitables)