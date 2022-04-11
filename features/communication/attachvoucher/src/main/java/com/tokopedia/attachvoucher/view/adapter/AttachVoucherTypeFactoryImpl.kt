package com.tokopedia.attachvoucher.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.attachvoucher.data.EmptyVoucherUiModel
import com.tokopedia.attachvoucher.data.VoucherUiModel
import com.tokopedia.attachvoucher.view.adapter.viewholder.AttachVoucherViewHolder
import com.tokopedia.attachvoucher.view.adapter.viewholder.EmptyAttachVoucherViewHolder
import com.tokopedia.attachvoucher.view.adapter.viewholder.EmptyVoucherViewHolder

class AttachVoucherTypeFactoryImpl : BaseAdapterTypeFactory(), AttachVoucherTypeFactory {

    override fun type(viewModel: EmptyModel?): Int {
        return EmptyAttachVoucherViewHolder.LAYOUT
    }

    override fun type(voucher: VoucherUiModel): Int {
        return AttachVoucherViewHolder.LAYOUT
    }

    override fun type(emptyVoucher: EmptyVoucherUiModel): Int {
        return EmptyVoucherViewHolder.LAYOUT
    }

    override fun createViewHolder(
            view: View?,
            viewType: Int,
            listener: AttachVoucherViewHolder.Listener
    ): AbstractViewHolder<out Visitable<*>> {
        return when (viewType) {
            AttachVoucherViewHolder.LAYOUT -> AttachVoucherViewHolder(view, listener)
            EmptyAttachVoucherViewHolder.LAYOUT -> EmptyAttachVoucherViewHolder(view)
            EmptyVoucherViewHolder.LAYOUT -> EmptyVoucherViewHolder(view)
            else -> createViewHolder(view, viewType)
        }
    }

}