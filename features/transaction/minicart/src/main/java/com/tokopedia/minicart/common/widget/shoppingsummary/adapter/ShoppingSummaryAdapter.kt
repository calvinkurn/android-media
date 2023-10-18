package com.tokopedia.minicart.common.widget.shoppingsummary.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter

class ShoppingSummaryAdapter(val items: List<Visitable<*>>, adapterTypeFactory: ShoppingSummaryAdapterTypeFactory) :
    BaseListAdapter<Visitable<*>, ShoppingSummaryAdapterTypeFactory>(adapterTypeFactory) {

    init {
        list.clear()
        list.addAll(items)
    }
}
