package com.tokopedia.attachvoucher.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter

class AttachVoucherAdapter(private val baseListAdapterTypeFactory: AttachVoucherTypeFactory)
    : BaseListAdapter<Visitable<*>, AttachVoucherTypeFactory>(baseListAdapterTypeFactory) {

}