package com.tokopedia.mvc.presentation.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.mvc.databinding.SmvcItemVoucherBinding
import com.tokopedia.mvc.domain.entity.Voucher

class VouchersAdapter(
    private val listener: VoucherAdapterListener
): RecyclerView.Adapter<VouchersViewHolder>() {

    private var data: MutableList<Voucher> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VouchersViewHolder {
        val binding = SmvcItemVoucherBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return VouchersViewHolder(binding, listener)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: VouchersViewHolder, position: Int) {
        data.getOrNull(position)?.let {
            holder.bind(it)
        }
    }

    fun clearDataList() {
        val oldDataSize = data.size
        data = mutableListOf()
        notifyItemRangeRemoved(Int.ZERO, oldDataSize)
    }

    fun addDataList(newData: List<Voucher>) {
        val oldDataSize = data.size
        data.addAll(newData)
        notifyItemRangeInserted(oldDataSize, oldDataSize + newData.size)
    }
}
