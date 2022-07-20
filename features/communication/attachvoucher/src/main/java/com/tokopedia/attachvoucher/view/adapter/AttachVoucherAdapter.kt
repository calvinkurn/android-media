package com.tokopedia.attachvoucher.view.adapter

import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.attachvoucher.data.VoucherUiModel
import com.tokopedia.attachvoucher.view.adapter.viewholder.AttachVoucherViewHolder

class AttachVoucherAdapter(private val baseListAdapterTypeFactory: AttachVoucherTypeFactory)
    : BaseListAdapter<Visitable<*>, AttachVoucherTypeFactory>(baseListAdapterTypeFactory),
        AttachVoucherViewHolder.Listener {

    val selectedVoucher: MutableLiveData<VoucherUiModel?> = MutableLiveData()
    private var selectedVoucherPosition: Int = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<out Visitable<*>> {
        val view = onCreateViewItem(parent, viewType)
        return baseListAdapterTypeFactory.createViewHolder(view, viewType, this)
    }

    override fun checkCurrentItem(element: VoucherUiModel, position: Int) {
        selectedVoucher.postValue(element)
        selectedVoucherPosition = position
    }

    override fun uncheckPreviousItem() {
        if (selectedVoucher.value == null || selectedVoucherPosition == RecyclerView.NO_POSITION) return
        notifyItemChanged(selectedVoucherPosition, AttachVoucherViewHolder.PAYLOAD_UNCHECK)
        selectedVoucher.postValue(null)
        selectedVoucherPosition = RecyclerView.NO_POSITION
    }

    override fun isChecked(element: VoucherUiModel): Boolean {
        return selectedVoucher.value == element
    }

    fun clearSelected() {
        selectedVoucher.value = null
        selectedVoucherPosition = RecyclerView.NO_POSITION
    }
}