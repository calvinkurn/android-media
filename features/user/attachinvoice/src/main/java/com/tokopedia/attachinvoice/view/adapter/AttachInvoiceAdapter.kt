package com.tokopedia.attachinvoice.view.adapter

import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.attachinvoice.data.Invoice
import com.tokopedia.attachinvoice.view.adapter.viewholder.AttachInvoiceViewHolder

class AttachInvoiceAdapter(private val baseListAdapterTypeFactory: AttachInvoiceTypeFactory)
    : BaseListAdapter<Visitable<*>, AttachInvoiceTypeFactory>(baseListAdapterTypeFactory),
        AttachInvoiceViewHolder.Listener {

    val selectedInvoice: MutableLiveData<Invoice?> = MutableLiveData()
    private var selectedInvoicePosition: Int = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<out Visitable<*>> {
        val view = onCreateViewItem(parent, viewType)
        return baseListAdapterTypeFactory.createViewHolder(view, viewType, this)
    }

    override fun checkCurrentItem(element: Invoice, position: Int) {
        selectedInvoice.postValue(element)
        selectedInvoicePosition = position
    }

    override fun uncheckPreviousItem() {
        if (selectedInvoice.value == null || selectedInvoicePosition == RecyclerView.NO_POSITION) return
        notifyItemChanged(selectedInvoicePosition, AttachInvoiceViewHolder.PAYLOAD_UNCHECK)
        selectedInvoice.postValue(null)
        selectedInvoicePosition = RecyclerView.NO_POSITION
    }

}