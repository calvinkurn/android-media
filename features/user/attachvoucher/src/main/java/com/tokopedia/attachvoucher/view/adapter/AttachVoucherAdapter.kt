package com.tokopedia.attachvoucher.view.adapter

import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.attachvoucher.view.adapter.viewholder.AttachVoucherViewHolder

class AttachVoucherAdapter(private val baseListAdapterTypeFactory: AttachVoucherTypeFactory)
    : BaseListAdapter<Visitable<*>, AttachVoucherTypeFactory>(baseListAdapterTypeFactory),
        AttachVoucherViewHolder.Listener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<out Visitable<*>> {
        val view = onCreateViewItem(parent, viewType)
        return baseListAdapterTypeFactory.createViewHolder(view, viewType, this)
    }
}