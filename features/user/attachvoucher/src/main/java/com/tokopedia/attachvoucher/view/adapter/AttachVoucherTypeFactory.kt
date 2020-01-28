package com.tokopedia.attachvoucher.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.attachvoucher.data.Voucher
import com.tokopedia.attachvoucher.view.adapter.viewholder.AttachVoucherViewHolder

interface AttachVoucherTypeFactory : AdapterTypeFactory {
    fun type(voucher: Voucher): Int
    fun createViewHolder(
            view: View?,
            viewType: Int,
            listener: AttachVoucherViewHolder.Listener
    ): AbstractViewHolder<out Visitable<*>>
}