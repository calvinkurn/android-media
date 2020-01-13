package com.tokopedia.attachvoucher.view.adapter

import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.attachvoucher.data.Voucher
import com.tokopedia.attachvoucher.view.adapter.viewholder.AttachVoucherViewHolder

class AttachVoucherAdapter(private val baseListAdapterTypeFactory: AttachVoucherTypeFactory)
    : BaseListAdapter<Visitable<*>, AttachVoucherTypeFactory>(baseListAdapterTypeFactory),
        AttachVoucherViewHolder.Listener {

    val selectedInvoice: MutableLiveData<Voucher?> = MutableLiveData()
    private var selectedInvoicePosition: Int = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<out Visitable<*>> {
        val view = onCreateViewItem(parent, viewType)
        return baseListAdapterTypeFactory.createViewHolder(view, viewType, this)
    }

    override fun checkCurrentItem(element: Voucher, position: Int) {
        selectedInvoice.postValue(element)
        selectedInvoicePosition = position
    }

    override fun uncheckPreviousItem() {
        if (selectedInvoice.value == null || selectedInvoicePosition == RecyclerView.NO_POSITION) return
        notifyItemChanged(selectedInvoicePosition, AttachVoucherViewHolder.PAYLOAD_UNCHECK)
        selectedInvoice.postValue(null)
        selectedInvoicePosition = RecyclerView.NO_POSITION
    }

    override fun isChecked(element: Voucher): Boolean {
        return selectedInvoice.value == element
    }
}